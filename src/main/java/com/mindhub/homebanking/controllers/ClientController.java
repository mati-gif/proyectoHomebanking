package com.mindhub.homebanking.controllers;

//estoy implementando el controlador que servirá como punto de acceso principal a los datos de nuestra aplicación. Mediante este controlador, definiremos las rutas esenciales para acceder y manipular los datos a través de peticiones HTTP.
//¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯

import com.mindhub.homebanking.dtos.ClienteDto;
import com.mindhub.homebanking.models.Cliente; //Estoy importando la clase Cliente desde el paquete models.
import com.mindhub.homebanking.repositories.ClientRepository;//Estoy importando la interfaz ClientRepository desde el paquete repositories. Esta interfaz extiende una de las interfaces de Spring Data JPA (JpaRepository)
//permitiéndome realizar operaciones CRUD sobre la entidad Cliente.


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController// ClientController es un controlador de spring. indica que la clase actúa como un controlador y que cada método devuelve un objeto serializado en lugar de una vista.
//ClientController define varias rutas (endpoints) para interactuar con los datos de los clientes a través de peticiones HTTP:


@RequestMapping("/api/clients")//  especifica la URL base para todas las solicitudes manejadas por este controlador. En este caso, cualquier solicitud que comience con "/api/clients" será manejada por este controlador.
// Todas las solicitudes que comiencen con /api serán manejadas por este controlador.
public class ClientController {


    @Autowired
//Estoy inicializando el repositorio de clientes. Inyecta una instancia de ClientRepository en esta clase. Spring se encarga de inicializar esta dependencia automáticamente.
    //se usa para inyectar automáticamente dependencias en las clases, como repositorios o servicios.
    //Estoy inyectando una referencia a la interfaz ClientRepository en esta clase.
    //va a "cablear" clientRepository para evitar la generacion de nuevos objetos cada vez que ejecutenmos la clase controlador
    private ClientRepository clientRepository;


    @GetMapping("/hello")
//es una estencion de @RequestMapping. Indica que este método responderá a las solicitudes HTTP GET en la ruta "/hello".
    public String hello() {//
        return "Hello clients!";
    }
    //@GetMapping("/hello") : Esta anotación asigna la ruta "/hello" al método getClients() .
    // Esto significa que cuando recibimos una solicitud GET en la ruta "/hello" (la ruta completa sería http://localhost:8080/api/clients/hello),
    // se invocará este método.


