package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double maxAmount;

    @ElementCollection // se utiliza para crear listas de  objetos integrables.Un objeto integrable son datos destinados a usarse únicamente en el objeto que lo contiene.
    //Esto se hace asi porque trabajamos con datos simples y Spring se encarga de crear esta entidad y configurar la relación uno a muchos automáticamente.
    @Column(name = "payments")
    private List<Integer> payments = new ArrayList<>();



    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();


    public Loan(String name, double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;

    }



    public Loan() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setLoan(this);


        clientLoans.add(clientLoan);

    }

    //clientePrestamo: Este es el parámetro que se pasó al método, que es una instancia de la clase ClientePrestamo.
    //setPrestamo(this): Aquí se está llamando al método setPrestamo del objeto clientePrestamo.
    // El argumento this se refiere a la instancia del objeto actual de  la clase  Prestamo. Esto establece la relación bidireccional, asegurando que clientePrestamo sepa a qué prestamo pertenece.

    //clientePrestamos: Este es el conjunto (Set<ClientePrestamo>) que pertenece a la instancia actual de Prestamo.
    //add(clientePrestamo): Este método agrega la instancia de ClientePrestamo al conjunto clientePrestamos.





    public List<Client> getClients(){
        return clientLoans.stream().map(c -> c.getClient()).collect( Collectors.toList());
    }


    //Este metodo devuelve la lista de clientes de un préstamo.

//List<Prestamo> ==> significa que va a retornar una lista de objetos de tipo Cliente.

//clientePrestamos  ==> es el conjunto (Set<ClientePrestamo>) que pertenece a la instancia actual de Cliente.

//clientePrestamos.stream() ===> stream() es un metodo que convierte la lista clientePrestamos en un flujo de datos "Stream".
// Esto permite realizar operaciones funcionales (como mapear, filtrar, etc.) sobre los elementos de la lista de manera secuencial o paralela.

//map(): Es una operación intermedia en los "streams" que transforma cada elemento del flujo de datos.
// El método map() se utiliza para mapear(Recorrer) cada elemento del flujo de datos a un nuevo elemento en el flujo de datos resultante.

//(c -> c.getCliente()): Es una expresión lambda. Para cada elemento "c" del flujo clientePrestamos, la expresión lambda aplica el método getCliente() sobre "c".

//"c": Representa cada elemento de clientePrestamos.
//c.getCliente(): Extrae el objeto Cliente de cada elemento "c". Ademas "c" es de la clase  ClientePrestamo, que tiene el método getCliente(), y este método retorna un objeto de tipo Cliente.

//Como resultado, map() genera un nuevo flujo de datos donde cada elemento es un objeto Cliente.

//.collect(Collectors.toList()):

//collect(): Es una operación terminal en los streams que recopila los elementos transformados en una estructura de datos concreta, en este caso, una lista.
    //Collectors.toList(): Indica que los elementos del flujo deben ser recolectados y almacenados en una lista.

    //Resultado Final:
    //Se retorna una lista (List<Cliente>) que contiene todos los objetos Cliente extraídos de cada elemento de la lista clientePrestamos.


    //Resumen:
    //Este método recorre una lista (clientePrestamos), extrae un objeto Cliente de cada elemento en esa lista usando getCliente(), y luego almacena todos estos objetos Cliente en una nueva lista que es devuelta como resultado.

}



