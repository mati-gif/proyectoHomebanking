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
import org.springframework.stereotype.Service;

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
//metodo que utiliza un metodo personalizado que cuenta cuantas tarjetas ,hay en la base de datos, con el color y tipo expecificado tiene el cliente
    @Override
    public long countCardsByClientAndColorAndType(Client client, ColorType color, CardType type) {
        return cardRepository.countByClientAndColorAndType(client, color, type);
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
        card.setType(createCardDto.type());
        card.setColor(createCardDto.color());
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
   public int generateUniqueCvv(){
        int cvv; // Estoy declarando una variable pero no inicializandola.
        do{
            cvv = generarCodigoSeguridad(); //metodo de cardUtils.
        }while(exitsCvv(cvv));
        return cvv;
    }

    @Override
    public Boolean exitsCvv(int cvv){
        return cardRepository.existsByCvv(cvv);
    }

//---------------------------------------------------------------
//---------------------------------------------------------------
//Metodo que valida que los datos de la tarjeta sean correctos.
    @Override
    public void validateCardCreation(CreateCardDto createCardDto) {
        if (createCardDto.type() == null) {
            throw new IllegalArgumentException("Card type must be specified");
        }
        if (createCardDto.color() == null) {
            throw new IllegalArgumentException("Card color must be specified");
        }
    }
//---------------------------------------------------------------

//---------------------------------------------------------------
//Metodo que valida que el cliente no tenga mas de una tarjeta de credito con el mismo color y tipo.
    @Override
    public void checkCardLimit(Client client, CreateCardDto createCardDto) {
        long countCardsByType = countCardsByClientAndColorAndType(client, createCardDto.color(), createCardDto.type());
        if (countCardsByType == 1) {
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


}
