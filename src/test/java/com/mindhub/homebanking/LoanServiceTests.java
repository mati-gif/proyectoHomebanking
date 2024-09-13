package com.mindhub.homebanking;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.mindhub.homebanking.dtos.CreateLoanDto;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.LoanService;
import com.mindhub.homebanking.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Collections;
import java.util.Optional;

@SpringBootTest
public class LoanServiceTests {
    @SpyBean
    private LoanRepository loanRepository;

    @MockBean
    ClientLoanRepository clientLoanRepository;

    @MockBean
    ClientService clientService;

    @MockBean
    TransactionService transactionService;

    @MockBean
    AccountService accountService;

    @Autowired
    LoanService loanService;


//    @Test
//    public void testCreateLoan_Success() {
//        // Arrange
//        CreateLoanDto createLoanDto = new CreateLoanDto(1L, 5000.0, 12, "123456789");
//        Client client = new Client();
//        Loan loan = new Loan();
//        Account destinationAccount = new Account();
//        destinationAccount.setBalance(10000.0);
//
//        when(clientService.findClientByEmail(anyString())).thenReturn(client);
//        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(loan));
//        when(clientLoanRepository.existsByClientAndLoanId(any(Client.class), anyLong())).thenReturn(false);
//        when(accountService.getAccountByNumber(anyString())).thenReturn(destinationAccount);
//        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
//        when(transactionService.saveTransaction(any(Transaction.class))).thenReturn(new Transaction());
//
//        // Act
//        loanService.createLoan(createLoanDto, mock(Authentication.class));
//
//        // Assert
//        verify(clientLoanRepository, times(1)).save(any(ClientLoan.class));
//        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
//        verify(accountService, times(1)).saveAccount(any(Account.class));
//    }




//-----------------------------------------------------------------------------

    @Test
    public void testValidateLoanAmount_InvalidAmount() {
        // Arrange
        CreateLoanDto createLoanDto = new CreateLoanDto(1L, -5000.0, 12, "123456789");

        // Act & Assert
        assertThatThrownBy(() -> loanService.validateLoanAmount(createLoanDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The amount is obligatory and must be greater than 0");
    }
//-----------------------------------------------------------------------------
    @Test
    public void testValidateOthersFields_MissingFields() {
        // Arrange
        CreateLoanDto createLoanDto = new CreateLoanDto(1L, 5000.0, 0, "");

        // Act & Assert
        assertThatThrownBy(() -> loanService.validateOthersFields(createLoanDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("the payments must be obligatory and must be greater than 0");
//                .hasMessageContaining("The payments must be obligatory and must be greater than 0");
    }
//-----------------------------------------------------------------------------
@Test
public void testValidateExistsLoan_LoanNotFound() {
    // Arrange
    CreateLoanDto createLoanDto = new CreateLoanDto(999L, 5000.0, 12, "123456789");

    // Aquí usamos un valor específico en lugar de un matcher
    when(loanRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> loanService.validateExistsLoan(createLoanDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("the loan does not exist");
}

//-----------------------------------------------------------------------------
    @Test
    public void testVerifySameType_LoanAlreadyExists() {
        // Arrange
        Client client = new Client();
        Loan loan = new Loan();
        CreateLoanDto createLoanDto = new CreateLoanDto(loan.getId(), 5000.0, 12, "123456789");

        when(clientLoanRepository.existsByClientAndLoanId(client, loan.getId())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> loanService.verifySameType(client, loan))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("the client already has a loan of this type");
    }
//-----------------------------------------------------------------------------
    @Test
    public void testValidateLoanPayments_InvalidPayments() {
        // Arrange
        CreateLoanDto createLoanDto = new CreateLoanDto(1L, 5000.0, 15, "123456789");
        Loan loan = new Loan();
        loan.setPayments(Collections.singletonList(12));

        // Act & Assert
        assertThatThrownBy(() -> loanService.validateLoanPayments(createLoanDto, loan))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("the number of payments requested is not available");
    }
//-----------------------------------------------------------------------------
    @Test
    public void testCalculateTotalLoanAmountWithInterest() {
        // Arrange
        CreateLoanDto createLoanDto = new CreateLoanDto(1L, 1000.0, 24, "123456789");

        // Act
        double totalAmount = loanService.calculateTotalLoanAmountWithInterest(createLoanDto);

        // Assert
        assertThat(totalAmount, is(closeTo(1250.0, 0.01))); // 25% interest for more than 12 payments
    }
//-----------------------------------------------------------------------------
    @Test
    public void testValidateDestinationAccount_InvalidAccount() {
        // Arrange
        CreateLoanDto createLoanDto = new CreateLoanDto(1L, 5000.0, 12, "99999999");
        Client client = new Client();

        when(accountService.getAccountByNumber(anyString())).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> loanService.validateDestinationAccount(createLoanDto, client))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("the destination account does not exist");
    }






}
