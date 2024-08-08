package com.mindhub.homebanking.models;


import jakarta.persistence.*;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;


@Entity
public class ClientePrestamo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private double amount;


    @ElementCollection // se utiliza para crear listas de  objetos integrables.Un objeto integrable son datos destinados a usarse únicamente en el objeto que lo contiene.
    //Esto se hace asi porque trabajamos con datos simples y Spring se encarga de crear esta entidad y configurar la relación uno a muchos automáticamente.
    @Column(name = "payments")
    private List<Integer> payments = new ArrayList<>();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="cliente_id")
    private Cliente cliente;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="prestamo_id")
    private Prestamo prestamo;


    public ClientePrestamo(Cliente cliente, Prestamo prestamo, double amount, List<Integer> payments) {
        this.cliente = cliente;
        this.prestamo = prestamo;
        this.amount = amount;
        this.payments = payments;
    }



    public ClientePrestamo(){

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }
}
