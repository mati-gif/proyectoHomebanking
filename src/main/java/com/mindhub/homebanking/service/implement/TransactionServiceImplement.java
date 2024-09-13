package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.CreateTransactionDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;

@Service
public class TransactionServiceImplement implements TransactionService {



    @Autowired
    public TransactionRepository transactionRepository;

    @Autowired
    public ClientService clientService;

    @Autowired
    public AccountService accountService;



    @Override
    public void createTransaction(CreateTransactionDto createTransactionDto, Authentication authentication) {

        Client client = clientService.findClientByEmail(authentication.getName());
        validateTransactionAmount(createTransactionDto);
        validateOthersFields(createTransactionDto);
        validateNumbersAccountsNotSame(createTransactionDto);
        Account sourceAccount = validateSourceAccount(createTransactionDto, client);
        Account destinationAccount = validateDestinationAccount(createTransactionDto, client);//probar en borrar el client
        validateAmountAvaliable(sourceAccount,createTransactionDto.amount() );
        executeTransaction(createTransactionDto, sourceAccount, destinationAccount);

    }

    @Override
    public void validateTransactionAmount(CreateTransactionDto createTransactionDto){

        // Verificar que amount no sea nulo o esté vacío
        if (createTransactionDto.amount() == null || createTransactionDto.amount() <= 0 ) {
            System.out.println(createTransactionDto.amount());
            throw new IllegalArgumentException("the amount is obligatory and must be greater than 0.");
        }
    }

    @Override
    public void validateOthersFields(CreateTransactionDto createTransactionDto) {

        // Verificar si algún campo es nulo o está vacío
        if (createTransactionDto.description() == null || createTransactionDto.description().isBlank() ||
                createTransactionDto.sourceAccountNumber() == null || createTransactionDto.sourceAccountNumber().isBlank() ||
                createTransactionDto.destinationAccountNumber() == null || createTransactionDto.destinationAccountNumber().isBlank()) {
            throw new IllegalArgumentException("All fields are obligatory.");
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
        // Crear la transacción de débito para la cuenta de origen
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
}
