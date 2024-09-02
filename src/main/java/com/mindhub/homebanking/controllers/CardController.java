package com.mindhub.homebanking.controllers;


import antlr.Utils;
import com.mindhub.homebanking.Utils.CardsUtils;
import com.mindhub.homebanking.dtos.CardDto;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardsUtils cardsUtils;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<?> createCardForCurrentClient(Authentication authentication, @RequestBody Map<String, String> cardDetails){


        String stringType = cardDetails.get("type");
        String stringColor = cardDetails.get("color");


        //Estoy valindo las entradas de datos que vienen por parametro.
        //Si alguno de los dos campos es nulo, devuelvo un error.
        if (stringType == null || stringType.trim().isEmpty()) {
            return new ResponseEntity<>("Card type must be specified", HttpStatus.FORBIDDEN);
        }
        if (stringColor == null || stringColor.trim().isEmpty()) {
            return new ResponseEntity<>("Card color must be specified", HttpStatus.FORBIDDEN);
        }


        CardType type = CardType.valueOf(stringType.toUpperCase());
        ColorType color = ColorType.valueOf(stringColor.toUpperCase());

        // Obtener el cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName());


        long countCardsByType = cardRepository.countByClientAndColorAndType(client,color, type);
        if (countCardsByType == 1 ) {
            return new ResponseEntity<>("You can't have more than one " + type + " " + color , HttpStatus.FORBIDDEN);
        }

        // Crear la tarjeta
        Card card = new Card();
        card.setClient(client);
        card.setType(type);
        card.setColor(color);
        card.setNumber(cardsUtils.GenerateCardNumber());
        card.setCvv(cardsUtils.generarCodigoSeguridad());
        card.setFromDate(cardsUtils.generateFromDate());
        card.setThruDate(cardsUtils.generateExpirationDate());
        card.setCardHolder(client.getFirstName() + " " + client.getLastName()); // Establecer el nombre del titular de la tarjeta
        client.addCards(card);
        cardRepository.save(card);


        return new ResponseEntity<>("The card has been created, you have one new  " + "tipo: " + type + " color: " + color,HttpStatus.CREATED);



    }

    @GetMapping("/clients/current/cards")
    public ResponseEntity<?> getClientCards(Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());
        Set<Card> clientCards = client.getCards();
        Set<CardDto> cardDto = clientCards.stream().map(CardDto::new).collect(Collectors.toSet());

        return new ResponseEntity<>(cardDto, HttpStatus.OK);
    }

}
