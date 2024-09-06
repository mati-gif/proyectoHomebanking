package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.CreateLoanDto;
import com.mindhub.homebanking.dtos.LoanDto;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.LoanService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LoanServiceImplement implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;


    @Override
    public void createLoan(CreateLoanDto createLoanDto, Authentication authentication) {
        // Obtener el cliente autenticado
        Client client = clientService.findClientByEmail(authentication.getName());


        validateLoanAmount(createLoanDto);
        validateOthersFields(createLoanDto);
        Loan loan = validateExistsLoan(createLoanDto);
        verifySameType(client, loan);
        validateLoanAmount(createLoanDto, loan);
        validateLoanPayments(createLoanDto, loan);
        Account destinationAccount = validateDestinationAccount(createLoanDto, client);
        double totalLoanAmountWithInterest = calculateTotalLoanAmountWithInterest(createLoanDto);
        createAndSaveLoanAndTransaction(createLoanDto, client, loan, destinationAccount, totalLoanAmountWithInterest);


    }

    @Override
    public void validateLoanAmount(CreateLoanDto createLoanDto) {
        // Verificar que amount no sea nulo o esté vacío
        if (createLoanDto.amount() == null || createLoanDto.amount() <= 0 ) {
            throw new IllegalArgumentException("El monto es obligatorio y debe ser mayor a cero.");
        }
    }

    @Override
    public void validateOthersFields(CreateLoanDto createLoanDto) {
        if(createLoanDto.payments() == null || createLoanDto.payments() <= 0 ){
            throw new IllegalArgumentException("the payments must be obligatory and must be greater than 0");
        }

        if(createLoanDto.destinationAccountNumber().isBlank()){
            throw new IllegalArgumentException("the destination account number must not be empty");
        }
    }

    @Override
    public Loan validateExistsLoan(CreateLoanDto createLoanDto) {

        // Verificar que el préstamo exista
        Loan loan = loanRepository.findById(createLoanDto.loanId()).orElse(null);
        if(loan == null){
            throw new IllegalArgumentException("the loan does not exist");
        }
        return loan;
    }

    @Override
    public void verifySameType(Client client, Loan loan) {

        // Verificar si el cliente ya tiene un préstamo del mismo tipo
        if(clientLoanRepository.existsByClientAndLoanId(client, loan.getId())){
            throw new IllegalArgumentException("the client already has a loan of this type");
        }
    }

    @Override
    public void validateLoanAmount(CreateLoanDto createLoanDto, Loan loan) {
        // Verificar que el monto solicitado no exceda el monto máximo del préstamo
        if(createLoanDto.amount() > loan.getMaxAmount()){
            throw new IllegalArgumentException("the amount requested exceeds the maximum loan amount");
        }
    }

    @Override
    public void validateLoanPayments(CreateLoanDto createLoanDto,Loan loan) {
        // Verificar que el número de cuotas esté entre las disponibles en el préstamo
        if(!loan.getPayments().contains(createLoanDto.payments())){
            throw new IllegalArgumentException("the number of payments requested is not available");
        }
    }

    @Override
    public Account validateDestinationAccount(CreateLoanDto createLoanDto, Client client) {
        // Verificar que la cuenta de destino exista
        Account destinationAccount = accountRepository.findByNumber(createLoanDto.destinationAccountNumber());
        if(destinationAccount == null){
            throw new IllegalArgumentException("the destination account does not exist");
        }

        // Verificar (por el Id) que la cuenta de destino pertenece al cliente autenticado.
        if(!destinationAccount.getClient().getId().equals(client.getId())){
            throw new IllegalArgumentException("the destination account does not belong to the authenticated client");
        }
        return destinationAccount;
    }

    @Override
    public double calculateTotalLoanAmountWithInterest(CreateLoanDto createLoanDto) {
        // Calcular el monto total basado en el número de cuotas
        // Calcular el monto total basado en el número de cuotas
        double interestRate;
        if (createLoanDto.payments() == 12) {
            interestRate = 0.20; // 20% para 12 cuotas
        } else if (createLoanDto.payments() > 12) {
            interestRate = 0.25; // 25% para más de 12 cuotas
        } else {
            interestRate = 0.15; // 15% para menos de 12 cuotas
        }

        double amount = createLoanDto.amount();
        double totalAmount = amount * (1 + interestRate);

        // Redondear el monto total a dos decimales
        totalAmount = Math.round(totalAmount * 100.0) / 100.0;
        return totalAmount;
    }

    @Override
    public void createAndSaveLoanAndTransaction(CreateLoanDto createLoanDto, Client client, Loan loan, Account destinationAccount, double totalAmount){
        // Crear la solicitud de préstamo (ClientLoan)
        ClientLoan clientLoan = new ClientLoan();
        clientLoan.setAmount(totalAmount);
        clientLoan.setPayment(createLoanDto.payments());
        clientLoan.setLoan(loan); // Asociamos el préstamo al ClientLoan
        clientLoan.setClient(client); // Asociamos el cliente al ClientLoan -
        client.addClientLoan(clientLoan);//estoy agregando el objeto ClientLoan a la lista de préstamos del cliente. Esto establece la relación desde el cliente hacia el ClientLoan.
        clientLoanRepository.save(clientLoan);
        clientRepository.save(client);


        // Crear la transacción de "CRÉDITO"
        Transaction creditTransaction = new Transaction(
                totalAmount,
                loan.getName() + " préstamo aprobado",
                LocalDateTime.now(),
                TransactionType.CREDIT
        );

        destinationAccount.addTransactions(creditTransaction);
        transactionRepository.save(creditTransaction);

        // Actualizar el saldo de la cuenta de destino
        destinationAccount.setBalance(destinationAccount.getBalance() + totalAmount);
        accountRepository.save(destinationAccount);
    }


    @Override
    public List<LoanDto> getAvailableLoans(Authentication authentication) {
        // Obtener el cliente autenticado
        Client client = clientService.findClientByEmail(authentication.getName());

        // Obtener todos los préstamos disponibles en la base de datos
        List<Loan> allLoans = getAllLoans();

        // Obtener los préstamos que ya ha solicitado el cliente
        List<Loan> loansRequestedByClient =  getLoansRequestedByClient(client);

        // Filtrar los préstamos que el cliente aún no ha solicitado
        return getLoansNotRequestedByClient(allLoans, loansRequestedByClient);
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public List<Loan> getLoansRequestedByClient(Client client){
        return client.getClientLoans().stream()
                .map(ClientLoan::getLoan)
                .collect(Collectors.toList());
    }
    @Override
    public List<LoanDto> getLoansNotRequestedByClient(List<Loan> allLoans, List<Loan> loansRequestedByClient) {
        return allLoans.stream()
                .filter(loan -> !loansRequestedByClient.contains(loan)) //// Excluir los préstamos que el cliente ya solicitó
                .map(loan -> new LoanDto(
                        loan.getId(),
                        loan.getName(),
                        loan.getMaxAmount(),
                        loan.getPayments()
                ))
                .collect(Collectors.toList());
    }
}


