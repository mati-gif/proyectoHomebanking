package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Cuenta;
import com.mindhub.homebanking.models.Transaccion;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class CuentaDto {

    private Long id;

    private String number;
    private LocalDate creationDate;
    private double balance;
    private Set<TransaccionDto> transacciones;


    public CuentaDto(Cuenta cuenta) {
        this.id = cuenta.getId();
        this.number = cuenta.getNumber();
        this.creationDate = cuenta.getCreationDate();
        this.balance = cuenta.getBalance();
        this.transacciones = converTransaccionesToDto(cuenta.getTransacciones());
    }

    private Set<TransaccionDto> converTransaccionesToDto(Set<Transaccion> transacciones) {
        return transacciones.stream()
                .map(TransaccionDto::new)
                .collect(Collectors.toSet());
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

    public Set<TransaccionDto> getTransacciones() {
        return transacciones;
    }
}
