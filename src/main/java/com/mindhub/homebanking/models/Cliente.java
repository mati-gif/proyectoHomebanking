package com.mindhub.homebanking.models;

import com.mindhub.homebanking.dtos.CuentaDto;
import jakarta.persistence.*;
//import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;


@Entity//indica que estoy generaqndo una tabla en la base de datos con el nombre de Cliente donde se alamcenaran los objetos como filas en esa tabla.
//cada columna de la tabla va a representar una de las propiedades de nuestro objeto.
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// Hace que el valor de ese Id sea generado por la base de datos automáticamente para que no se repita. es decir Genera de manera automatica el id para que no se repita.(Se genera en el sistema de persistencia en la base de datos)


    private Long id; //con @Id le estoy diciendo que la propiedad long id es la clave primaria  de este objeto en la base de datos.
    //es decir va a ser el unico dato que no se ve a repetir en esa tabla. Cada instancia (objeto) de la clase Cliente va a tener un id unico.
    private String name;
    private String lastName;
    private String email;




    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)//mappedBy = “cliente”: Esto especifica que la relación es bidireccional y que la entidad Cuenta tiene una propiedad llamada cliente que es la dueña de la relación.
            //significa que las entidades Cuenta relacionadas se cargarán inmediatamente junto con la entidad actual. Cuando consultas la entidad actual, todas las instancias de Cuenta asociadas se recuperan en la misma consulta.
    //FetchType.EAGER, estás diciendo que quieres que la entidad con la que esta relacionada la clase Cliente  se cargue inmediatamente junto con la entidad principal. Es como si estuvieras pidiendo que, al obtener la entidad principal de la base de datos, también se obtengan automáticamente todas las entidades relacionadas en la misma operación.



    Set<Cuenta> cuentas = new HashSet<>(); //new HashSet<>();: Inicializa la variable cuentas como una nueva instancia de HashSet, que es una implementación de Set.

    //cuentas: Es el nombre de la variable de instancia que representa la colección de Cuenta.



    public Cliente(String name, String lastName, String email) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Cuenta> getCuentas() {
        return cuentas;
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






    //esto es para que el objeto cliente se pueda mostrar en la consola.
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }




    //Usar Long en lugar de long para el campo id permite que el campo tenga un valor nulo antes de ser asignado por la base de datos y facilita el manejo del campo a través de frameworks como JPA y Hibernate.
    //basicamente porque es útil poder tener un valor nulo para el id antes de que se genere un valor único automáticamente por la base de datos. Esto indica que el id aún no ha sido asignado.
}
