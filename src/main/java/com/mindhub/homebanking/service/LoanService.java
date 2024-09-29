package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CreateLoanDto;
import com.mindhub.homebanking.dtos.LoanDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface LoanService {

    void createLoan(CreateLoanDto createLoanDto, Authentication authentication);
    void validateDestinationAccount(CreateLoanDto createLoanDto);
    void validateLoanAmount(CreateLoanDto createLoanDto);
    void validatePayments(CreateLoanDto createLoanDto);
    Loan validateExistsLoan(CreateLoanDto createLoanDto);
    void verifySameType(Client client, Loan loan);
    void validateLoanAmount(CreateLoanDto createLoanDto, Loan loan);
    void validateLoanPayments(CreateLoanDto createLoanDto, Loan loan);
    Account validateDestinationAccount(CreateLoanDto createLoanDto, Client client);
    double calculateTotalLoanAmountWithInterest(CreateLoanDto createLoanDto);
    void createAndSaveLoanAndTransaction(CreateLoanDto createLoanDto, Client client, Loan loan, Account destinationAccount, double totalAmount);

    List<LoanDto> getAvailableLoans(Authentication authentication);
    List<Loan> getAllLoans();
    List<Loan> getLoansRequestedByClient(Client client);
    List<LoanDto> getLoansNotRequestedByClient(List<Loan> allLoans, List<Loan> loansRequestedByClient);
}
