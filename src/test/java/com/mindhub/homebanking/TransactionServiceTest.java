package com.mindhub.homebanking;
import com.mindhub.homebanking.dtos.CreateTransactionDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


import static org.hamcrest.Matchers.*;

import static org.hamcrest.MatcherAssert.assertThat;        //me va a permitir usar el aserThat con el hamcrest para validar que el resultado sea el esperado.
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any; // Importa el Matcher genérico de Mockito para cualquier tipo de objeto
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {

    @MockBean
    ClientService clientService; // Servicio simulado para el cliente

    @MockBean
    Authentication authentication; // Autenticación simulada

    @SpyBean
    CardService cardService; // Usamos Spy para el servicio de tarjeta, para espiar ciertos métodos

    @MockBean
    AccountService accountService;

    @MockBean
    TransactionRepository transactionRepository;

    @SpyBean
    TransactionService transactionService;


//------------------------------------------------------------------------------------------

    //-------------------    test para el metodo createTransaction()    ----------------------//
//    @Test
//    public void testCreateTransaction_Success() {
//        // Datos de prueba
//        CreateTransactionDto createTransactionDto = new CreateTransactionDto(100.0, "Transfer", "12345", "67890");
//        Client client = new Client();  // Debes inicializar con valores adecuados
//        Account sourceAccount = new Account(); // Debes inicializar con valores adecuados
//        Account destinationAccount = new Account(); // Debes inicializar con valores adecuados
//
//        // Configuración de los mocks
//        when(authentication.getName()).thenReturn("client@example.com");
//        when(clientService.findClientByEmail("client@example.com")).thenReturn(client);
//        when(accountService.getAccountByNumber("12345")).thenReturn(sourceAccount);
//        when(accountService.getAccountByNumber("67890")).thenReturn(destinationAccount);
//        when(accountService.saveAccount(any(Account.class))).thenReturn(null);
//        when(transactionRepository.save(any(Transaction.class))).thenReturn(null);
//
//        // Ejecutar el método de prueba
//        transactionService.createTransaction(createTransactionDto, authentication);
//
//        // Verificar interacciones y estados
//        verify(transactionRepository, times(2)).save(any(Transaction.class));
//        verify(accountService, times(2)).saveAccount(any(Account.class));
//    }



//test 2 para el metodo createTransaction().

    @Test
    public void testCreateTransaction2() {
        // Crear y configurar el cliente autenticado
        Client client = new Client();
        client.setId(1L); // Configuramos el ID del cliente

        // Crear y configurar las cuentas
        Account sourceAccount = new Account();
        sourceAccount.setNumber("12345");
        sourceAccount.setClient(client);
        sourceAccount.setBalance(1000.0);

        Account destinationAccount = new Account();
        destinationAccount.setNumber("67890");
        destinationAccount.setBalance(500.0);

        // Crear DTO de transacción
        CreateTransactionDto transactionDto = new CreateTransactionDto(200.0, "Payment", "12345", "67890");

        // Simulaciones (mocks)
        Mockito.when(clientService.findClientByEmail(anyString())).thenReturn(client);
        Mockito.when(accountService.getAccountByNumber("12345")).thenReturn(sourceAccount);
        Mockito.when(accountService.getAccountByNumber("67890")).thenReturn(destinationAccount);
        Mockito.when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Retorna el objeto pasado

        // Crear una autenticación simulada
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("client@example.com");

        // Ejecutar el método
        transactionService.createTransaction(transactionDto, authentication);

        // Validaciones de assertThat con Hamcrest
        assertThat(sourceAccount.getBalance(), is(800.0));
        assertThat(destinationAccount.getBalance(), is(700.0));

        // Verificar que los métodos mock fueron llamados correctamente
        verify(transactionRepository, Mockito.times(2)).save(any(Transaction.class));
        verify(accountService, Mockito.times(2)).saveAccount(any(Account.class));
    }


    //-------------------------------------------------------------------------------

    //-------------------    test para el metodo validateTransactionAmount()    ----------------------//

    @Test
    public void testValidateTransactionAmount_invalid() {
        CreateTransactionDto transactionDto = new CreateTransactionDto(0.0, "Payment", "12345", "67890");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateTransactionAmount(transactionDto);
        });

        assertThat(exception.getMessage(), is("the amount is obligatory and must be greater than 0."));
    }



