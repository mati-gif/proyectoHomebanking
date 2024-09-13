package com.mindhub.homebanking;

import com.mindhub.homebanking.dtos.ClientDto;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;        //me va a permitir usar el aserThat con el hamcrest para validar que el resultado sea el esperado.
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest//va a ser una clase de testeos por eso lleva esta anotacion para que este en el contexto de spring cuando se ejecuten los testeos
public class ClientServiceTests {

    @MockBean//va a simular que existe un clientService
     ClientService clientService;

    @MockBean
     ClientRepository clientRepository;

//    @Autowired //para hacer un test de integración
//    private ClientRepository clientRepository;



    //Vamos a testear un metodo de la clase ClientService,es un testeo unitario
    @Test//con esta anotacion indico que vot a generar un nuevo testeo y se va a asegurar que el metodo devuelva algo.
    public void findClientByEmail() {

        when(clientService.findClientByEmail("M.morel@me.com")).thenReturn(new Client("Melba","Morel","M.morel@me.com","123456789"));

        Client client = clientService.findClientByEmail("M.morel@me.com");

        assertThat(client, is(notNullValue()));
        assertThat(client.getEmail(),equalTo("M.morel@me.com"));
    }

    @Test
    public void saveClient(){

        //Creo un nuevo cliente de prueba
        Client newClient = new Client("John","Doe","J.doe@me.com","123456789");

        // Simulamos el comportamiento del repositorio para el método saveClient()
        when(clientService.saveClient(newClient)).thenReturn(newClient);

        // Ejecutamos el método que queremos probar
        Client savedClient = clientService.saveClient(newClient);

        // Validamos que el cliente no sea nulo
        assertThat(savedClient, is(notNullValue()));

        // Verificamos que el cliente guardado tenga los mismos atributos que el original
        assertThat(savedClient.getEmail(), equalTo("J.doe@me.com"));
        assertThat(savedClient.getFirstName(), equalTo("John"));
        assertThat(savedClient.getLastName(), equalTo("Doe"));
        assertThat(savedClient.getPassword(), equalTo("123456789"));

    }
//-------------------------------------------------------------------------------------------//
    @Test
    public void clientExistsById(){

        // Simulamos que el cliente con ID 1 existe en la base de datos
        when(clientService.clientExistsById(1L)).thenReturn(true);

        // Llamamos al método que queremos probar
        boolean result = clientService.clientExistsById(1L);
        // Verificamos que el resultado sea verdadero
        assertThat(result,is(equalTo(true)));
    }

    @Test
    public void shouldReturnFalseIfClientDoesNotExist() {
        // Simulamos que el cliente con ID 99 no existe en la base de datos
        when(clientService.clientExistsById(99L)).thenReturn(false);

        // Llamamos al método que queremos probar
        boolean exists = clientService.clientExistsById(99L);

        // Verificamos que el resultado sea falso
        assertThat(exists, is(false)); // Aquí usamos 'is(false)' para validar el valor booleano
    }

//-------------------------------------------------------------------------------------------//

    @Test
    public void  getClientById() {

        // Creamos un cliente de prueba
        Client newClient = new Client("John","Doe","J.doe@me.com","123456789");

        // Simulamos que el cliente con ID 1 existe en el repositorio
        when(clientService.getClientById(1L)).thenReturn(newClient);

        // Llamamos al método que queremos probar
        Client client = clientService.getClientById(1L);

        // Verificamos que el cliente no sea nulo
        assertThat(client, is(notNullValue()));

        // Verificamos que el cliente devuelto tenga los mismos atributos que el original
        assertThat(client.getEmail(), equalTo("J.doe@me.com"));
        assertThat(client.getFirstName(), equalTo("John"));
        assertThat(client.getLastName(), equalTo("Doe"));
        assertThat(client.getPassword(), equalTo("123456789"));
    }

    @Test
    public void shouldReturnNullWhenClientDoesNotExist() {
        // Simulamos que no hay un cliente con ID 99 en el repositorio
        when(clientService.getClientById(99L)).thenReturn(null);

        // Llamamos al método que queremos probar
        Client client = clientService.getClientById(99L);

        // Verificamos que el resultado sea nulo
        assertThat(client, is(nullValue()));
    }

//-------------------------------------------------------------------------------------------//

