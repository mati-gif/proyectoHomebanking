package com.mindhub.homebanking.service.implement;


import com.mindhub.homebanking.Utils.CardsUtils;
import com.mindhub.homebanking.dtos.CreateCardDto;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    public CardRepository cardRepository;

    @Autowired
    private CardsUtils cardsUtils;





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

}