//test 2 para el metodo validateTransactionAmount().
    @Test
    public void testValidateTransactionAmount() {
        // Prueba con monto válido, no debería lanzar excepción
        CreateTransactionDto transactionDto = new CreateTransactionDto(100.0, "Description", "12345", "67890");

        // Validar que el método no lanza excepción y que el monto es mayor que 0
        transactionService.validateTransactionAmount(transactionDto);
        assertThat(transactionDto.amount(), is(greaterThan(0.0)));

        // Prueba con monto inválido (0.0), debería lanzar excepción
        CreateTransactionDto invalidTransactionDto = new CreateTransactionDto(0.0, "Description", "12345", "67890");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateTransactionAmount(invalidTransactionDto);
        });

        // Verificar que el mensaje de la excepción es el esperado
        assertThat(exception.getMessage(), containsString("the amount is obligatory and must be greater than 0."));
    }


//-------------------------------------------------------------------------------

    //-------------------    test para el metodo validateNumbersAccountsNotSame()    ----------------------//

    @Test
    public void testValidateNumbersAccountsNotSame() {
        // Caso válido: Números de cuenta diferentes
        CreateTransactionDto transactionDto = new CreateTransactionDto(100.0, "Description", "12345", "67890");

        // No debe lanzar excepción
        transactionService.validateNumbersAccountsNotSame(transactionDto);

        // Verificación usando Hamcrest: cuentas diferentes
        assertThat(transactionDto.sourceAccountNumber(), is(not(equalTo(transactionDto.destinationAccountNumber()))));

        // Caso inválido: Números de cuenta iguales
        CreateTransactionDto invalidTransactionDto = new CreateTransactionDto(100.0, "Description", "12345", "12345");

        // Verificación: debe lanzar una excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateNumbersAccountsNotSame(invalidTransactionDto);
        });

        // Verificación usando Hamcrest: mensaje de excepción
        assertThat(exception.getMessage(), containsString("The source and destination accounts must be different."));
    }


//test 2 para el metodo validateNumbersAccountsNotSame().

    @Test
    public void testValidateNumbersAccountsNotSame2() {
        // Caso válido: Números de cuenta diferentes
        CreateTransactionDto validTransactionDto = new CreateTransactionDto(100.0, "Payment", "12345", "67890");

        // Validar que no se lanza excepción y que las cuentas son diferentes
        transactionService.validateNumbersAccountsNotSame(validTransactionDto);
        assertThat(validTransactionDto.sourceAccountNumber(), is(not(equalTo(validTransactionDto.destinationAccountNumber()))));

        // Caso inválido: Números de cuenta iguales
        CreateTransactionDto invalidTransactionDto = new CreateTransactionDto(100.0, "Payment", "12345", "12345");

        // Verificar que se lanza la excepción y que el mensaje es el correcto
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateNumbersAccountsNotSame(invalidTransactionDto);
        });
        assertThat(exception.getMessage(), is("The source and destination accounts must be different."));
    }



