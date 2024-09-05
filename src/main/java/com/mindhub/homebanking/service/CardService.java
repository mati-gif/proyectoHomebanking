package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CreateCardDto;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;

public interface CardService {
    long countCardsByClientAndColorAndType(Client client, ColorType color, CardType type);
    Card saveCard(Card card);
    Card buildCard(Client client, CreateCardDto createCardDto);//metodo para crear una instancia de una tarjeta.
}
