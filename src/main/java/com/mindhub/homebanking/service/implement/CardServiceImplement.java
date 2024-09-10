package com.mindhub.homebanking.service.implement;


import com.mindhub.homebanking.Utils.CardsUtils;
import com.mindhub.homebanking.dtos.CardDto;
import com.mindhub.homebanking.dtos.CreateCardDto;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.AuthService;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.Utils.CardsUtils.GenerateCardNumber;
import static com.mindhub.homebanking.Utils.CardsUtils.generarCodigoSeguridad;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    public CardRepository cardRepository;

    @Autowired
    public CardsUtils cardsUtils;

    @Autowired
    public ClientService clientService;

    @Autowired
    public AccountService accountService;

//-------------------------------------------------------------------------------
    //metodo peincipal que  contiene toda la logica para crear una tarjeta para un cliente


    @Override
    public void createCardForClient(Authentication authentication, CreateCardDto createCardDto) {
        // Obtener el cliente autenticado
        Client client = clientService.findClientByEmail(authentication.getName());

        // Validar la entrada de datos y verificar el límite de tarjetas
        validateCardCreation(createCardDto);
        checkCardLimit(client, createCardDto);

        // Crear y guardar la tarjeta
        Card card = buildCard(client, createCardDto);
        saveCard(card);
    }

    //-------------------------------------------------------------------------------
//metodo que utiliza un metodo personalizado que cuenta ,en la base de datos, cuantas tarjetas tiene un cliente expecifico con el color y tipo determinado
    @Override
    public Boolean exitsCardByClientAndColorAndType(Client client, ColorType color, CardType type) {
        return cardRepository.existsCardsByClientAndColorAndType(client, color, type);
    }
    //-------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    //metodo para guardar una cuenta en la base de datos a traves del metodo .save()
    @Override
    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

//-------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
//Metodo para crear una instancia de una tarjeta.
    @Override
    public Card buildCard(Client client, CreateCardDto createCardDto) {

        String cardNumber = generateUniqueCardNumber();
        int cvv = generateUniqueCvv();


        Card card = new Card();
        card.setClient(client);
        card.setType(CardType.valueOf(createCardDto.type()));
        card.setColor(ColorType.valueOf(createCardDto.color()));
        card.setNumber(cardNumber);
        card.setCvv(cvv);
        card.setFromDate(cardsUtils.generateFromDate());
        card.setThruDate(cardsUtils.generateExpirationDate());
        card.setCardHolder(client.getFirstName() + " " + client.getLastName()); // Establecer el nombre del titular de la tarjeta
        client.addCards(card);
        return card;
    }
//-------------------------------------------------------------------------------

//--------------------------------------------------------------------------------
    //Verifica que el numero de la tarjeta no exista en la base de datos.

    @Override
    public String generateUniqueCardNumber() {
        String cardNumber;// Estoy declarando una variable pero no inicializandola.
        do {
            cardNumber = GenerateCardNumber(); //metodo de cardUtils.
        } while (exitsCardNumber(cardNumber));
        return cardNumber;
    }

    @Override
    public Boolean exitsCardNumber(String number) {
        return cardRepository.existsCardNumberByNumber(number);
    }

    //-----------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------
    //Verifica que el cvv no exista en la base de datos.

    @Override
    public int generateUniqueCvv() {
        int cvv; // Estoy declarando una variable pero no inicializandola.
        do {
            cvv = generarCodigoSeguridad(); //metodo de cardUtils.
        } while (exitsCvv(cvv));
        return cvv;
    }

    @Override
    public Boolean exitsCvv(int cvv) {
        return cardRepository.existsByCvv(cvv);
    }

    //---------------------------------------------------------------
//---------------------------------------------------------------
//Metodo que valida que los datos de la tarjeta sean correctos.
    @Override
    public void validateCardCreation(CreateCardDto createCardDto) {

        // Verificar que el tipo no sea nulo ni esté vacío
        if (createCardDto.type() == null || createCardDto.type().isBlank()) {
            throw new IllegalArgumentException("Card type must be specified");
        }

        // Verificar que el tipo sea válido en el enum CardType
        try {
            CardType.valueOf(createCardDto.type().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid card type: " + createCardDto.type());
        }
        // Verificar que el color no sea nulo ni esté vacío
        if (createCardDto.color() == null || createCardDto.color().isBlank()) {
            throw new IllegalArgumentException("Card color must be specified");
        }

        // Verificar que el color sea válido en el enum ColorType
        try {
            ColorType.valueOf(createCardDto.color().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid card color: " + createCardDto.color());
        }
    }
//---------------------------------------------------------------

    //---------------------------------------------------------------
//Metodo que valida que el cliente no tenga mas de una tarjeta de credito con el mismo color y tipo.
    @Override
    public void checkCardLimit(Client client, CreateCardDto createCardDto) {
        Boolean countCardsByType = exitsCardByClientAndColorAndType(client, ColorType.valueOf(createCardDto.color()), CardType.valueOf(createCardDto.type()));
        if (countCardsByType) {
            throw new IllegalArgumentException("You can't have more than one " + createCardDto.type() + " " + createCardDto.color());
        }
    }

//---------------------------------------------------------------

    //---------------------------------------------------------------
//Metodo que busca en la base de datos un cliente por su email devuelve un Set<CardDto> que contienen todos las tarjetas del cliente.
    @Override
    public Set<CardDto> getClientCardsForCurrentClient(String email) {
        Client client = clientService.findClientByEmail(email);
        return getClientCardDtos(client);
    }

    //---------------------------------------------------------------
    //Metodo que toma un cliente y devuelve un Set<CardDto> que contienen todas las tarjetas del cliente.
    @Override
    public Set<CardDto> getClientCardDtos(Client client) {
        return client.getCards().stream()
                .map(CardDto::new)
                .collect(Collectors.toSet());
    }


    //-------------------------------------------------------------------
    //metodo que obtiene las tarjetas disponibles para el usuario, es de prueba no esta en el requerimiento.(borrar si hace falta)
    @Override
    public List<String> getAvailableCardsForUser(Client client) {
        // Definir todas las combinaciones posibles de tipo y color
        Set<String> allCardCombinations = new HashSet<>();
        for (CardType type : CardType.values()) {
            for (ColorType color : ColorType.values()) {
                allCardCombinations.add(type.name() + "-" + color.name());
            }
        }

        // Obtener las combinaciones de tarjetas que el usuario ya tiene
        Set<String> userCardCombinations = client.getCards().stream()
                .map(card -> card.getType().name() + "-" + card.getColor().name())
                .collect(Collectors.toSet());

        // Filtrar las combinaciones que el usuario ya tiene para obtener las disponibles
        Set<String> availableCardCombinations = new HashSet<>(allCardCombinations);
        availableCardCombinations.removeAll(userCardCombinations);

        // Crear una lista de mensajes personalizados
        List<String> personalizedMessages = new ArrayList<>();
        for (String combination : availableCardCombinations) {
            String[] parts = combination.split("-"); // Separar tipo y color
            String type = parts[0];
            String color = parts[1];

            String message = "Puedes solicitar una tarjeta de " + type.toLowerCase() + " de color " + color.toLowerCase() + ".";
            personalizedMessages.add(message);
        }

        return personalizedMessages;
    }


}
