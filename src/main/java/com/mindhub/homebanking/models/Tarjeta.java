package com.mindhub.homebanking.models;

import antlr.Utils;
import com.mindhub.homebanking.Utils.CardsUtils;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Tarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String cardHolder;
    private CardType type;
    private ColorType color;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;


    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;


    public Tarjeta(CardType type, ColorType color, LocalDate fromDate, LocalDate thruDate) {
        this.type = type;
        this.color = color;
//        this.number = CardsUtils.GenerateCardNumber();
//        this.cvv = CardsUtils.generarCodigoSeguridad();
        this.fromDate = fromDate;
        this.thruDate = thruDate;
    }




    public Tarjeta() {
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public ColorType getColor() {
        return color;
    }

    public void setColor(ColorType color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }


    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