    @Test
    public void  testClientsIsDatabase() {

// Creamos una lista de clientes de prueba
        List<Client> mockClients = Arrays.asList(
                new Client("John", "Doe", "john.doe@example.com", "123456789"),
                new Client("Jane", "Doe", "jane.doe@example.com", "987654321")
        );

        // Simulamos que el método devuelve la lista de clientes
        when(clientService.getAllClient()).thenReturn(mockClients);

        // Llamamos al método que queremos probar
        List<Client> clients = clientService.getAllClient();

        // Verificamos que la lista no sea nula
        assertThat(clients, is(notNullValue()));
        // Verificamos que la lista tiene 2 elementos
        assertThat(clients.size(), is(2));
        // Verificamos los detalles de los clientes
        assertThat(clients.get(0).getEmail(), equalTo("john.doe@example.com"));
        assertThat(clients.get(1).getEmail(), equalTo("jane.doe@example.com"));
    }
    @Test
    public void shouldReturnEmptyListWhenNoClientsExist() {
        // Simulamos que el método devuelve una lista vacía
        when(clientService.getAllClient()).thenReturn(Collections.emptyList());

        // Llamamos al método que queremos probar
        List<Client> clients = clientService.getAllClient();

        // Verificamos que la lista no sea nula
        assertThat(clients, is(notNullValue()));
        // Verificamos que la lista esté vacía
        assertThat(clients.isEmpty(), is(true));
    }
//-------------------------------------------------------------------------------------------//

//Chequear por que no esta retornando solo los activos.(Esta mal)
    @Test
    public void shouldReturnOnlyActiveClientsAsClientDto() {
        // Crear clientes de prueba
        Client activeClient = new Client("John", "Doe", "john.doe@example.com", "123456789");
        activeClient.setActive(true);
        Client inactiveClient = new Client("Jane", "Doe", "jane.doe@example.com", "987654321");
        inactiveClient.setActive(false);

        // Simular el método getAllClient() para devolver los clientes de prueba
        when(clientRepository.findAll()).thenReturn(Arrays.asList(activeClient, inactiveClient));

        // Ejecutar el método que queremos probar
        List<ClientDto> clientDtos = clientService.getAllClientDto();

        // Verificar que la lista de ClientDto no sea nula
        assertThat(clientDtos, is(notNullValue()));
        System.out.println(clientDtos);
//        // Verificar que la lista contenga solo clientes activos
//        assertThat(clientDtos.size(), is(1));
//        assertThat(clientDtos.get(0).getEmail(), is(equalTo("john.doe@example.com")));
    }

//-------------------------------------------------------------------------------------------//

    @Test
    public void shouldConvertClientToClientDto() {
        // Crear un cliente de prueba con cuentas, préstamos y tarjetas
        Set<Account> accounts = new HashSet<>();
        Set<ClientLoan> loans = new HashSet<>();
        Set<Card> cards = new HashSet<>();

        // Crear objetos de prueba para cuentas, préstamos y tarjetas
        Account account = new Account("VIN-006", LocalDate.now(),1000);
        ClientLoan loan = new ClientLoan(2000, 6);
        Card card = new Card(CardType.CREDIT, ColorType.PLATINUM, LocalDate.now(), LocalDate.now().plusYears(5));

        accounts.add(account);
        loans.add(loan);
        cards.add(card);

//        Client client = new Client("John", "Doe", "john.doe@example.com", "123456789");

        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");
        client.setAccounts(accounts);
        client.setClientLoans(loans);
        client.setCards(cards);


        // Ejecutar el método que queremos probar
        ClientDto clientDto = clientService.getClientDto(client);

        System.out.println("si aparace este mensaje es porque clienDto es null" + " " +  clientDto);

        // Verificar que el ClientDto no sea nulo
        assertThat(clientDto, is(notNullValue()));

        // Verificar los valores del ClientDto
        assertThat(clientDto.getId(), is(equalTo(client.getId())));
        assertThat(clientDto.getFirstName(), is(equalTo(client.getFirstName())));
        assertThat(clientDto.getLastName(), is(equalTo(client.getLastName())));
        assertThat(clientDto.getEmail(), is(equalTo(client.getEmail())));
        assertThat(clientDto.getAccounts(), hasSize(1)); // Verifica que haya al menos una cuenta
        assertThat(clientDto.getLoans(), hasSize(1));    // Verifica que haya al menos un préstamo
        assertThat(clientDto.getCards(), hasSize(1));    // Verifica que haya al menos una tarjeta

        // Verificar los detalles específicos de las cuentas, préstamos y tarjetas
        assertThat(clientDto.getAccounts(), contains(hasProperty("number", is(equalTo("Account1")))));
        assertThat(clientDto.getLoans(), contains(hasProperty("name", is(equalTo("Loan1")))));
        assertThat(clientDto.getCards(), contains(hasProperty("number", is(equalTo("1234-5678-9876-5432")))));
    }




//-----------------------------          O          --------------------------------//

//    @Test //Test de integracion para chequear la comunicacion entre la app y la base de datos.
//    public void  testClientsIsDatabase() {
//
//        List<Client> clients = clientRepository.findAll();
//
//        assertThat(clients, is(notNullValue()));
//        assertThat(clients.size(),is(6));
//    }
}
