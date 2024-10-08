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

            Account newAccount = client.getAccounts().stream()
                    .filter(account -> account.getBalance() >= paymentDto.maxAmount()).findFirst().orElse(null);

            if(newAccount == null){
                return new ResponseEntity<>("Not enough balance", HttpStatus.FORBIDDEN);
            }

            Account restauranteAccount =  accountService.getAccountByNumber(paymentDto.accountNumber());


            Transaction debitTransaction = new Transaction(

                    - paymentDto.maxAmount(),
                     "Payment to account" + paymentDto.accountNumber(),
                    LocalDateTime.now(),
                    TransactionType.DEBIT


            );

            Transaction creditTransaction = new Transaction(
                    paymentDto.maxAmount(),
            "Payment from account" + paymentDto.accountNumber(),
                    LocalDateTime.now(),
                    TransactionType.CREDIT
            );


            newAccount.addTransactions(debitTransaction);
            restauranteAccount.addTransactions(creditTransaction);


            transactionService.saveTransaction(debitTransaction);
            transactionService.saveTransaction(creditTransaction);

            newAccount.setBalance(newAccount.getBalance() - paymentDto.maxAmount());
            restauranteAccount.setBalance(restauranteAccount.getBalance() + paymentDto.maxAmount());


            accountService.saveAccount(newAccount);
            accountService.saveAccount(restauranteAccount);


            return new ResponseEntity<>("Transaction processed successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
