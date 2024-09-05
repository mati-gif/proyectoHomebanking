package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.CreateTransactionDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
   private ClientService clientService;



    @Override
    public void createTransaction(CreateTransactionDto createTransactionDto, Authentication authentication) {

        Client client = clientService.findClientByEmail(authentication.getName());
        validateTransactionAmount(createTransactionDto);
        validateOthersFields(createTransactionDto);
        validateNumbersAccountsNotSame(createTransactionDto);
        Account sourceAccount = validateSourceAccount(createTransactionDto, client);
        Account destinationAccount = validateDestinationAccount(createTransactionDto, client);
        validateAmountAvaliable(sourceAccount,createTransactionDto.amount() );
        executeTransaction(createTransactionDto, sourceAccount, destinationAccount);

    }

    @Override
    public ResponseEntity<?> validateTransactionAmount(CreateTransactionDto createTransactionDto){

        // Verificar que amount no sea nulo o esté vacío
        if (createTransactionDto.amount() == null || createTransactionDto.amount() <= 0 ) {
            return new ResponseEntity<>("El monto es obligatorio y debe ser mayor a cero.", HttpStatus.FORBIDDEN);
        }

        return null;
    }

    @Override
    public ResponseEntity<?> validateOthersFields(CreateTransactionDto createTransactionDto) {

        // Verificar que los parámetros no estén vacíos
        if (createTransactionDto.description().isBlank() || createTransactionDto.sourceAccountNumber().isBlank() || createTransactionDto.destinationAccountNumber().isBlank()) {
            return new ResponseEntity<>("Todos los campos son obligatorios.", HttpStatus.FORBIDDEN);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> validateNumbersAccountsNotSame(CreateTransactionDto createTransactionDto) {
        // Verificar que los números de cuenta no sean los mismos
        if (createTransactionDto.sourceAccountNumber().equals(createTransactionDto.destinationAccountNumber())) {
            return new ResponseEntity<>("La cuenta de origen y destino no pueden ser la misma.", HttpStatus.FORBIDDEN);
        }
        return null;
    }

    public Account validateSourceAccount(CreateTransactionDto createTransactionDto, Client client) {
        Account sourceAccount = accountRepository.findByNumber(createTransactionDto.sourceAccountNumber());

        // Verificar que la cuenta de origen exista
        if (sourceAccount == null) {
            throw new IllegalArgumentException("La cuenta de origen no existe.");
        }

        // Verificar (por el Id) que la cuenta de origen pertenece al cliente autenticado.
        if (!sourceAccount.getClient().getId().equals(client.getId())) {
            throw new IllegalArgumentException("La cuenta de origen no pertenece al cliente autenticado.");

        }
        return sourceAccount;
    }

    @Override
    public Account validateDestinationAccount(CreateTransactionDto createTransactionDto, Client client) {
        Account destinationAccount = accountRepository.findByNumber(createTransactionDto.destinationAccountNumber());
        if (destinationAccount == null) {
            throw new IllegalArgumentException("La cuenta de destino no existe.");
        }

        return destinationAccount;
    }

    @Override
    public ResponseEntity<?> validateAmountAvaliable(Account sourceAccount, Double amount ) {

        // Verificar que la cuenta de origen tenga el monto disponible
        if (sourceAccount.getBalance() < amount) {
        return new ResponseEntity<>("Saldo insuficiente en la cuenta de origen.", HttpStatus.FORBIDDEN);
        }
        return null;
    }

    public void executeTransaction(CreateTransactionDto createTransactionDto, Account sourceAccount, Account destinationAccount) {
        // Crear la transacción de débito para la cuenta de origen
        Transaction debitTransaction = new Transaction(
                - createTransactionDto.amount(),
                createTransactionDto.description() + " " + "para la cuenta" + " " +  createTransactionDto.destinationAccountNumber(),
                LocalDateTime.now(),
                TransactionType.DEBIT
        );

        // Crear la transacción de crédito para la cuenta de destino
        Transaction creditTransaction = new Transaction(
                createTransactionDto.amount(),
                createTransactionDto.description() + " " + " desde la cuenta " + " " + createTransactionDto.sourceAccountNumber(),
                LocalDateTime.now(),
                TransactionType.CREDIT
        );

        // Asociar las transacciones a las cuentas
        sourceAccount.addTransactions(debitTransaction);
        destinationAccount.addTransactions(creditTransaction);

        // Guardar las transacciones en la base de datos.
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        // Actualizar los saldos de las cuentas
        sourceAccount.setBalance(sourceAccount.getBalance() - createTransactionDto.amount());
        destinationAccount.setBalance(destinationAccount.getBalance() + createTransactionDto.amount());

        // Guardar las cuentas actualizadas en el repositorio
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    }

}
