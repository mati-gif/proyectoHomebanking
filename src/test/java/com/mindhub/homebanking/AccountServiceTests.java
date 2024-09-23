/*
package com.mindhub.homebanking;


import com.mindhub.homebanking.dtos.AccountDto;
import com.mindhub.homebanking.dtos.TransactionDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@SpringBootTest
public class AccountServiceTests {

    @SpyBean //El uso de @SpyBean es clave cuando necesitas mantener el comportamiento real de algunos métodos y mockear otros.
     AccountService accountService; // El servicio que estamos probando

    @MockBean
     AccountRepository accountRepository; // Simulamos el repositorio

    @MockBean
    ClientService clientService;

    @MockBean
    Authentication authentication;



//-----------------------   test para el metodo getAllAccounts()   -------------------------//

    @Test
    public void testGetAllAccounts() {
        // Preparamos datos de prueba
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new Account("VIN-006", LocalDate.now(), 1000));
        mockAccounts.add(new Account("VIN-007", LocalDate.now(), 2000));

        // Configuramos el comportamiento simulado
        when(accountRepository.findAll()).thenReturn(mockAccounts);

        // Ejecutamos el método que estamos probando
        List<Account> accounts = accountService.getAllAccounts();

        // Verificamos el resultado
        assertThat(accounts,hasSize(2));
        assertThat(accounts.get(0).getNumber(),equalTo("VIN-006"));
        assertThat(accounts.get(1).getNumber(),equalTo("VIN-007"));
    }

//-----------------------   test para el metodo getAllAccountsDto()   -------------------------//

    @Test
    public void testGetAllAccountsDto() {
        // Preparamos datos de prueba
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new Account("VIN-006", LocalDate.now(), 1000));
        mockAccounts.add(new Account("VIN-007", LocalDate.now(), 2000));

        // Configuramos el comportamiento simulado
        when(accountService.getAllAccounts()).thenReturn(mockAccounts);

        // Ejecutamos el método que estamos probando
        List<AccountDto> accountDtos = accountService.getAllAccountsDto();

        // Verificamos el resultado
        assertThat(accountDtos, hasSize(2));
        assertThat(accountDtos.get(0).getNumber(), equalTo("VIN-006"));
        assertThat(accountDtos.get(1).getNumber(), equalTo("VIN-007"));
    }

    //Este incluye los atributos de Transaction
    @Test
    public void testGetAllAccountsDto2() {
        // Preparamos datos de prueba para Account
        Set<Transaction> transactions = new HashSet<>();
        transactions.add(new Transaction(250,"Pago servicios" , LocalDateTime.now(), TransactionType.CREDIT));

        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new Account("VIN-006", LocalDate.now(), 1000));
        mockAccounts.add(new Account("VIN-007", LocalDate.now(), 2000));
        mockAccounts.get(0).setTransactions(transactions);
        mockAccounts.get(1).setTransactions(transactions);


        // Configuramos el comportamiento simulado
        when(accountRepository.findAll()).thenReturn(mockAccounts);

        // Ejecutamos el método que estamos probando
        List<AccountDto> accountDtos = accountService.getAllAccountsDto();

        // Verificamos el resultado
        assertThat(accountDtos, hasSize(2));

        // Verificamos el primer AccountDto
        AccountDto dto1 = accountDtos.get(0);
        assertThat(dto1.getNumber(), equalTo("VIN-006"));
        assertThat(dto1.getBalance(), equalTo(1000.0));
        assertThat(dto1.getTransactions(), hasSize(1));
        assertThat(dto1.getTransactions().iterator().next().getAmount(), equalTo(250.0));



        // Verificamos el segundo AccountDto
        AccountDto dto2 = accountDtos.get(1);
        assertThat(dto2.getNumber(), equalTo("VIN-007"));
        assertThat(dto2.getBalance(), equalTo(2000.0));
        assertThat(dto2.getTransactions(), hasSize(1));
        assertThat(dto2.getTransactions().iterator().next().getAmount(), equalTo(250.0));

    }

   //Test 3 para el metodo getAllAccountsDto()

    @Test
    public void testGetAllAccountsDto3() {
        Account account1 = new Account("123456", LocalDate.now(), 5000);
        Account account2 = new Account("654321", LocalDate.now(), 3000);

        Mockito.when(accountRepository.findAll()).thenReturn(List.of(account1, account2));

        List<AccountDto> accountDtos = accountService.getAllAccountsDto();

        assertThat(accountDtos, hasSize(2));
        assertThat(accountDtos.get(0).getNumber(), is("123456"));
        assertThat(accountDtos.get(1).getNumber(), is("654321"));
    }



//-----------------------   test para el metodo getAccountById()   -------------------------//

    @Test
    public void testGetAccountById() {
        // Preparamos datos de prueba
        Account newAccount = new Account("VIN-008", LocalDate.now(),1500);

        // Configuramos el comportamiento simulado
        when(accountRepository.findById(1L)).thenReturn(Optional.of(newAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        // Ejecutamos el método que estamos probando con un ID existente
        Account account = accountService.getAccountById(1L);

        // Verificamos el resultado
        assertThat(account, equalTo(newAccount));
        assertThat(account, is(notNullValue()));

        // Ejecutamos el método que estamos probando con un ID no existente
        Account noAccount = accountService.getAccountById(2L);

        // Verificamos el resultado cuando no se encuentra el ID
        assertThat(noAccount, equalTo(null));
    }


//-----------------------   test para el metodo accountExistsById()   -------------------------//
    @Test
    public void testAccountExistsById() {
        // Configuramos el comportamiento simulado
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(accountRepository.existsById(2L)).thenReturn(false);

        // Ejecutamos el método que estamos probando con un ID que existe
        boolean exists = accountService.accountExistsById(1L);

        // Verificamos el resultado
        assertThat(exists, equalTo(true));

        // Ejecutamos el método que estamos probando con un ID que no existe
        boolean notExists = accountService.accountExistsById(2L);

        // Verificamos el resultado
        assertThat(notExists, equalTo(false));
    }

//-----------------------   test para el metodo getAccountsDtoByClient()   -------------------------//

    @Test
    public void testGetAccountsDtoByClient() {
        // Preparamos datos de prueba para Transaction
        Transaction transaction = new Transaction(250, "Pago servicios", LocalDateTime.now(), TransactionType.CREDIT);

        // Preparamos datos de prueba para Account
        Account account = new Account("VIN-007", LocalDate.now(), 1000);
        account.addTransactions(transaction);


        // Preparamos datos de prueba para Client
        Client client = new Client(); // Asume que Client tiene un constructor sin argumentos
        client.addAccounts(account);


        // Ejecutamos el método que estamos probando
        List<AccountDto> accountDtos = accountService.getAccountsDtoByClient(client);

        // Verificamos el resultado
        assertThat(accountDtos, hasSize(1));

        // Verificamos el primer AccountDto
        AccountDto dto1 = accountDtos.get(0);
        assertThat(dto1.getNumber(), equalTo("VIN-007"));
        assertThat(dto1.getBalance(), equalTo(1000.0));

        // Verificamos las transacciones
        assertThat(dto1.getTransactions(), notNullValue()); // Asegúrate de que las transacciones no sean nulas
        assertThat(dto1.getTransactions(), hasSize(1));


//¿Por Qué Es Necesario?
// getAccountsDtoByClient convierte no solo Account sino también las transacciones asociadas a Account
// en TransactionDto.
// Verificar que las transacciones se hayan convertido correctamente asegura que la lógica de conversión completa
// es correcta.
        TransactionDto transactionDto = dto1.getTransactions().iterator().next();
        assertThat(transactionDto.getAmount(), equalTo(250.0)); // Verifica el monto de la transacción
        assertThat(transactionDto.getDescription(), equalTo("Pago servicios")); // Verifica la descripción de la transacción

    }

    //Test 2 para el metodo getAccountsDtoByClient()
    @Test
    public void testGetAccountsDtoByClient2() {
        Client client = new Client();
        Account account1 = new Account("123456", LocalDate.now(), 5000);
//        Account account2 = new Account("654321", LocalDate.now(), 3000);
        client.addAccounts(account1);
//        client.addAccounts(account2);

        when(clientService.findClientByEmail(any())).thenReturn(client);

        List<AccountDto> accountDtos = accountService.getAccountsDtoByClient(client);

        assertThat(accountDtos, hasSize(1));
        assertThat(accountDtos.get(0).getNumber(), is("123456"));
//        assertThat(accountDtos.get(1).getNumber(), is("654321"));
    }


//-----------------------   test para el metodo saveAccount()   -------------------------//
    @Test
    public void testSaveAccount() {

        // Preparamos datos de prueba para Account
        Account account = new Account("VIN-008", LocalDate.now(), 1500);
        // Configuramos el comportamiento simulado para accountRepository.save
        when(accountRepository.save(account)).thenReturn(account);

        // Ejecutamos el método que estamos probando
        Account savedAccount = accountService.saveAccount(account);

        // Verificamos el resultado
        assertThat(savedAccount, equalTo(account)); // Verifica que el objeto devuelto es el esperado
        assertThat(savedAccount.getNumber(), is("VIN-008")); // Verifica que el número de cuenta sea el esperado
    }


//-----------------------   test para el metodo getAccountByNumber()   -------------------------//
    @Test
    public void testGetAccountByNumber() {
        // Preparamos datos de prueba para Account
        Account account = new Account("VIN-009", LocalDate.now(), 2000);

        // Configuramos el comportamiento simulado para accountRepository.findByNumber
        when(accountRepository.findByNumber("VIN-009")).thenReturn(account);

        // Ejecutamos el método que estamos probando
        Account foundAccount = accountService.getAccountByNumber("VIN-009");


        // Verificamos el resultado
        assertThat(foundAccount, equalTo(account)); // Verifica que el objeto devuelto es el esperado
        assertThat(foundAccount.getNumber(), is("VIN-009")); // Verifica que el número de cuenta sea el esperado
    }


//-----------------------   test para el metodo createAccountForClient()   -------------------------//
//@Test
//public void testCreateAccountForClient() {
//    // Preparamos datos de prueba
//    Client client = new Client();
//    client.setEmail("client@example.com");
//
//    Account newAccount = new Account("VIN-010", LocalDate.now(), 2500);
//    AccountDto accountDto = new AccountDto(newAccount);
//
//    // Configuramos el comportamiento simulado para clientService.findClientByEmail
//    when(authentication.getName()).thenReturn("client@example.com");
//    when(clientService.findClientByEmail("client@example.com")).thenReturn(client);
//
//    // Configuramos el comportamiento simulado para otros métodos
//    when(accountService.createNewAccount(client)).thenReturn(newAccount);
//    when(accountRepository.save(newAccount)).thenReturn(newAccount);
//    when(accountService.getAccountDto(newAccount)).thenReturn(accountDto);
//
//    // Ejecutamos el método que estamos probando
//    AccountDto resultDto = accountService.createAccountForClient(authentication);
//
//    // Verificamos el resultado
//    assertThat(resultDto, equalTo(accountDto));
//}


    //Test 2 para el metodo createAccountForClient()
    @Test
    public void testCreateAccountForClient() {
        Client client = new Client();
        client.setEmail("client@example.com");

        Account newAccount = new Account("12345678", LocalDate.now(), 0.0);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authentication.getName()).thenReturn("client@example.com");
        Mockito.when(clientService.findClientByEmail("client@example.com")).thenReturn(client);
//        Mockito.when(accountRepository.save(any(Account.class))).thenReturn(newAccount);
        when(accountRepository.save(ArgumentMatchers.any(Account.class))).thenReturn(newAccount);

        AccountDto accountDto = accountService.createAccountForClient(authentication);

        assertThat(accountDto, is(notNullValue()));
//        assertThat(accountDto.getNumber(), is("12345678"));
    }

//-----------------------   test para el metodo existsAccountByNumber()   -------------------------//
    @Test
    public void testValidateClientAccountLimit_WithLessThan3Accounts() {
        // Crea un cliente con menos de 3 cuentas
        Client client = new Client();

        client.addAccounts(new Account()); // 3 cuentas
        client.addAccounts(new Account()); // 4 cuentas

        // Podemos usar assertThat para verificar algún comportamiento adicional si es necesario
        assertThat(client.getAccounts().size(), is(2)); // Por ejemplo, asegurarnos que tiene 2 cuentas
    }

//-----------------------   test para el metodo createNewAccount()   -------------------------//

    @Test
    public void testCreateNewAccount() {
        // Creamos un objeto Client ficticio para la prueba
        Client mockClient = new Client();
        mockClient.setId(1L); // Le damos un ID ficticio al cliente
        mockClient.setEmail("client@example.com");

        // Configuramos el comportamiento simulado del repositorio para que no haya conflicto de números
        String generatedAccountNumber = "12345678";

        // Simulamos que el método existsByNumber devuelve 'false', es decir, que el número no existe
        when(accountRepository.existsByNumber(generatedAccountNumber)).thenReturn(false);

        // Simulamos que el método generateAccountNumber devuelve el número de cuenta
        // Aquí asumimos que el método generateAccountNumber() es privado, por lo que no es posible mockearlo directamente
        // En su lugar, se mockea su impacto en el flujo
        doReturn(generatedAccountNumber).when(accountService).generateUniqueAccountNumber();

        // Llamamos al método que queremos probar
        Account newAccount = accountService.createNewAccount(mockClient);

        // Verificamos que la nueva cuenta tenga el número generado
        assertThat(newAccount.getNumber(),equalTo(generatedAccountNumber));

        // Verificamos que la cuenta esté asociada con el cliente
        assertThat(newAccount.getClient(),equalTo(mockClient));

        // Verificamos que los valores iniciales de la cuenta son correctos
        assertThat(newAccount.getBalance(),equalTo(0.0));
        assertThat(newAccount.getCreationDate(),equalTo(LocalDate.now()));
    }

    //test 2 para el metodo createNewAccount()
    @Test
    public void testCreateNewAccount2() {
        Client client = new Client();
        Account newAccount = accountService.createNewAccount(client);

        assertThat(newAccount, is(notNullValue()));
        assertThat(newAccount.getNumber(), is(notNullValue())); // El número de cuenta generado debe existir
        assertThat(newAccount.getBalance(), is(0.0));           // El balance inicial debe ser 0
        assertThat(newAccount.getClient(), is(client));         // La cuenta debe estar asociada al cliente
    }

    //-----------------------   test para el metodo existsAccountByNumber()   -------------------------//

    @Test
    public void testExistsAccountByNumber() {
        Mockito.when(accountRepository.existsByNumber("123456")).thenReturn(true);

        boolean exists = accountService.existsAccountByNumber("123456");

        assertThat(exists, is(true));
    }



    //-------------------------   test para el metodo generateUniqueAccountNumber()   -------------------------//

    @Test
    public void testGenerateUniqueAccountNumber() {

        Mockito.when(accountRepository.existsByNumber(anyString())).thenReturn(false);// Simulamos que no existe

        String accountNumber = accountService.generateUniqueAccountNumber();

        assertThat(accountNumber, is(notNullValue()));
//        assertThat(accountNumber, matchesPattern("VIN\\d{8}")); // Debe ser un número
    }

}
*/
