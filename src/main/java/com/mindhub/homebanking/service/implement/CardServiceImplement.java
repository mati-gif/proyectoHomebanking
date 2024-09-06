package com.mindhub.homebanking.service.implement;


import com.mindhub.homebanking.Utils.CardsUtils;
import com.mindhub.homebanking.dtos.CardDto;
import com.mindhub.homebanking.dtos.CreateCardDto;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    public CardRepository cardRepository;

    @Autowired
    private CardsUtils cardsUtils;

    @Autowired
    private ClientService clientService;



    @Override
    public long countCardsByClientAndColorAndType(Client client, ColorType color, CardType type) {
        return cardRepository.countByClientAndColorAndType(client, color, type);
    }

    @Override
    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Card buildCard(Client client, CreateCardDto createCardDto) {
        Card card = new Card();
        card.setClient(client);
        card.setType(createCardDto.type());
        card.setColor(createCardDto.color());
        card.setNumber(cardsUtils.GenerateCardNumber());
        card.setCvv(cardsUtils.generarCodigoSeguridad());
        card.setFromDate(cardsUtils.generateFromDate());
        card.setThruDate(cardsUtils.generateExpirationDate());
        card.setCardHolder(client.getFirstName() + " " + client.getLastName()); // Establecer el nombre del titular de la tarjeta
        client.addCards(card);
        return card;
    }

    public void validateCardCreation(CreateCardDto createCardDto) {
        if (createCardDto.type() == null) {
            throw new IllegalArgumentException("Card type must be specified");
        }
        if (createCardDto.color() == null) {
            throw new IllegalArgumentException("Card color must be specified");
        }
    }

    public void checkCardLimit(Client client, CreateCardDto createCardDto) {
        long countCardsByType = countCardsByClientAndColorAndType(client, createCardDto.color(), createCardDto.type());
        if (countCardsByType >= 1) {
            throw new IllegalArgumentException("You can't have more than one " + createCardDto.type() + " " + createCardDto.color());
        }
    }




    @Override
    public Set<CardDto> getClientCardsForCurrentClient(String email) {
        Client client = clientService.findClientByEmail(email);
        return getClientCardDtos(client);
    }

    @Override
    public Set<CardDto> getClientCardDtos(Client client) {
        return client.getCards().stream()
                .map(CardDto::new)
                .collect(Collectors.toSet());
    }


}
