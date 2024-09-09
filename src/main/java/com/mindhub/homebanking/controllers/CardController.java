package com.mindhub.homebanking.controllers;



import com.mindhub.homebanking.dtos.CardDto;
import com.mindhub.homebanking.dtos.CreateCardDto;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<?> createCardForCurrentClient(Authentication authentication, @RequestBody CreateCardDto createCardDto){

        try {
            // Obtener el cliente autenticado
            Client client = clientService.findClientByEmail(authentication.getName());

            // Validar la entrada de datos y verificar el límite de tarjetas
            cardService.validateCardCreation(createCardDto);
            cardService.checkCardLimit(client, createCardDto);

            // Crear y guardar la tarjeta
            Card card = cardService.buildCard(client, createCardDto);
            cardService.saveCard(card);

            return new ResponseEntity<>("The card has been created, you have one new card " +
                    "type: " + createCardDto.type() + " color: " + createCardDto.color(), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/clients/current/cards")
    public ResponseEntity<?> getClientCards(Authentication authentication){

        try {
            // Obtener las tarjetas del cliente
            Set<CardDto> cardDtos = cardService.getClientCardsForCurrentClient(authentication.getName());
            return new ResponseEntity<>(cardDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    }


