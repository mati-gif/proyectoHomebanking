package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PaymentDto;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/external")
public class PaymentSimulationController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;
    @PostMapping("/payment")
    @Transactional
    public ResponseEntity<?> simulatePayment(@RequestBody PaymentDto paymentDto
                                             ) {
        try {

            Card clientCard = cardService.getCardByNumber(paymentDto.cardNumber());

            if(clientCard == null){
                return new ResponseEntity<>("Card not found", HttpStatus.FORBIDDEN);
            }
            Client client  = clientCard.getClient();

            Account sourceAccount = client.getAccounts().stream()
                    .filter(account -> account.getBalance() >= paymentDto.maxAmount()).findFirst().orElse(null);

            if(sourceAccount == null){
                return new ResponseEntity<>("Not enough balance", HttpStatus.FORBIDDEN);
            }

            // Buscar la cuenta destino (cuenta del restaurante) usando el accountNumber del PaymentDto
            Account destinationAccount = accountService.getAccountByNumber(paymentDto.accountNumber());

            if (destinationAccount == null) {
                return new ResponseEntity<>("Destination account not found", HttpStatus.BAD_REQUEST);
            }

            // Crear transacción de débito en la cuenta de origen (cliente)
            Transaction debitTransaction = new Transaction(
                    -paymentDto.maxAmount(), // Monto negativo
                    "Payment to account " + paymentDto.accountNumber(),
                    LocalDateTime.now(),
                    TransactionType.DEBIT
            );

            // Crear transacción de crédito en la cuenta destino (restaurante)
            Transaction creditTransaction = new Transaction(
                    paymentDto.maxAmount(), // Monto positivo
                    "Payment from account " + paymentDto.cardNumber(),
                    LocalDateTime.now(),
                    TransactionType.CREDIT
            );

            // Asociar las transacciones a las cuentas correctas
            sourceAccount.addTransactions(debitTransaction); // Debitar en cuenta de origen
            destinationAccount.addTransactions(creditTransaction); // Acreditar en cuenta destino

            // Guardar las transacciones en la base de datos
            transactionService.saveTransaction(debitTransaction);
            transactionService.saveTransaction(creditTransaction);

            // Actualizar los balances de las cuentas
            sourceAccount.setBalance(sourceAccount.getBalance() - paymentDto.maxAmount()); // Restar en origen
            destinationAccount.setBalance(destinationAccount.getBalance() + paymentDto.maxAmount()); // Sumar en destino

            // Guardar las cuentas actualizadas
            accountService.saveAccount(sourceAccount); // Guardar la cuenta de origen
            accountService.saveAccount(destinationAccount); // Guardar la cuenta de destino



            return new ResponseEntity<>("Transaction processed successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
