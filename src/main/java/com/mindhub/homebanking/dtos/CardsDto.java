package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.models.Card;

import java.time.LocalDate;

public class CardsDto {

    private Long id;
    private String cardHolder;
    private CardType type;
    private ColorType color;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;




    public CardsDto(Card card){
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
    }


    public Long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public ColorType getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
}
