package com.mindhub.homebanking.controllers;


import antlr.Utils;
import com.mindhub.homebanking.Utils.CardsUtils;
import com.mindhub.homebanking.dtos.CardDto;
import com.mindhub.homebanking.dtos.CreateCardDto;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
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
    private ClientService clientService;

    @Autowired
    private CardService cardService;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<?> createCardForCurrentClient(Authentication authentication, @RequestBody CreateCardDto createCardDto){

        //Estoy valindo las entradas de datos que vienen por parametro.
        //Si alguno de los dos campos es nulo, devuelvo un error.

        if (createCardDto.type() == null ) {
            return new ResponseEntity<>("Card type must be specified", HttpStatus.FORBIDDEN);
        }
        if (createCardDto.color() == null ) {
            System.out.println(createCardDto.color());
            return new ResponseEntity<>("Card color must be specified", HttpStatus.FORBIDDEN);
        }

        // Obtener el cliente autenticado
        Client client = clientService.findClientByEmail(authentication.getName());

        long countCardsByType = cardService.countCardsByClientAndColorAndType(client, createCardDto.color(), createCardDto.type());
        if (countCardsByType == 1 ) {
            return new ResponseEntity<>("You can't have more than one " + createCardDto.type() + " " + createCardDto.color() , HttpStatus.FORBIDDEN);
        }

        // Crear la tarjeta
        Card card = cardService.buildCard(client, createCardDto);
        cardService.saveCard(card);

        return new ResponseEntity<>("The card has been created, you have one new card " + "type: " + createCardDto.type() + " color: " + createCardDto.color(),HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/cards")
    public ResponseEntity<?> getClientCards(Authentication authentication){

        Client client = clientService.findClientByEmail(authentication.getName());
        Set<Card> clientCards = client.getCards();
        Set<CardDto> cardDto = clientCards.stream().map(CardDto::new).collect(Collectors.toSet());

        return new ResponseEntity<>(cardDto, HttpStatus.OK);
    }

}