//-------------------------------------------------------------------------------

    //-------------------    test para el metodo validateSourceAccount()    ----------------------//
    @Test
    public void testValidateSourceAccount() {
        // Configuración del cliente
        Client client = new Client();
        client.setId(1L);

        // Configuración de la cuenta de origen válida
        Account sourceAccount = new Account();
        sourceAccount.setClient(client);
        sourceAccount.setNumber("12345");

        CreateTransactionDto transactionDto = new CreateTransactionDto(100.0, "Description", "12345", "67890");

        // Mock para que el servicio retorne la cuenta de origen válida
        when(accountService.getAccountByNumber("12345")).thenReturn(sourceAccount);

        // Verificar que la cuenta de origen es válida y pertenece al cliente
        Account validatedAccount = transactionService.validateSourceAccount(transactionDto, client);
        assertThat(validatedAccount.getNumber(), is("12345"));

        // Test con cuenta de origen inexistente
        when(accountService.getAccountByNumber("54321")).thenReturn(null);

        CreateTransactionDto invalidTransactionDto = new CreateTransactionDto(100.0, "Description", "54321", "67890");

        // Verificar que se lanza la excepción cuando la cuenta no existe
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateSourceAccount(invalidTransactionDto, client);
        });
        assertThat(exception.getMessage(), is("The source account does not exist."));

        // Test con cuenta de origen que no pertenece al cliente
        Account otherClientAccount = new Account();
        Client otherClient = new Client();
        otherClient.setId(2L); // Diferente cliente
        otherClientAccount.setClient(otherClient);
        otherClientAccount.setNumber("12345");

        when(accountService.getAccountByNumber("12345")).thenReturn(otherClientAccount);

        // Verificar que se lanza la excepción cuando la cuenta no pertenece al cliente autenticado
        exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateSourceAccount(transactionDto, client);
        });
        assertThat(exception.getMessage(), is("The source account does not belong to the authenticated client."));
    }

 ////test 2 para el metodo validateSourceAccount().

    @Test
    public void testValidateSourceAccount2() {
        Client client = new Client();
        client.setId(1L);

        Account sourceAccount = new Account();
        sourceAccount.setClient(client);
        sourceAccount.setNumber("12345");

        CreateTransactionDto transactionDto = new CreateTransactionDto(100.0, "Description", "12345", "67890");

        // Mock
        Mockito.when(accountService.getAccountByNumber("12345")).thenReturn(sourceAccount);

        // Validar que la cuenta de origen es válida
        Account validatedAccount = transactionService.validateSourceAccount(transactionDto, client);
        assertThat(validatedAccount.getNumber(), is("12345"));

        // Test con cuenta de origen inválida
        CreateTransactionDto invalidTransactionDto = new CreateTransactionDto(100.0, "Description", "54321", "67890");
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateSourceAccount(invalidTransactionDto, client);
        });
    }

//-------------------------------------------------------------------------------

    //-------------------    test para el metodo validateDestinationAccount()    ----------------------//

    @Test
    public void testValidateDestinationAccount() {
        // Caso válido: la cuenta de destino existe
        Account destinationAccount = new Account();
        destinationAccount.setNumber("67890");

        CreateTransactionDto transactionDto = new CreateTransactionDto(100.0, "Description", "12345", "67890");

        // Mock para que el servicio retorne la cuenta de destino
        when(accountService.getAccountByNumber("67890")).thenReturn(destinationAccount);

        // Validar que la cuenta de destino es válida
        Account validatedAccount = transactionService.validateDestinationAccount(transactionDto, null);
        assertThat(validatedAccount.getNumber(), is("67890"));

        // Caso inválido: la cuenta de destino no existe
        when(accountService.getAccountByNumber("54321")).thenReturn(null);

        CreateTransactionDto invalidTransactionDto = new CreateTransactionDto(100.0, "Description", "12345", "54321");

        // Verificar que se lanza una excepción cuando la cuenta de destino no existe
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateDestinationAccount(invalidTransactionDto, null);
        });
        assertThat(exception.getMessage(), is("the destination account does not exist"));
    }


    //test 2 para el metodo validateDestinationAccount().
    @Test
    public void testValidateDestinationAccount2() {
        Account destinationAccount = new Account();
        destinationAccount.setNumber("67890");

        CreateTransactionDto transactionDto = new CreateTransactionDto(100.0, "Description", "12345", "67890");

        // Mock
        Mockito.when(accountService.getAccountByNumber("67890")).thenReturn(destinationAccount);

        // Validar que la cuenta de destino es válida
        Account validatedAccount = transactionService.validateDestinationAccount(transactionDto, Mockito.mock(Client.class));
        assertThat(validatedAccount.getNumber(), is("67890"));

        // Test con cuenta de destino inválida
        CreateTransactionDto invalidTransactionDto = new CreateTransactionDto(100.0, "Description", "12345", "54321");
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateDestinationAccount(invalidTransactionDto, Mockito.mock(Client.class));
        });
    }


