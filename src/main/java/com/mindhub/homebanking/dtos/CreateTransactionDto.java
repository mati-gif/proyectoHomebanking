package com.mindhub.homebanking.dtos;


public record CreateTransactionDto( Double amount, String description, String sourceAccountNumber, String destinationAccountNumber) {
}
