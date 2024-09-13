package com.mindhub.homebanking;
import com.mindhub.homebanking.dtos.CreateTransactionDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

import static org.hamcrest.MatcherAssert.assertThat;        //me va a permitir usar el aserThat con el hamcrest para validar que el resultado sea el esperado.
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

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

    @Autowired
    TransactionRepository transactionRepository;

    @SpyBean
    TransactionService transactionService;


//------------------------------------------------------------------------------------------
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



    //-------------------------------------------------------------------------------
    @Test
    public void testValidateTransactionAmount_InvalidAmount() {
        CreateTransactionDto createTransactionDto = new CreateTransactionDto(-100.0, "Transfer", "12345", "67890");

        // Verificar que se lanza una excepción cuando el monto es inválido
        assertThatThrownBy(() -> transactionService.validateTransactionAmount(createTransactionDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("the amount is obligatory and must be greater than 0.");
    }


//-------------------------------------------------------------------------------

    @Test
    public void testValidateNumbersAccountsNotSame_SameNumbers() {
        CreateTransactionDto createTransactionDto = new CreateTransactionDto(100.0, "Transfer", "12345", "12345");

        // Verificar que se lanza una excepción cuando las cuentas son iguales
        assertThatThrownBy(() -> transactionService.validateNumbersAccountsNotSame(createTransactionDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The source and destination accounts must be different.");
    }

//-------------------------------------------------------------------------------

    @Test
    public void testValidateSourceAccount_SourceAccountNotFound() {
        CreateTransactionDto createTransactionDto = new CreateTransactionDto(100.0, "Transfer", "12345", "67890");
        Client client = new Client();  // Debes inicializar con valores adecuados

        // Configuración del mock
        when(accountService.getAccountByNumber("12345")).thenReturn(null);

        // Verificar que se lanza una excepción cuando la cuenta de origen no se encuentra
        assertThatThrownBy(() -> transactionService.validateSourceAccount(createTransactionDto, client))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The source account does not exist.");
    }

//-------------------------------------------------------------------------------

    @Test
    public void testValidateDestinationAccount_DestinationAccountNotFound() {
        CreateTransactionDto createTransactionDto = new CreateTransactionDto(100.0, "Transfer", "12345", "67890");

        // Configuración del mock
        when(accountService.getAccountByNumber("67890")).thenReturn(null);

        // Verificar que se lanza una excepción cuando la cuenta de destino no se encuentra
        assertThatThrownBy(() -> transactionService.validateDestinationAccount(createTransactionDto, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("the destination account does not exist");
    }

//-------------------------------------------------------------------------------

    @Test
    public void testValidateAmountAvailable_InsufficientFunds() {
        CreateTransactionDto createTransactionDto = new CreateTransactionDto(100.0, "Transfer", "12345", "67890");
        Account sourceAccount = new Account();
        sourceAccount.setBalance(50.0);

        // Verificar que se lanza una excepción cuando el saldo es insuficiente
        assertThatThrownBy(() -> transactionService.validateAmountAvaliable(sourceAccount, createTransactionDto.amount()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The source account does not have enough balance.");
    }



}
