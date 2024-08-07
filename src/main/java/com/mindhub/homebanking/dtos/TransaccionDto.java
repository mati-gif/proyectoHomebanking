package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.Transaccion;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransaccionDto {

    private Long id;
    private double amount;
    private String description;
    private LocalDateTime date;
    private TransactionType type;


    public TransaccionDto(Transaccion transaccion){
        this.id = transaccion.getId();
        this.amount = transaccion.getAmount();
        this.description = transaccion.getDescription();
        this.date = transaccion.getDate();
        this.type = transaccion.getType();


    }


    public Long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }
}
