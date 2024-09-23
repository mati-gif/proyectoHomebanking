/*
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest//va a ser una clase de testeos por eso lleva esta anotacion para que este en el contexto de spring cuando se ejecuten los testeos
public class ClientServiceTests {

    @Autowired //va a simular que existe un clientService
     ClientService clientService;

    @MockBean
     ClientRepository clientRepository;

//    @Autowired //para hacer un test de integración
//    private ClientRepository clientRepository;


//------------------------    test del metodo findClientByEmail()       --------------------------//

    //Vamos a testear un metodo de la clase ClientService,es un testeo unitario
//    @Test//con esta anotacion indico que vot a generar un nuevo testeo y se va a asegurar que el metodo devuelva algo.
//    public void findClientByEmail() {
//
//        when(clientService.findClientByEmail("M.morel@me.com")).thenReturn(new Client("Melba","Morel","M.morel@me.com","123456789"));
//
//        Client client = clientService.findClientByEmail("M.morel@me.com");
//
//        assertThat(client, is(notNullValue()));
//        assertThat(client.getEmail(),equalTo("M.morel@me.com"));
//    }
//-------------------------------------------------------------------------------------------//

//------------------------    test del metodo saveClient()     --------------------------//

//    @Test
//    public void saveClient(){
//
//        //Creo un nuevo cliente de prueba
//        Client client = new Client("John", "Doe", "john.doe@gmail.com", "123456789");
//
//        // Simulamos el comportamiento del repositorio para el método saveClient()
//        when(clientRepository.save(client)).thenReturn(client);
//
//
//        // Ejecutamos el método que queremos probar
//        Client savedClient = clientService.saveClient(client);
//
//        // Validamos que el cliente no sea nulo
//        assertThat(savedClient, is(notNullValue()));
//
//        // Verificamos que el cliente guardado tenga los mismos atributos que el original
//        assertThat(savedClient.getEmail(), equalTo("john.doe@gmail.com"));
//        assertThat(savedClient.getFirstName(), equalTo("John"));
//        assertThat(savedClient.getLastName(), equalTo("Doe"));
//        assertThat(savedClient.getPassword(), equalTo("123456789"));
//
//    }
//-------------------------------------------------------------------------------------------//

//------------------------    test del metodo clientExistsById()     --------------------------//
//    @Test
//    public void testClientExistsById() {
//        // Arrange
//        when(clientRepository.existsById(1L)).thenReturn(true);
//
//        // Act
//        boolean exists = clientService.clientExistsById(1L);
//
//        // Assert
//        assertThat(exists, equalTo(true));
//    }
//
//    @Test
//    public void shouldReturnFalseIfClientDoesNotExist() {
//        // Simulamos que el cliente con ID 99 no existe en la base de datos
//        when(clientService.clientExistsById(99L)).thenReturn(false);
//
//        // Llamamos al método que queremos probar
//        boolean exists = clientService.clientExistsById(99L);
//
//        // Verificamos que el resultado sea falso
//        assertThat(exists, is(false)); // Aquí usamos 'is(false)' para validar el valor booleano
//    }

//-------------------------------------------------------------------------------------------//

//------------------------    test del metodo getClientById()     --------------------------//

//    @Test
//    public void testGetClientById() {
//        // Arrange
//        Client client = new Client("John", "Doe", "john.doe@gmail.com", "123456789");
//        when(clientRepository.findById(1L)).thenReturn(java.util.Optional.of(client));
//
//        // Act
//        Client foundClient = clientService.getClientById(1L);
//
//        // Assert
//        assertThat(foundClient, is(notNullValue()));
//        assertThat(foundClient.getEmail(), equalTo("john.doe@gmail.com"));
//        assertThat(foundClient.getFirstName(), equalTo("John"));
//        assertThat(foundClient.getLastName(), equalTo("Doe"));
//        assertThat(foundClient.getPassword(), equalTo("123456789"));
//    }



//-------------------------------------------------------------------------------------------//

//------------------------    test del metodo getAllClient()     --------------------------//

//    @Test
//    public void  testClientsIsDatabase() {
//
//// Creamos una lista de clientes de prueba
//        Client client1 = new Client("John", "Doe", "john.doe@gmail.com");
//        Client client2 = new Client("Jane", "Smith", "jane.smith@gmail.com");
//        List<Client> mockClients = Arrays.asList(client1, client2);
//
//        when(clientRepository.findAll()).thenReturn(mockClients);
//
//        // Llamamos al método que queremos probar
//        List<Client> clients = clientService.getAllClient();
//
//        // Verificamos que la lista no sea nula
//        assertThat(clients, is(notNullValue()));
//        // Verificamos que la lista tiene 2 elementos
//        assertThat(clients.size(), is(2));
//        // Verificamos los detalles de los clientes
//        assertThat(client1.getEmail(), equalTo("john.doe@gmail.com"));
//        assertThat(client2.getEmail(), equalTo("jane.smith@gmail.com"));
//        assertThat(clients, contains(client1, client2));
//    }
//    @Test
//    public void shouldReturnEmptyListWhenNoClientsExist() {
//        // Simulamos que el método devuelve una lista vacía
//        when(clientService.getAllClient()).thenReturn(Collections.emptyList());
//
//        // Llamamos al método que queremos probar
//        List<Client> clients = clientService.getAllClient();
//
//        // Verificamos que la lista no sea nula
//        assertThat(clients, is(notNullValue()));
//        // Verificamos que la lista esté vacía
//        assertThat(clients.isEmpty(), is(true));
//    }
//-------------------------------------------------------------------------------------------//

//------------------------    test del metodo getAllClientDto()     --------------------------//

//    @Test
//    public void testGetAllClientDto() {
//        // Arrange
//        Client client1 = mock(Client.class);
//        Client client2 = mock(Client.class);
//        when(client1.isActive()).thenReturn(true);
//        when(client2.isActive()).thenReturn(false);
//        when(clientRepository.findAll()).thenReturn(Arrays.asList(client1, client2));
//
//        // Act
//        List<ClientDto> clientDtos = clientService.getAllClientDto();
//
//        // Assert
//        assertThat(clientDtos, hasSize(1));
//        assertThat(clientDtos.get(0), is(instanceOf(ClientDto.class)));
//    }






//-------------------------------------------------------------------------------------------//

    //------------------------    test del metodo getClientDto()     --------------------------//
//    @Test
//    public void shouldConvertClientToClientDto() {
//        // Crear un cliente de prueba con cuentas, préstamos y tarjetas
//        Set<Account> accounts = new HashSet<>();
//        Set<ClientLoan> loans = new HashSet<>();
//        Set<Card> cards = new HashSet<>();
//
//        // Crear objetos de prueba para cuentas, préstamos y tarjetas
//        Account account = new Account("VIN-006", LocalDate.now(),1000);
//        ClientLoan loan = new ClientLoan(2000, 6);
//        Card card = new Card(CardType.CREDIT, ColorType.PLATINUM, LocalDate.now(), LocalDate.now().plusYears(5));
//
//        accounts.add(account);
//        loans.add(loan);
//        cards.add(card);
//
////        Client client = new Client("John", "Doe", "john.doe@example.com", "123456789");
//
//        Client client = new Client();
//        client.setId(1L);
//        client.setFirstName("John");
//        client.setLastName("Doe");
//        client.setEmail("john.doe@example.com");
//        client.setAccounts(accounts);
//        client.setClientLoans(loans);
//        client.setCards(cards);
//
//
//        // Ejecutar el método que queremos probar
//        ClientDto clientDto = clientService.getClientDto(client);
//
//        System.out.println("si aparace este mensaje es porque clienDto es null" + " " +  clientDto);
//
//        // Verificar que el ClientDto no sea nulo
//        assertThat(clientDto, is(notNullValue()));
//
//        // Verificar los valores del ClientDto
//        assertThat(clientDto.getId(), is(equalTo(client.getId())));
//        assertThat(clientDto.getFirstName(), is(equalTo(client.getFirstName())));
//        assertThat(clientDto.getLastName(), is(equalTo(client.getLastName())));
//        assertThat(clientDto.getEmail(), is(equalTo(client.getEmail())));
//        assertThat(clientDto.getAccounts(), hasSize(1)); // Verifica que haya al menos una cuenta
//        assertThat(clientDto.getLoans(), hasSize(1));    // Verifica que haya al menos un préstamo
//        assertThat(clientDto.getCards(), hasSize(1));    // Verifica que haya al menos una tarjeta
//
//        // Verificar los detalles específicos de las cuentas, préstamos y tarjetas
//        assertThat(clientDto.getAccounts(), contains(hasProperty("number", is(equalTo("Account1")))));
//        assertThat(clientDto.getLoans(), contains(hasProperty("name", is(equalTo("Loan1")))));
//        assertThat(clientDto.getCards(), contains(hasProperty("number", is(equalTo("1234-5678-9876-5432")))));
//    }

//------------------------    test 2 del metodo getClientDto()     --------------------------//
//    @Test
//    public void testGetClientDto() {
//        // Arrange
//        Client client = mock(Client.class);
//
//        // Act
//        ClientDto clientDto = clientService.getClientDto(client);
//
//        // Assert
//        assertThat(clientDto, is(notNullValue()));
//        assertThat(clientDto.getId(), is(client.getId()));
//    }






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
*/