    @GetMapping("/all")
// en locaHhost: 8080/api/clients/all --> aca en esta ruta me va a devolver todos los clientes.// lo que se esta definiendo aca es la ruta y tambein la respuesta que se va a obtener cuando se hace una peticion a esta ruta.
    public ResponseEntity<List<ClienteDto>>  getAllClients() {

        List<Cliente> clientes = clientRepository.findAll();//devuelve una lista de todos los clientes

        List<ClienteDto> clientesDto = clientes.stream()
                .map(cliente -> new ClienteDto(cliente))
                .collect(Collectors.toList());

        return new ResponseEntity<>(clientesDto, HttpStatus.OK);

    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {//@PathVariable long id: Vincula el parámetro de ruta {id} al parámetro id del método.
        //Captura el valor del {id} en la URL.
            Cliente cliente = clientRepository.findById(id).orElse(null);
            if(cliente == null) {

                return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
            }

            ClienteDto clienteDto = new ClienteDto(cliente);
            return new ResponseEntity<>(clienteDto, HttpStatus.OK);


    }

    @GetMapping("/recuento")
    public long getCount() {
        return clientRepository.count();
    }




    @PatchMapping("/nuevoName")//Este metodo respondera a la solicitud de tipo patch en la ruta /api/clients/nuevoName
    public ResponseEntity<?> updateName(@RequestParam String name, @RequestParam Long id) {
        //las anotaciones @RequestParam indican que el parámetro name se obtendrá de la solicitud HTTP PATCH.
//@RequestParam significa que si o si tengo que pasarle esos datos.Si no se lo paso va a devolver un status 400

        Cliente cliente = clientRepository.findById(id).orElse(null);    //Busca en el repositorio clientRepository un cliente con el id especificado. Si no se encuentra un cliente con ese id, retorna null.

        if(cliente == null) {


            return new ResponseEntity<>("No se encontro el cliente", HttpStatus.NOT_FOUND);

        }

        cliente.setName(name);
        clientRepository.save(cliente);

        ClienteDto clienteDto = new ClienteDto(cliente);

        return new ResponseEntity<>(clienteDto, HttpStatus.OK);



    }






    @PostMapping("/new")
// El meotodo se va a disparar cuando se reciba una solicitud POST en la ruta "/api/clients/new".
    public ResponseEntity<?> createClient(@RequestParam String name, @RequestParam String lastName, @RequestParam String email) {//las anotaciones @RequestParam indican que los parámetros name, lastName y email se obtendrán de la solicitud HTTP POST.
        // Específicamente, @RequestParam se usa para extraer los valores de los parámetros de la solicitud y asignarlos a las variables correspondientes en el método.


//        clientRepository.save(new Cliente(name, lastName, email));


        Cliente cliente = new Cliente();
        cliente.setName(name);
        cliente.setLastName(lastName);
        cliente.setEmail(email);
        clientRepository.save(cliente);

        ClienteDto clienteDto = new ClienteDto(cliente);

        return new ResponseEntity<>(clienteDto, HttpStatus.CREATED);
    }



    @PostMapping("/newCliente")
    public ResponseEntity<Cliente>crearCliente(@RequestBody Cliente cliente){
        Cliente savedClient = clientRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
    }




    @DeleteMapping("/{id}")
//El meotodo se va a disparar cuando se reciba una solicitud DELETE en la ruta "/api/clients/{id}"
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {  // @PathVariable long id: Vincula el parámetro de ruta {id} al parámetro id del método. Captura el valor del {id} en la URL.

        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return new ResponseEntity<>(" ya no esta el cliente con el id: " + id,HttpStatus.OK);
        }
            return new ResponseEntity<>("no se encontro el cliente con el id: " + id,HttpStatus.NO_CONTENT);


    }


    @PutMapping("/modificar/{id}")
// Que este entre llaves sugnifica que es un parametro dinamico y no estatico porque esta esperando un id que le voy a pasar cuando haga la peticion.
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestParam String name, @RequestParam String lastName, @RequestParam String email) {
        Cliente clienteAModificar = clientRepository.findById(id).orElse(null); //Estoy buscando el objeto que coicnida con el id que voy a pasar por parametro y para eso uso el metodo findById().
        //Este metodo viene de clienteRepository que a su vez viene de JpaRepository.

        if (clientRepository.existsById(id)) {   // Verificamos si el cliente existe.
            clienteAModificar.setName(name);
            clienteAModificar.setLastName(lastName);
            clienteAModificar.setEmail(email);
            clientRepository.save(clienteAModificar);
            ClienteDto clienteDto = new ClienteDto(clienteAModificar);
            return new ResponseEntity<>(clienteDto, HttpStatus.OK);

        }
            return new ResponseEntity<>("cliente no encontrado",HttpStatus.NOT_FOUND);




    }











    @PatchMapping("/modificarCliente")
    // Este método responderá a la solicitud de tipo PATCH en la ruta /api/clients/modificarCliente.
    public String updateCliente(
            @RequestParam long id,
            @RequestParam(required = false) String name,  //Si se proporciona en la solicitud, se convertirá en un tipo String. Si no se proporciona, su valor será null.
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String lastName) {

        // Busca en el repositorio clientRepository un cliente con el id especificado. Si no se encuentra, retorna null.
        Cliente cliente = clientRepository.findById(id).orElse(null);

        // Si el cliente no se encuentra, retorna un mensaje de error.
        if (cliente == null) {
            return "Cliente no encontrado con id " + id; // Mensaje de error simple
        }

        // Actualiza los atributos solo si se han proporcionado.
        if (name != null) {
            cliente.setName(name);
        }
        if (email != null) {
            cliente.setEmail(email);
        }
        if (lastName != null) {
            cliente.setLastName(lastName);
        }

        // Guarda los cambios en el repositorio.
        clientRepository.save(cliente);

        return "Atributos del cliente actualizados"; // Mensaje de éxito simple
    }



}
