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

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    public ClientService clientService;

    @Autowired
    public CardService cardService;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<?> createCardForCurrentClient(Authentication authentication, @RequestBody CreateCardDto createCardDto){

        try {

            cardService.createCardForClient(authentication, createCardDto);
            return new ResponseEntity<>("The card has been created, you have one new card " +
                    "type: " + createCardDto.type() + " color: " + createCardDto.color(), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }catch (Exception e){
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



    //metodo para obtener las tarjetas disponibles para el cliente. es de prueba ,no esta en el reqerimiento.(borrar si hace falta)
    @GetMapping("/clients/current/availables")
    public ResponseEntity<?> getAvailableCardsToChoose(Authentication authentication) {
        // Obtener el cliente autenticado
        Client client = clientService.findClientByEmail(authentication.getName());

        // Obtener las tarjetas que todavía puede solicitar
        List<String> availableCards = cardService.getAvailableCardsForUser(client);

        if (availableCards.isEmpty()) {
            return new ResponseEntity<>("No more cards available.", HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(availableCards, HttpStatus.OK);
    }



}


