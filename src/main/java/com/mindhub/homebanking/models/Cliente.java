package com.mindhub.homebanking.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;      //todas estas anotaciones JPA  se hacen para conectar la clase a una tabla de base de datos.
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;


@Entity//indica que estoy generaqndo una tabla en la base de datos con el nombre de Cliente donde se alamcenaran los objetos como filas en esa tabla.
//cada columna de la tabla va a representar una de las propiedades de nuestro objeto.
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// Hace que el valor de ese Id sea generado por la base de datos automáticamente para que no se repita. es decir Genera de manera automatica el id para que no se repita.(Se genera en el sistema de persistencia en la base de datos)
    private long id; //con @Id le estoy diciendo que la propiedad long id es la clave primaria  de este objeto en la base de datos.
    //es decir va a ser el unico dato que no se ve a repetir en esa tabla. Cada instancia (objeto) de la clase Cliente va a tener un id unico.
    private String name;
    private String lastName;
    private String email;



    public Cliente(String name, String lastName, String email) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }



    public Cliente() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
