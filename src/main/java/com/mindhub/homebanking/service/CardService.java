package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CardDto;
import com.mindhub.homebanking.dtos.CreateCardDto;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;

import java.util.Set;

public interface CardService {
    long countCardsByClientAndColorAndType(Client client, ColorType color, CardType type);
    Card saveCard(Card card);
    Card buildCard(Client client, CreateCardDto createCardDto);//metodo para crear una instancia de una tarjeta.
    void checkCardLimit(Client client, CreateCardDto createCardDto);
    void validateCardCreation(CreateCardDto createCardDto);
    Set<CardDto> getClientCardDtos(Client client);
//    Client findClientByEmail(String email);
    Set<CardDto> getClientCardsForCurrentClient(String email);
}