//-------------------------------------------------------------------------------

    //-------------------    test para el metodo validateAmountAvailable()    ----------------------//

    @Test
    public void testValidateAmountAvaliable() {
        // Caso válido: la cuenta tiene saldo suficiente
        Account sourceAccount = new Account();
        sourceAccount.setBalance(500.0);

        // Validar que no se lanza excepción cuando hay suficiente saldo
        transactionService.validateAmountAvaliable(sourceAccount, 200.0);
        assertThat(sourceAccount.getBalance(), is(greaterThanOrEqualTo(200.0)));

        // Caso inválido: la cuenta no tiene saldo suficiente
        Account lowBalanceAccount = new Account();
        lowBalanceAccount.setBalance(100.0);

        // Verificar que se lanza una excepción cuando el saldo es insuficiente
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateAmountAvaliable(lowBalanceAccount, 200.0);
        });
        assertThat(exception.getMessage(), is("The source account does not have enough balance."));
    }




    //test 2 para el metodo validateAmountAvailable().
@Test
public void testValidateAmountAvaliable2() {
    Account sourceAccount = new Account();
    sourceAccount.setBalance(500.0);

    // No debe lanzar excepción
    transactionService.validateAmountAvaliable(sourceAccount, 200.0);

    // Test con saldo insuficiente
    assertThrows(IllegalArgumentException.class, () -> {
        transactionService.validateAmountAvaliable(sourceAccount, 600.0);
    });
}


//-------------------------------------------------------------------------------

    //--------------------    test para el metodo validateOthersFields()    ----------------------//

    //por separado:
    @Test
    public void testValidateOthersFields_valid() {
        CreateTransactionDto transactionDto = new CreateTransactionDto(200.0, "Payment", "12345", "67890");

        transactionService.validateOthersFields(transactionDto);

        assertThat(transactionDto.description(), is(notNullValue()));
        assertThat(transactionDto.sourceAccountNumber(), is(notNullValue()));
        assertThat(transactionDto.destinationAccountNumber(), is(notNullValue()));
    }


    @Test
    public void testValidateOthersFields_invalid() {
        CreateTransactionDto transactionDto = new CreateTransactionDto(200.0, "", "12345", "67890");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateOthersFields(transactionDto);
        });

        assertThat(exception.getMessage(), is("All fields are obligatory."));
    }


