package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.models.Tarjeta;

import java.time.LocalDate;

public class TarjetaDto {

    private Long id;
    private String cardHolder;
    private CardType type;
    private ColorType color;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;




    public TarjetaDto(Tarjeta tarjeta){
        this.id = tarjeta.getId();
        this.cardHolder = tarjeta.getCardHolder();
        this.type = tarjeta.getType();
        this.color = tarjeta.getColor();
        this.number = tarjeta.getNumber();
        this.cvv = tarjeta.getCvv();
        this.fromDate = tarjeta.getFromDate();
        this.thruDate = tarjeta.getThruDate();
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
