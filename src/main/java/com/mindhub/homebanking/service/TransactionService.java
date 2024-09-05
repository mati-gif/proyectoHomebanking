package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CreateTransactionDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface TransactionService {

   void createTransaction(CreateTransactionDto createTransactionDto, Authentication authentication);
   ResponseEntity<?> validateTransactionAmount(CreateTransactionDto createTransactionDto);
   ResponseEntity<?> validateOthersFields(CreateTransactionDto createTransactionDto);
   ResponseEntity<?> validateNumbersAccountsNotSame(CreateTransactionDto createTransactionDto);
   Account validateSourceAccount(CreateTransactionDto createTransactionDto, Client client);
   Account validateDestinationAccount(CreateTransactionDto createTransactionDto, Client client);
   ResponseEntity<?> validateAmountAvaliable(Account sourceAccount, Double amount );
   void executeTransaction(CreateTransactionDto createTransactionDto, Account sourceAccount,Account destinationAccount);
}
