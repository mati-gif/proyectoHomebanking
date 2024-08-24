package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDto {

    private Long id;

    private String number;
    private LocalDate creationDate;
    private double balance;
    private Set<TransactionDto> transactions;


    public AccountDto(Account account) {
        this.id = account.getId();           //Asigna el valor del id del objeto Cuenta al id del CuentaDto.
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();  //asiga el valor de la fecha de creacion del objeto Cuenta al creationDate del CuentaDto.
        this.balance = account.getBalance();
        this.transactions = converTransactionsToDto(account.getTransactions());
    }

    private Set<TransactionDto> converTransactionsToDto(Set<Transaction> transactions) {
        return transactions.stream()
                .map(TransactionDto::new)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public Set<TransactionDto> getTransactions() {
        return transactions;
    }
}
