package com.mindhub.homebanking.models;


import jakarta.persistence.*;
//import org.hibernate.annotations.GenericGenerator;

//import javax.persistence.Entity;
//import javax.persistence.Id;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
//    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String number;
    private LocalDate creationDate;
    private double balance;




    @ManyToOne(fetch = FetchType.EAGER) // significa que cuando se carga una instancia de la entidad actual, la instancia asociada de Cliente también se carga inmediatamente. Esto puede ser útil si siempre necesitas acceder a los datos de Cliente junto con la entidad principal.
    //FetchType determina cuándo y cómo se cargan las entidades relacionadas desde la base de datos.
    @JoinColumn(name = "cliente_id") // Esta seria la clave foreana en la tabla Cuenta.
    private Cliente cliente;



    @OneToMany(mappedBy = "cuenta", fetch = FetchType.EAGER)
     Set<Transaccion> transacciones = new HashSet<>();



    public Cuenta(String number, LocalDate creationDate, double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
    }


    public Cuenta() {
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }



    public Set<Transaccion> getTransacciones() {
        return transacciones;
    }


    public void addTransacciones(Transaccion transaccion){
        transaccion.setCuenta(this);
        transacciones.add(transaccion);
    }


}
