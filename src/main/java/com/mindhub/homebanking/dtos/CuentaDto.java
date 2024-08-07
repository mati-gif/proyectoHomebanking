package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Cuenta;

import java.time.LocalDate;

public class CuentaDto {

    private Long id;

    private String number;
    private LocalDate creationDate;
    private double balance;


    public CuentaDto(Cuenta cuenta) {
        this.id = cuenta.getId();
        this.number = cuenta.getNumber();
        this.creationDate = cuenta.getCreationDate();
        this.balance = cuenta.getBalance();
    }


    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }
}
