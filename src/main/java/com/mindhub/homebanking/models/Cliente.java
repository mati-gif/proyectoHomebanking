package com.mindhub.homebanking.models;

import com.mindhub.homebanking.dtos.CuentaDto;
import jakarta.persistence.*;
//import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity//indica que estoy generaqndo una tabla en la base de datos con el nombre de Cliente donde se alamcenaran los objetos como filas en esa tabla.
//cada columna de la tabla va a representar una de las propiedades de nuestro objeto.
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// Hace que el valor de ese Id sea generado por la base de datos automáticamente para que no se repita. es decir Genera de manera automatica el id para que no se repita.(Se genera en el sistema de persistencia en la base de datos)


    private Long id; //con @Id le estoy diciendo que la propiedad long id es la clave primaria  de este objeto en la base de datos.
    //es decir va a ser el unico dato que no se ve a repetir en esa tabla. Cada instancia (objeto) de la clase Cliente va a tener un id unico.
    private String firstName;
    private String lastName;
    private String email;
    private boolean active = true;




    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)//mappedBy = “cliente”: Esto especifica que la relación es bidireccional y que la entidad Cuenta tiene una propiedad llamada cliente que es la dueña de la relación.
            //significa que las entidades Cuenta relacionadas se cargarán inmediatamente junto con la entidad actual. Cuando consultas la entidad actual, todas las instancias de Cuenta asociadas se recuperan en la misma consulta.
    //FetchType.EAGER, estoy diciendo que quiero que la entidad con la que esta relacionada la clase Cliente  se cargue inmediatamente junto con la entidad principal. Es como si estuvieras pidiendo que, al obtener la entidad principal de la base de datos, también se obtengan automáticamente todas las entidades relacionadas en la misma operación.



    Set<Cuenta> cuentas = new HashSet<>(); //new HashSet<>();: Inicializa la variable cuentas como una nueva instancia de HashSet, que es una implementación de Set.

    //cuentas: Es el nombre de la variable de instancia que representa la colección de Cuenta.




    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    Set<ClientePrestamo> clientePrestamos = new HashSet<>();



    public Cliente(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }



    public Cliente() {//JPA y Hibernate requieren un constructor sin argumentos para crear instancias de la entidad a través de la reflexión.Basicamente para generar un espacio en memoria.
        // Esto es necesario para que el framework pueda crear instancias de la entidad sin conocer los detalles de sus constructores.


        //la reflexión se refiere a la capacidad del lenguaje para inspeccionar y manipular las clases, interfaces, métodos y campos en tiempo de ejecución. Esto significa que se puede, por ejemplo, obtener información sobre una clase, crear instancias de objetos, llamar a métodos y acceder a campos, todo sin conocer de antemano sus nombres o tipos exactos en tiempo de compilación.
        //Si no se proporciona un constructor vacío, JPA/Hibernate no podrán instanciar la entidad correctamente.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public Set<Cuenta> getCuentas() {
        return cuentas;
    }

    public Set<ClientePrestamo> getClientePrestamos() {
        return clientePrestamos;
    }

    public void setClientePrestamos(Set<ClientePrestamo> clientePrestamos) {
        this.clientePrestamos = clientePrestamos;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addCuentas(Cuenta cuenta){

        cuenta.setCliente(this);
        cuentas.add(cuenta);
    }



    //cuenta: Este es el parámetro que se pasó al método, que es una instancia de la clase Cuenta.
    //setCliente(this): Aquí se está llamando al método setCliente del objeto cuenta.
    // El argumento this se refiere a la instancia actual de Cliente. Esto establece la relación bidireccional, asegurando que la cuenta sepa a qué cliente pertenece.

    //cuentas: Este es el conjunto (Set<Cuenta>) que pertenece a la instancia actual de Cliente.
    //add(cuenta): Este método agrega la instancia de Cuenta al conjunto cuentas.





    public void addClientePrestamo(ClientePrestamo clientePrestamo){
        clientePrestamo.setCliente(this);

        clientePrestamos.add(clientePrestamo);


    }
    //clientePrestamo: Este es el parámetro que se pasó al método, que es una instancia de la clase ClientePrestamo.
    //setCliente(this): Aquí se está llamando al método setCliente del objeto clientePrestamo.
    // El argumento this se refiere a la instancia del objeto actual de  la clase  Cliente. Esto establece la relación bidireccional, asegurando que clientePrestamo sepa a qué cliente pertenece.

    //clientePrestamos: Este es el conjunto (Set<ClientePrestamo>) que pertenece a la instancia actual de Cliente.
    //add(clientePrestamo): Este método agrega la instancia de ClientePrestamo al conjunto clientePrestamos.





    public List<Prestamo> getPrestamos(){
        return clientePrestamos.stream().map(c -> c.getPrestamo()).collect(Collectors.toList());
    }

//Este metodo devuelve la lista de préstamos de un cliente.
//List<Prestamo> ==> significa que va a retornar una lista de objetos de tipo Prestamo.

//clientePrestamos  ==> es el conjunto (Set<ClientePrestamo>) que pertenece a la instancia actual de Cliente.

//clientePrestamos.stream() ===> stream() es un metodo que convierte la lista clientePrestamos en un flujo de datos "Stream".
// Esto permite realizar operaciones funcionales (como mapear, filtrar, etc.) sobre los elementos de la lista de manera secuencial o paralela.

//map(): Es una operación intermedia en los "streams" que transforma cada elemento del flujo de datos.
// El método map() se utiliza para mapear cada elemento del flujo de datos a un nuevo elemento en el flujo de datos resultante.

//(c -> c.getCliente()): Es una expresión lambda. Para cada elemento "c" del flujo clientePrestamos, la expresión lambda aplica el método getPrestamo() sobre "c".

//"c": Representa cada elemento de clientePrestamos.
//c.getPrestamo(): Extrae el objeto Prestamo de cada elemento "c". Ademas "c" es de la clase  ClientePrestamo, que tiene el método getPrestamo(), y este método retorna un objeto de tipo Cliente.

//Como resultado, map() genera un nuevo flujo de datos donde cada elemento es un objeto Cliente.

//.collect(Collectors.toList()):

//collect(): Es una operación terminal en los streams que recopila los elementos transformados en una estructura de datos concreta, en este caso, una lista.
 //Collectors.toList(): Indica que los elementos del flujo deben ser recolectados y almacenados en una lista.

    //Resultado Final:
    //Se retorna una lista (List<Prestamo>) que contiene todos los objetos Prestamo extraídos de cada elemento de la lista clientePrestamos.


    //Resumen:
    //Este método recorre una lista (clientePrestamos), extrae un objeto Prestamo de cada elemento en esa lista usando getPrestamo(), y luego almacena todos estos objetos Prestamo en una nueva lista que es devuelta como resultado.











    //esto es para que el objeto cliente se pueda mostrar en la consola.
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }




    //Usar Long en lugar de long para el campo id permite que el campo tenga un valor nulo antes de ser asignado por la base de datos y facilita el manejo del campo a través de frameworks como JPA y Hibernate.
    //basicamente porque es útil poder tener un valor nulo para el id antes de que se genere un valor único automáticamente por la base de datos. Esto indica que el id aún no ha sido asignado.
}
