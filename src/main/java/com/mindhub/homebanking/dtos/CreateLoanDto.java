package com.mindhub.homebanking.dtos;

public record CreateLoanDto(Long loanId,Double amount,Integer payments,String destinationAccountNumber) {
}
