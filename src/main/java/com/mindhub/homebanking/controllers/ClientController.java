package com.mindhub.homebanking.controllers;

//estoy implementando el controlador que servirá como punto de acceso principal a los datos de nuestra aplicación. Mediante este controlador, definiremos las rutas esenciales para acceder y manipular los datos a través de peticiones HTTP.
//¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯

import com.mindhub.homebanking.dtos.ClientDto;
import com.mindhub.homebanking.models.Client; //Estoy importando la clase Cliente desde el paquete models.
import com.mindhub.homebanking.repositories.ClientRepository;//Estoy importando la interfaz ClientRepository desde el paquete repositories. Esta interfaz extiende una de las interfaces de Spring Data JPA (JpaRepository)
import org.springframework.web.bind.annotation.CrossOrigin;
//permitiéndome realizar operaciones CRUD sobre la entidad Client.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5174") // Configuración CORS para este controlador
@RestController// ClientController es un controlador de spring. indica que la clase actúa como un controlador y que cada método devuelve un objeto serializado en lugar de una vista.
//ClientController define varias rutas (endpoints) para interactuar con los datos de los clientes a través de peticiones HTTP:


@RequestMapping("/api/clients")//  especifica la URL base para todas las solicitudes manejadas por este controlador. En este caso, cualquier solicitud que comience con "/api/clients" será manejada por este controlador.
// Todas las solicitudes que comiencen con /api serán manejadas por este controlador.
public class ClientController {


    @Autowired
//Estoy inicializando el repositorio de client. Inyecta una instancia de ClientRepository en esta clase. Spring se encarga de inicializar esta dependencia automáticamente.
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
    public ResponseEntity<List<ClientDto>>  getAllClients() {

        List<Client> clients = clientRepository.findAll();//devuelve una lista de todos los clientes.
//Estoy declarando una lista llamada clientes que contendra objetos de tipo Cliente.

        List<ClientDto> clientsDto = clients.stream() //Declara una lista llamada clientesDto que contendrá objetos de tipo ClienteDto.
                // clientes.stream(): Convierte la colección clientes en un flujo (stream). Un flujo es una secuencia de elementos que se pueden procesar de manera funcional.
                .filter(client -> client.isActive() == true) //Filtra los elementos del flujo. En este caso, para cada cliente en el flujo, se filtra si el atributo isActive es true.
                .map(client -> new ClientDto(client)) //Aplica una función a cada elemento del flujo. En este caso, para cada cliente en el flujo, se crea una nueva instancia de ClienteDto usando el constructor ClienteDto(cliente).
                // La función map transforma cada cliente en un ClienteDto.

                .collect(Collectors.toList());   // Recoge los elementos transformados del flujo y los convierte en una lista. Collectors.toList() es un colector que acumula los elementos en una lista.

        //toma una colección de clientes, la convierte en un flujo, transforma cada cliente en un ClienteDto, y finalmente recoge todos los ClienteDto en una lista llamada clientesDto.


        return new ResponseEntity<>(clientsDto, HttpStatus.OK);

    }


    @GetMapping("/{id}")//@PathVariable definimos que ese parametro recibido es lo que va a variar en la ruta.
    public ResponseEntity<?> getClientById(@PathVariable Long id) {//@PathVariable long id: Vincula el parámetro de ruta {id} al parámetro id del método.
        //Captura el valor del {id} en la URL.
            Client client = clientRepository.findById(id).orElse(null);
            //Client client => Estoy declarando un objeto cliente de tipo Cliente que contendra los datos del cliente que coincida con el id especificado.

            if(client == null) {

                return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
            }

            ClientDto clientDto = new ClientDto(client);
            return new ResponseEntity<>(clientDto, HttpStatus.OK);


    }

    @GetMapping("/recuento")
    public long getCount() {
        return clientRepository.count();
    }




    @PatchMapping("/nuevoName")//Este metodo respondera a la solicitud de tipo patch en la ruta /api/clients/nuevoName
    public ResponseEntity<?> updateName(@RequestParam String firstName, @RequestParam Long id) {
        //las anotaciones @RequestParam indican que el parámetro name se obtendrá de la solicitud HTTP PATCH.
//@RequestParam significa que si o si tengo que pasarle esos datos.Si no se lo paso va a devolver un status 400

        Client client = clientRepository.findById(id).orElse(null);    //Busca en el repositorio clientRepository un cliente con el id especificado. Si no se encuentra un cliente con ese id, retorna null.

        if(client == null) {


            return new ResponseEntity<>("No se encontro el cliente", HttpStatus.NOT_FOUND);

        }

        client.setFirstName(firstName);
        clientRepository.save(client);

        ClientDto clientDto = new ClientDto(client);

        return new ResponseEntity<>(clientDto, HttpStatus.OK);



    }






