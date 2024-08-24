package com.mindhub.homebanking.models;


import jakarta.persistence.*;
//import org.hibernate.annotations.GenericGenerator;

//import javax.persistence.Entity;
//import javax.persistence.Id;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;
    private LocalDate creationDate;
    private double balance;

    //FetchType determina cuándo y cómo se cargan las entidades relacionadas desde la base de datos.
    @ManyToOne(fetch = FetchType.EAGER) // significa que cuando se carga una instancia de la entidad actual, la instancia asociada de Cliente también se carga inmediatamente. Esto puede ser útil si siempre necesitas acceder a los datos de Cliente junto con la entidad principal.
    @JoinColumn(name = "client_id") // Esta seria la clave foreana en la tabla Cuenta.
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
     Set<Transaction> transactions = new HashSet<>();
//Estoy declarando un Set llamad transacciones que contendra objetos de tipo Transaccion.


    public Account(String number, LocalDate creationDate, double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
    }


    public Account() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransactions(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }


}
