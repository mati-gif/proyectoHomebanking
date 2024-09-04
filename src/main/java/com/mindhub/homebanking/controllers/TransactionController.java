package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CreateTransactionDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {


    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional // Para garantizar la atomicidad de la operación.
    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@RequestBody CreateTransactionDto createTransactionDto,
                                                Authentication authentication) {


        try {
            Client client = clientRepository.findByEmail(authentication.getName());

            // Verificar que amount no sea nulo o esté vacío
            if (createTransactionDto.amount() == null || createTransactionDto.amount() <= 0 || createTransactionDto.amount().isNaN()) {
                return new ResponseEntity<>("El monto es obligatorio y debe ser mayor a cero.", HttpStatus.FORBIDDEN);
            }

            // Verificar que los parámetros no estén vacíos
            if (createTransactionDto.description().isBlank() || createTransactionDto.sourceAccountNumber().isBlank() || createTransactionDto.destinationAccountNumber().isBlank()) {
                return new ResponseEntity<>("Todos los campos son obligatorios.", HttpStatus.FORBIDDEN);
            }


            // Verificar que los números de cuenta no sean los mismos
            if (createTransactionDto.sourceAccountNumber().equals(createTransactionDto.destinationAccountNumber())) {
                return new ResponseEntity<>("La cuenta de origen y destino no pueden ser la misma.", HttpStatus.FORBIDDEN);
            }

            // Verificar que la cuenta de origen exista
            Account sourceAccount = accountRepository.findByNumber(createTransactionDto.sourceAccountNumber());
            if (sourceAccount == null) {
               return new ResponseEntity<> ( "La cuenta de origen no existe.",HttpStatus.FORBIDDEN);
            }

            // Verificar (por el Id) que la cuenta de origen pertenece al cliente autenticado.
            if (!sourceAccount.getClient().getId().equals(client.getId())) {
                return new ResponseEntity<>("La cuenta de origen no pertenece al cliente autenticado.", HttpStatus.FORBIDDEN);

            }


            // Verificar que la cuenta de destino exista
            Account destinationAccount = accountRepository.findByNumber(createTransactionDto.destinationAccountNumber());
            if (!accountRepository.existsByNumber(createTransactionDto.destinationAccountNumber())) {
                return new ResponseEntity<>("La cuenta de destino no existe.", HttpStatus.FORBIDDEN);
            }

            // Verificar que la cuenta de origen tenga el monto disponible
            if (sourceAccount.getBalance() < createTransactionDto.amount()) {
                return new ResponseEntity<>("Saldo insuficiente en la cuenta de origen.", HttpStatus.FORBIDDEN);
            }

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

            return new ResponseEntity<>("Transferencia realizada con éxito", HttpStatus.OK);

        } catch ( Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
