package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CardDto;
import com.mindhub.homebanking.dtos.CreateCardDto;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface CardService {
    Boolean exitsCardByClientAndColorAndType(Client client, ColorType color, CardType type);
    Card saveCard(Card card);
    Card buildCard(Client client, CreateCardDto createCardDto);//metodo para crear una instancia de una tarjeta.
    void checkCardLimit(Client client, CreateCardDto createCardDto);
    void validateCardCreation(CreateCardDto createCardDto);
    Set<CardDto> getClientCardDtos(Client client);
//    Client findClientByEmail(String email);
    Set<CardDto> getClientCardsForCurrentClient(String email);
    String generateUniqueCardNumber();
    Boolean exitsCardNumber(String number);


    void createCardForClient(Authentication authentication, CreateCardDto createCardDto);

    List<String> getAvailableCardsForUser(Client client);


    Card getCardByNumber(String cardNumber);//busca una tarjeta por su nuemero y la devuelve

}
