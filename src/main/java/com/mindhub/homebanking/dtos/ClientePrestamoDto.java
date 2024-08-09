package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.ClientePrestamo;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

import java.util.ArrayList;
import java.util.List;

public class ClientePrestamoDto {

    private Long id;
    private Long prestamoId;
    private String firstName;
    private double amount;
    private Integer payment;


    public ClientePrestamoDto(ClientePrestamo clientePrestamo){
        this.id = clientePrestamo.getId();
        this.prestamoId = clientePrestamo.getPrestamo().getId();
        this.firstName = clientePrestamo.getPrestamo().getFirstName();
        this.amount = clientePrestamo.getAmount();
        this.payment = clientePrestamo.getPayment();

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(Long prestamoId) {
        this.prestamoId = prestamoId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }
}