    @PostMapping("/new")
// El meotodo se va a disparar cuando se reciba una solicitud POST en la ruta "/api/clients/new".
    public ResponseEntity<?> createClient(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {//las anotaciones @RequestParam indican que los parámetros name, lastName y email se obtendrán de la solicitud HTTP POST.
        // Específicamente, @RequestParam se usa para extraer los valores de los parámetros de la solicitud y asignarlos a las variables correspondientes en el método.


//        clientRepository.save(new Client(firstName, lastName, email));


        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        clientRepository.save(client);

        ClientDto clientDto = new ClientDto(client);

        return new ResponseEntity<>(clientDto, HttpStatus.CREATED);
    }



//    @PostMapping("/newCliente")
//    public ResponseEntity<Client>crearClient(@RequestBody Client client){
//        Client savedClient = clientRepository.save(client);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
//    }




    @DeleteMapping("/{id}")
//El meotodo se va a disparar cuando se reciba una solicitud DELETE en la ruta "/api/clients/{id}"
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {  // @PathVariable long id: Vincula el parámetro de ruta {id} al parámetro id del método. Captura el valor del {id} en la URL.

        Client client = clientRepository.findById(id).orElse(null);

        if(client == null){
            return new ResponseEntity<>("no se encontro el cliente con el id: " + id,HttpStatus.NO_CONTENT);

        }

        if (client.isActive()) {
            client.setActive(false); // Realizamos el borrado lógico marcando como inactivo.
            clientRepository.save(client);  // Guardamos el cliente con el nuevo estado.
            return new ResponseEntity<>("El cliente con id " + id + " ha sido marcado como inactivo.", HttpStatus.OK);
        }


        return new ResponseEntity<>("El cliente con id " + id + " ya se encuentra inactivo.", HttpStatus.OK);
    }


    @PutMapping("/modificar/{id}")
// Que este entre llaves sugnifica que es un parametro dinamico y no estatico porque esta esperando un id que le voy a pasar cuando haga la peticion.
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {
        Client clienteAModificar = clientRepository.findById(id).orElse(null); //Estoy buscando el objeto que coicnida con el id que voy a pasar por parametro y para eso uso el metodo findById().
        //Este metodo viene de clienteRepository que a su vez viene de JpaRepository.

        if (clientRepository.existsById(id)) {   // Verificamos si el cliente existe.
            clienteAModificar.setFirstName(firstName);
            clienteAModificar.setLastName(lastName);
            clienteAModificar.setEmail(email);
            clientRepository.save(clienteAModificar);
            ClientDto clientDto = new ClientDto(clienteAModificar);
            return new ResponseEntity<>(clientDto, HttpStatus.OK);

        }
            return new ResponseEntity<>("cliente no encontrado",HttpStatus.NOT_FOUND);




    }











    @PatchMapping("/modificarCliente")
    // Este método responderá a la solicitud de tipo PATCH en la ruta /api/clients/modificarCliente.
    public String updateClient(
            @RequestParam long id,
            @RequestParam(required = false) String firstName,  //Si se proporciona en la solicitud, se convertirá en un tipo String. Si no se proporciona, su valor será null.
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String lastName) {

        // Busca en el repositorio clientRepository un cliente con el id especificado. Si no se encuentra, retorna null.
        Client client = clientRepository.findById(id).orElse(null);

        // Si el cliente no se encuentra, retorna un mensaje de error.
        if (client == null) {
            return "Cliente no encontrado con id " + id; // Mensaje de error simple
        }

        // Actualiza los atributos solo si se han proporcionado.
        if (firstName != null) {
            client.setFirstName(firstName);
        }
        if (email != null) {
            client.setEmail(email);
        }
        if (lastName != null) {
            client.setLastName(lastName);
        }

        // Guarda los cambios en el repositorio.
        clientRepository.save(client);

        return "Atributos del cliente actualizados"; // Mensaje de éxito simple
    }



}