//todo junto:
    @Test
    public void testValidateOthersFields() {
        // Caso válido: Todos los campos están correctamente llenos
        CreateTransactionDto validTransactionDto = new CreateTransactionDto(200.0, "Payment", "12345", "67890");

        // Verificar que no se lanza excepción
        transactionService.validateOthersFields(validTransactionDto);

        // Usar assertThat para validar que los campos no son nulos
        assertThat(validTransactionDto.description(), is(not(emptyOrNullString())));
        assertThat(validTransactionDto.sourceAccountNumber(), is(not(emptyOrNullString())));
        assertThat(validTransactionDto.destinationAccountNumber(), is(not(emptyOrNullString())));

        // Caso inválido: Descripción vacía
        CreateTransactionDto invalidTransactionDto = new CreateTransactionDto(200.0, "", "12345", "67890");

        // Verificar que se lanza una excepción y validar el mensaje
        CreateTransactionDto finalInvalidTransactionDto = invalidTransactionDto;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateOthersFields(finalInvalidTransactionDto);
        });

        assertThat(exception.getMessage(), is("All fields are obligatory."));

        // Caso inválido: sourceAccountNumber nulo
        invalidTransactionDto = new CreateTransactionDto(200.0, "Payment", null, "67890");

        CreateTransactionDto finalInvalidTransactionDto1 = invalidTransactionDto;
        exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateOthersFields(finalInvalidTransactionDto1);
        });

        assertThat(exception.getMessage(), is("All fields are obligatory."));

        // Caso inválido: destinationAccountNumber en blanco
        invalidTransactionDto = new CreateTransactionDto(200.0, "Payment", "12345", "   ");

        CreateTransactionDto finalInvalidTransactionDto2 = invalidTransactionDto;
        exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.validateOthersFields(finalInvalidTransactionDto2);
        });

        assertThat(exception.getMessage(), is("All fields are obligatory."));
    }

//-------------------------------------------------------------------------------------------------
    //--------------------    test para el metodo executeTransaction()    ----------------------//

    //no funciona revisar

//    @Test
//    public void testExecuteTransaction() {
//        // Configuración inicial de datos
//        CreateTransactionDto createTransactionDto = new CreateTransactionDto(200.0, "Cobro", "VIN-008", "VIN-009");
//
//        Account sourceAccount = new Account();
//        sourceAccount.setNumber("VIN-009");
//        sourceAccount.setBalance(1000.0);
//
//        Account destinationAccount = new Account();
//        destinationAccount.setNumber("VIN-008");
//        destinationAccount.setBalance(500.0);
//
//        // Configurar mocks
//        when(accountService.saveAccount(any(Account.class))).thenReturn(sourceAccount);
//        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
//
//        // Ejecutar el método a probar
//        transactionService.executeTransaction(createTransactionDto, sourceAccount, destinationAccount);
//
//        // Verificar que las transacciones se guardaron correctamente
//        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
//        verify(transactionRepository, times(2)).save(transactionCaptor.capture());
//
//        List<Transaction> transactions = transactionCaptor.getAllValues();
//        assertThat(transactions, hasSize(2));
//
//        // Verificar la transacción de débito
//        Transaction debitTransaction = transactions.get(0);
//        assertThat(debitTransaction.getAmount(), is(-200.0));
//        assertThat(debitTransaction.getDescription(), equalTo("Cobro to account VIN-009"));
//        assertThat(debitTransaction.getType(), is(TransactionType.DEBIT));
//
//        // Verificar la transacción de crédito
//        Transaction creditTransaction = transactions.get(1);
//        assertThat(creditTransaction.getAmount(), is(200.0));
//        assertThat(creditTransaction.getDescription(), equalTo("Cobro from account VIN-008"));
//        assertThat(creditTransaction.getType(), is(TransactionType.CREDIT));
//
//        // Verificar que se guardaron las cuentas actualizadas
//        verify(accountService).saveAccount(sourceAccount);
//        verify(accountService).saveAccount(destinationAccount);
//
//        // Verificar los saldos actualizados
//        assertThat(sourceAccount.getBalance(), is(800.0));
//        assertThat(destinationAccount.getBalance(), is(700.0));
//    }

    //-----------------------------------------------------------------------------------------------

    //--------------------    test para el metodo saveTransaction()    ----------------------//

    @Test
    public void testSaveTransaction() {
        // Configuración de datos
        Transaction transaction = new Transaction(
                200.0,
                "Payment to account 67890",
                LocalDateTime.now(),
                TransactionType.DEBIT
        );

        // Configurar el mock
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Ejecutar el método a probar
        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        // Verificar que la transacción fue guardada y devuelta correctamente
        assertThat(savedTransaction, is(transaction));
        verify(transactionRepository).save(transaction);
    }

}
