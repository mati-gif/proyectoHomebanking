package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CreateTransactionDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.security.core.Authentication;

public interface TransactionService {

   void createTransaction(CreateTransactionDto createTransactionDto, Authentication authentication);
//   void validateTransactionAmount(CreateTransactionDto createTransactionDto);
  void validateOthersFields(CreateTransactionDto createTransactionDto);
   void validateNumbersAccountsNotSame(CreateTransactionDto createTransactionDto);
   Account validateSourceAccount(CreateTransactionDto createTransactionDto, Client client);
   Account validateDestinationAccount(CreateTransactionDto createTransactionDto, Client client);
  void validateAmountAvaliable(Account sourceAccount, Double amount );
   void executeTransaction(CreateTransactionDto createTransactionDto, Account sourceAccount,Account destinationAccount);
   Transaction saveTransaction(Transaction transaction);



}
