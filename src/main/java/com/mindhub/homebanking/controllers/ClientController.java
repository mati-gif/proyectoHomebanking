package com.mindhub.homebanking.controllers;

//estoy implementando el controlador que servirá como punto de acceso principal a los datos de nuestra aplicación. Mediante este controlador, definiremos las rutas esenciales para acceder y manipular los datos a través de peticiones HTTP.
//¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯

import com.mindhub.homebanking.models.Cliente;
import com.mindhub.homebanking.repositories.ClientRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController// ClientController es un controlador de spring. indica que la clase actúa como un controlador y que cada método devuelve un objeto serializado en lugar de una vista.
//ClientController define varias rutas (endpoints) para interactuar con los datos de los clientes a través de peticiones HTTP:


@RequestMapping("/api/clients")//  especifica la URL base para todas las solicitudes manejadas por este controlador. En este caso, cualquier solicitud que comience con "/api/clients" será manejada por este controlador.
// Todas las solicitudes que comiencen con /api serán manejadas por este controlador.
public class ClientController {


    @Autowired//Estoy inicializando el repositorio de clientes. Inyecta una instancia de ClientRepository en esta clase. Spring se encarga de inicializar esta dependencia automáticamente.
    private ClientRepository clientRepository;


    @GetMapping("/hello")//es una estencion de @RequestMapping. Indica que este método responderá a las solicitudes HTTP GET en la ruta "/hello".
    public String hello(){
        return "Hello clients!";
    }
    //@GetMapping("/hello") : Esta anotación asigna la ruta "/hello" al método getClients() .
    // Esto significa que cuando recibimos una solicitud GET en la ruta "/hello" (la ruta completa sería http://localhost:8080/api/clients/hello),
    // se invocará este método.



    @GetMapping("/")// en locaHhost: 8080/api/clients/ --> aca en esta ruta me va a devolver todos los clientes.// lo que se esta definiendo aca es la ruta y tambein la respuesta que se va a obtener cuando se hace una peticion a esta ruta.
    public List<Cliente> getAllClients(){
        return clientRepository.findAll();

    }


    @GetMapping("/{id}")
    public Cliente getClientById(@PathVariable long id){//@PathVariable long id: Vincula el parámetro de ruta {id} al parámetro id del método.
        return clientRepository.findById(id).orElse(null);
    }

    @GetMapping("/recuento")
    public long  getCount(){
        return clientRepository.count();
    }

@PatchMapping ("/nuevoName")
    public String updateName(@RequestParam String name, @RequestParam long id){
        Cliente idd = clientRepository.findById(id).orElse(null);
        idd.setName(name);
        clientRepository.save(idd);
        return "nombre cambiado";
    }

    @PostMapping("/new")// El meotodo se va a disparar cuando se reciba una solicitud POST en la ruta "/api/clients/new".
    public Cliente createClient(@RequestParam String name, @RequestParam String lastName, @RequestParam String email){
//        clientRepository.save(new Cliente(name, lastName, email));
        Cliente cliente = new Cliente();
        cliente.setName(name);
        cliente.setLastName(lastName);
        cliente.setEmail(email);
        return clientRepository.save(cliente);
    }


    @DeleteMapping("/{id}")
    public String deleteClient(@PathVariable long id){
        clientRepository.deleteById(id);
        return "El cliente fue boorado con id: " + id;
    }


}
