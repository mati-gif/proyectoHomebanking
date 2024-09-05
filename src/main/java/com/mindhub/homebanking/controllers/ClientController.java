package com.mindhub.homebanking.controllers;

//estoy implementando el controlador que servirá como punto de acceso principal a los datos de nuestra aplicación. Mediante este controlador, definiremos las rutas esenciales para acceder y manipular los datos a través de peticiones HTTP.
//¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯

import com.mindhub.homebanking.dtos.ClientDto;
import com.mindhub.homebanking.models.Client; //Estoy importando la clase Cliente desde el paquete models.
import com.mindhub.homebanking.repositories.ClientRepository;//Estoy importando la interfaz ClientRepository desde el paquete repositories. Esta interfaz extiende una de las interfaces de Spring Data JPA (JpaRepository)
import com.mindhub.homebanking.service.ClientService;
import org.springframework.web.bind.annotation.CrossOrigin;
//permitiéndome realizar operaciones CRUD sobre la entidad Client.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:5173") // Configuración CORS para este controlador

@RestController// ClientController es un controlador de spring. indica que la clase actúa como un controlador y que cada método devuelve un objeto serializado en lugar de una vista.//ClientController define varias rutas (endpoints) para interactuar con los datos de los clientes a través de peticiones HTTP:
@RequestMapping("/api/clients")//  especifica la URL base para todas las solicitudes manejadas por este controlador. En este caso, cualquier solicitud que comience con "/api/clients" será manejada por este controlador.// Todas las solicitudes que comiencen con /api serán manejadas por este controlador.
public class ClientController {

//    @Autowired//Estoy inyectando una referencia a la interfaz ClientRepository en esta clase.//va a "cablear" clientRepository para evitar la generacion de nuevos objetos cada vez que ejecutenmos la clase controlador
//    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @GetMapping("/hello")//es una estencion de @RequestMapping. Indica que este método responderá a las solicitudes HTTP GET en la ruta "/hello".
    public String hello() {//
        return "Hello clients!";
    }
    //@GetMapping("/hello") : Esta anotación asigna la ruta "/hello" al método getClients() .
    // Esto significa que cuando recibimos una solicitud GET en la ruta "/hello" (la ruta completa sería http://localhost:8080/api/clients/hello),
    // se invocará este método.

    @GetMapping("/")// en locaHhost: 8080/api/clients/all --> aca en esta ruta me va a devolver todos los clientes.// lo que se esta definiendo aca es la ruta y tambein la respuesta que se va a obtener cuando se hace una peticion a esta ruta.
    public ResponseEntity<List<ClientDto>>  getAllClients() {

        return new ResponseEntity<>(clientService.getAllClientDto(), HttpStatus.OK);
    }

    @GetMapping("/{id}")//@PathVariable definimos que ese parametro recibido es lo que va a variar en la ruta.
    public ResponseEntity<?> getClientById(@PathVariable Long id) {//@PathVariable long id: Vincula el parámetro de ruta {id} al parámetro id del método.
        //Captura el valor del {id} en la URL.

        if(clientService.getClientById(id) == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(clientService.getClientDto(clientService.getClientById(id)), HttpStatus.OK);
    }



    @PatchMapping("/updateNewName")//Este metodo respondera a la solicitud de tipo patch en la ruta /api/clients/nuevoName
    public ResponseEntity<?> updateName(@RequestParam String firstName, @RequestParam Long id) {
        //las anotaciones @RequestParam indican que el parámetro name se obtendrá de la solicitud HTTP PATCH.
        //@RequestParam significa que si o si tengo que pasarle esos datos.Si no se lo paso va a devolver un status 400
            Client client = clientService.getClientById(id);
        if(client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }
        client.setFirstName(firstName);

        // Guarda el cliente actualizado usando el servicio
        ClientDto clientDto = clientService.saveUpdatedClient(client);
        return new ResponseEntity<>(clientDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")//El meotodo se va a disparar cuando se reciba una solicitud DELETE en la ruta "/api/clients/{id}"
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {  // @PathVariable long id: Vincula el parámetro de ruta {id} al parámetro id del método. Captura el valor del {id} en la URL.

        Client client = clientService.getClientById(id);
        if(client == null){
            return new ResponseEntity<>("not found the client with id " + id,HttpStatus.NO_CONTENT);
        }

        if (client.isActive()) {
            client.setActive(false); // Realizamos el borrado lógico marcando como inactivo.
            clientService.saveUpdatedClient(client); // Guardamos el cliente con el nuevo estado.
            return new ResponseEntity<>("The client with id " + id + " was successfully deleted.", HttpStatus.OK);
        }

        return new ResponseEntity<>("The client with id " + id + " is already inactive.", HttpStatus.OK);
    }

    @PutMapping("/modify/{id}")// Que este entre llaves sugnifica que es un parametro dinamico y no estatico porque esta esperando un id que le voy a pasar cuando haga la peticion.
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {
        Client clienteAModificar = clientService.getClientById(id);; //Estoy buscando el objeto que coicnida con el id que voy a pasar por parametro y para eso uso el metodo findById().//Este metodo viene de clienteRepository que a su vez viene de JpaRepository.
        if (clientService.clientExistsById(id)) {   // Verificamos si el cliente existe.
            clienteAModificar.setFirstName(firstName);
            clienteAModificar.setLastName(lastName);
            clienteAModificar.setEmail(email);
            clientService.saveUpdatedClient(clienteAModificar);

            ClientDto clientDto = clientService.getClientDto(clienteAModificar);
            return new ResponseEntity<>(clientDto, HttpStatus.OK);
        }
            return new ResponseEntity<>("Client not found",HttpStatus.NOT_FOUND);
    }



}
