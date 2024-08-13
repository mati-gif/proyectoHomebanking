package com.mindhub.homebanking.models;




import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private String description;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)//se utiliza para especificar cómo se debe almacenar una enumeración en la base de datos.Indica que el valor de los enumo se va a almacenar como un String en la base d e datos.
    private TransactionType type;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;


    public Transaccion(double amount, String description, LocalDateTime date, TransactionType type) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
    }


    public Transaccion() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }




}
