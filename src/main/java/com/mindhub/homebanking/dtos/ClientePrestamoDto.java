package com.mindhub.homebanking.dtos;


import com.mindhub.homebanking.models.ClientePrestamo;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

import java.util.ArrayList;
import java.util.List;

public class ClientePrestamoDto {

    private Long id;
    private Long prestamoId;
    private String name;
    private double amount;

    @ElementCollection
    @Column(name = "payments")
    private List<Integer> payments = new ArrayList<>();


    public ClientePrestamoDto(ClientePrestamo clientePrestamo){
        this.id = clientePrestamo.getId();
        this.prestamoId = clientePrestamo.getPrestamo().getId();
        this.name = clientePrestamo.getPrestamo().getName();
        this.amount = clientePrestamo.getAmount();
        this.payments = clientePrestamo.getPayments();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
}
