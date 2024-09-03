//package com.mindhub.homebanking.service;
//
//import com.mindhub.homebanking.models.Account;
//import com.mindhub.homebanking.models.Client;
//import com.mindhub.homebanking.models.Transaction;
//import com.mindhub.homebanking.models.TransactionType;
//import com.mindhub.homebanking.repositories.AccountRepository;
//import com.mindhub.homebanking.repositories.ClientRepository;
//import com.mindhub.homebanking.repositories.TransactionRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalDateTime;
//
//public class TransactionService {
//
//    @Autowired
//    AccountRepository accountRepository;
//
//    @Autowired
//    TransactionRepository transactionRepository;
//
//    @Autowired
//    ClientRepository clientRepository;
//
//    @Transactional // Para garantizar la atomicidad de la operación.
//    public ResponseEntity<?> transferFunds(Double amount, String description, String sourceAccountNumber, String destinationAccountNumber, Authentication authentication) {
//
//        Client client = clientRepository.findByEmail(authentication.getName());
//
//        // Verificar que amount no sea nulo o esté vacío
//        if (amount == null || amount <= 0 || amount.isNaN()) {
//            System.out.println("Error: Monto inválido.");
//            return new ResponseEntity<>("El monto es obligatorio y debe ser mayor a cero.", HttpStatus.FORBIDDEN);
//        }
//
//        // Verificar que los parámetros no estén vacíos
//        if (description.isBlank() || sourceAccountNumber.isBlank() || destinationAccountNumber.isBlank()) {
//            return new ResponseEntity<>("Todos los campos son obligatorios.", HttpStatus.FORBIDDEN);
//        }
//
//
//        // Verificar que los números de cuenta no sean los mismos
//        if (sourceAccountNumber.equals(destinationAccountNumber)) {
//            return new ResponseEntity<>("La cuenta de origen y destino no pueden ser la misma.", HttpStatus.FORBIDDEN);
//        }
//
//        // Verificar que la cuenta de origen exista
//        Account sourceAccount = accountRepository.findByNumber(sourceAccountNumber);
//        if (sourceAccount == null) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La cuenta de origen no existe.");
//        }
//
//        // Verificar (por el Id) que la cuenta de origen pertenece al cliente autenticado
//        if (!sourceAccount.getClient().getId().equals(client.getId())) {
//            return new ResponseEntity<>("La cuenta de origen no pertenece al cliente autenticado.", HttpStatus.FORBIDDEN);
//
//        }
//
//
//
//
//        // Verificar que la cuenta de destino exista
//        Account destinationAccount = accountRepository.findByNumber(destinationAccountNumber);
//        if (destinationAccount == null) {
//            return new ResponseEntity<>("La cuenta de destino no existe.", HttpStatus.FORBIDDEN);
//        }
//
//        // Verificar que la cuenta de origen tenga el monto disponible
//        if (sourceAccount.getBalance() < amount) {
//            return new ResponseEntity<>("Saldo insuficiente en la cuenta de origen.", HttpStatus.FORBIDDEN);
//        }
//
//        // Crear la transacción de débito para la cuenta de origen
//        Transaction debitTransaction = new Transaction(
//                -amount,
//                description + " " + sourceAccountNumber,
//                LocalDateTime.now(),
//                TransactionType.DEBIT
//        );
//
//        // Crear la transacción de crédito para la cuenta de destino
//        Transaction creditTransaction = new Transaction(
//                amount,
//                description + " " + destinationAccountNumber,
//                LocalDateTime.now(),
//                TransactionType.CREDIT
//        );
//
//        // Asociar las transacciones a las cuentas
//        sourceAccount.addTransactions(debitTransaction);
//        destinationAccount.addTransactions(creditTransaction);
//
//        // Guardar las transacciones en el repositorio
//        transactionRepository.save(debitTransaction);
//        transactionRepository.save(creditTransaction);
//
//        // Actualizar los saldos de las cuentas
//        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
//        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
//
//        // Guardar las cuentas actualizadas en el repositorio
//        accountRepository.save(sourceAccount);
//        accountRepository.save(destinationAccount);
//
//
//
//
//        return new ResponseEntity<>("Transferencia realizada con éxito.", HttpStatus.OK);
//    }
//}
