package com.mindhub.homebanking.repositories;

//Estoy creando un repositorio y se lo  estoy estableciendo a la clase Cliente.Se crea  para administrar las instancias de esa clase en la base de datos.
//¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository// Estoy indicando que este componente actuará como gestor de acceso a la capa de persistencia de datos, considerándola como una base de datos.//Se escribe @Repository para indicar que cumple con el patrón de diseño Repository, de modo que actúa como punto de acceso a los datos en una aplicación.
public interface ClientRepository extends JpaRepository<Client, Long> {//me dice que JpaRepository va a usar la clase Cliente y va a trabajar con objetos de tipo Cliente
//Usando ClientRepository voy a poder acceder a todos los metodos de JpaRepository.

Client findByEmail(String email); //tiene que coincidir la palabra clave "Email" con la propiedad email que tien la entidad Cliente .

//Cliente es la entidad con la que está asociado el repositorio y Long es el tipo de datos del identificador principal de la entidad.
}


//La interfaz ClientRepository extiende la interfaz JpaRepository .
// JpaRepository es parte de Spring Data JPA y proporciona métodos para Crear, Leer, Actualizar, Eliminar y otras operaciones comunes en una entidad.