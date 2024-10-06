package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.CreateTransactionDto;
import com.mindhub.homebanking.dtos.SimulateTransactionDto;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class TransactionServiceImplement implements TransactionService {



    @Autowired
    public TransactionRepository transactionRepository;

    @Autowired
    public ClientService clientService;

    @Autowired
    public AccountService accountService;

    @Autowired
    public CardService cardService;



    @Override
    public void createTransaction(CreateTransactionDto createTransactionDto, Authentication authentication) {

        Client client = clientService.findClientByEmail(authentication.getName());
//        validateTransactionAmount(createTransactionDto);
        validateOthersFields(createTransactionDto);
        validateNumbersAccountsNotSame(createTransactionDto);
        Account sourceAccount = validateSourceAccount(createTransactionDto, client);
        Account destinationAccount = validateDestinationAccount(createTransactionDto, client);//probar en borrar el client
        validateAmountAvaliable(sourceAccount,createTransactionDto.amount() );
        executeTransaction(createTransactionDto, sourceAccount, destinationAccount);

    }

//    @Override
//    public void validateTransactionAmount(CreateTransactionDto createTransactionDto){
//
//        // Verificar que amount no sea nulo o esté vacío
//        if (createTransactionDto.amount() == null || createTransactionDto.amount() <= 0 ) {
//            System.out.println(createTransactionDto.amount());
//            throw new IllegalArgumentException("the amount is obligatory and must be greater than 0.");
//        }
//    }

    @Override
    public void validateOthersFields(CreateTransactionDto createTransactionDto) {

        // Verificar si algún campo es nulo o está vacío
//        if (createTransactionDto.description() == null || createTransactionDto.description().isBlank() ||
//                createTransactionDto.sourceAccountNumber() == null || createTransactionDto.sourceAccountNumber().isBlank() ||
//                createTransactionDto.destinationAccountNumber() == null || createTransactionDto.destinationAccountNumber().isBlank()) {
//            throw new IllegalArgumentException("All fields are obligatory.");
//        }

        if(createTransactionDto.sourceAccountNumber() == null || createTransactionDto.sourceAccountNumber().isBlank()){
            throw new IllegalArgumentException("Source account number must not be empty");
        }
        if(createTransactionDto.destinationAccountNumber() == null || createTransactionDto.destinationAccountNumber().isBlank()){
            throw new IllegalArgumentException("Destination account number must not be empty");
        }

        // Verificar que amount no sea nulo o esté vacío
        if (createTransactionDto.amount() == null || createTransactionDto.amount() <= 0 ) {
            System.out.println(createTransactionDto.amount());
            throw new IllegalArgumentException("the amount is obligatory and must be greater than 0.");
        }

        if(createTransactionDto.description() == null || createTransactionDto.description().isBlank()){
            throw new IllegalArgumentException("Description  must not be empty");
        }
    }

    @Override
    public void validateNumbersAccountsNotSame(CreateTransactionDto createTransactionDto) {
        // Verificar que los números de cuenta no sean los mismos
        if (createTransactionDto.sourceAccountNumber().equals(createTransactionDto.destinationAccountNumber())) {
            throw new IllegalArgumentException("The source and destination accounts must be different.");
        }

    }

    public Account validateSourceAccount(CreateTransactionDto createTransactionDto, Client client) {
        Account sourceAccount = accountService.getAccountByNumber(createTransactionDto.sourceAccountNumber());

        // Verificar que la cuenta de origen exista
        if (sourceAccount == null) {
            throw new IllegalArgumentException("The source account does not exist.");
        }

        // Verificar (por el Id) que la cuenta de origen pertenece al cliente autenticado.
        if (!sourceAccount.getClient().getId().equals(client.getId())) {
            throw new IllegalArgumentException("The source account does not belong to the authenticated client.");
        }
        return sourceAccount;
    }

    @Override
    public Account validateDestinationAccount(CreateTransactionDto createTransactionDto, Client client) {
        Account destinationAccount = accountService.getAccountByNumber(createTransactionDto.destinationAccountNumber());
        if (destinationAccount == null) {
            throw new IllegalArgumentException("the destination account does not exist");
        }

        return destinationAccount;
    }

    @Override
    public void validateAmountAvaliable(Account sourceAccount, Double amount ) {

        // Verificar que la cuenta de origen tenga el monto disponible
        if (sourceAccount.getBalance() < amount) {
            throw new IllegalArgumentException("The source account does not have enough balance.");
        }

    }

    public void executeTransaction(CreateTransactionDto createTransactionDto, Account sourceAccount, Account destinationAccount) {


//        // Crear la transacción de débito para la cuenta de origen
        Transaction debitTransaction = new Transaction(
                - createTransactionDto.amount(),
                createTransactionDto.description() + " " + " to account " + " " +  createTransactionDto.destinationAccountNumber(),
                LocalDateTime.now(),
                TransactionType.DEBIT
        );


        // Crear la transacción de crédito para la cuenta de destino
        Transaction creditTransaction = new Transaction(
                createTransactionDto.amount(),
                createTransactionDto.description() + " " + " from account " + " " + createTransactionDto.sourceAccountNumber(),
                LocalDateTime.now(),
                TransactionType.CREDIT
        );

        // Asociar las transacciones a las cuentas
        sourceAccount.addTransactions(debitTransaction);
        destinationAccount.addTransactions(creditTransaction);

        // Guardar las transacciones en la base de datos.
        saveTransaction(debitTransaction);
        saveTransaction(creditTransaction);

        // Actualizar los saldos de las cuentas
        sourceAccount.setBalance(sourceAccount.getBalance() - createTransactionDto.amount());
        destinationAccount.setBalance(destinationAccount.getBalance() + createTransactionDto.amount());

        // Guardar las cuentas actualizadas en la base de datos .
        accountService.saveAccount(sourceAccount);
        accountService.saveAccount(destinationAccount);
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }


    @Override
    public void createTransactionWithCard(SimulateTransactionDto simulateTransactionDto) {
        // Buscar la tarjeta por su número
        Card card = cardService.getCardByNumber(simulateTransactionDto.getCardNumber());

        // Validar que la tarjeta exista
        if (card == null) {
            throw new IllegalArgumentException("Card not found");
        }

        // Validar el CVV
        if (card.getCvv() != simulateTransactionDto.getCvv()) {
            throw new IllegalArgumentException("Invalid CVV");
        }

        // Verificar que la tarjeta sea de tipo débito
        if (!card.getType().equals(CardType.DEBIT)) {
            throw new IllegalArgumentException("Only debit cards are accepted");
        }

        // Obtener la cuenta asociada a la tarjeta
        Account sourceAccount = accountService.getAccountByClientId(card.getClient().getId());

        // Validar que la cuenta tenga suficiente saldo
        if (sourceAccount.getBalance() < simulateTransactionDto.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds in source account");
        }

        // Obtener la cuenta de destino
        Account destinationAccount = accountService.getAccountByNumber(simulateTransactionDto.getDestinationAccountNumber());
        if (destinationAccount == null) {
            throw new IllegalArgumentException("Destination account not found");
        }

        // Crear las transacciones (débito y crédito)
        Transaction debitTransaction = new Transaction(
                -simulateTransactionDto.getAmount(),
                "Payment to account " + simulateTransactionDto.getDestinationAccountNumber(),
                LocalDateTime.now(),
                TransactionType.DEBIT
        );

        Transaction creditTransaction = new Transaction(
                simulateTransactionDto.getAmount(),
                "Payment from card " + simulateTransactionDto.getCardNumber(),
                LocalDateTime.now(),
                TransactionType.CREDIT
        );

        // Asociar las transacciones a las cuentas
        sourceAccount.addTransactions(debitTransaction);
        destinationAccount.addTransactions(creditTransaction);

        // Guardar las transacciones en la base de datos
        saveTransaction(debitTransaction);
        saveTransaction(creditTransaction);

        // Actualizar los saldos de las cuentas
        sourceAccount.setBalance(sourceAccount.getBalance() - simulateTransactionDto.getAmount());
        destinationAccount.setBalance(destinationAccount.getBalance() + simulateTransactionDto.getAmount());

        // Guardar las cuentas actualizadas
        accountService.saveAccount(sourceAccount);
        accountService.saveAccount(destinationAccount);
    }
}

