package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.CreateLoanDto;
import com.mindhub.homebanking.dtos.LoanDto;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.service.AccountService;
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
    public LoanRepository loanRepository;

    @Autowired
    public ClientLoanRepository clientLoanRepository;

    @Autowired
    public ClientService clientService;

    @Autowired
    public TransactionService transactionService;

    @Autowired
    public AccountService accountService;

//------------------------------------------------------------------------------
    //Metodo para crear un prestamo.
    @Override
    public void createLoan(CreateLoanDto createLoanDto, Authentication authentication) {
        // Obtener el cliente autenticado
        Client client = clientService.findClientByEmail(authentication.getName());

        validateDestinationAccount( createLoanDto);
        validateLoanAmount(createLoanDto);
        validatePayments(createLoanDto);
        Loan loan = validateExistsLoan(createLoanDto);
        verifySameType(client, loan);
        validateLoanAmount(createLoanDto, loan);
        validateLoanPayments(createLoanDto, loan);
        Account destinationAccount = validateDestinationAccount(createLoanDto, client);
        double totalLoanAmountWithInterest = calculateTotalLoanAmountWithInterest(createLoanDto);
        createAndSaveLoanAndTransaction(createLoanDto, client, loan, destinationAccount, totalLoanAmountWithInterest);

    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//Metodos para validar  el monto del prestamo,los payments,y si existe el prestamo.

    @Override
    public void validateDestinationAccount(CreateLoanDto createLoanDto){
        if(createLoanDto.destinationAccountNumber() == null || createLoanDto.destinationAccountNumber().isBlank() ){
            throw new IllegalArgumentException("the destination account number must not be empty");
        }
    }
    @Override
    public void validateLoanAmount(CreateLoanDto createLoanDto) {
        // Verificar que amount no sea nulo o esté vacío
        if (createLoanDto.amount() == null || createLoanDto.amount() <= 0 ) {
            throw new IllegalArgumentException("The amount is obligatory and must be greater than 0");
        }

    }

    @Override
    public void validatePayments(CreateLoanDto createLoanDto) {
        if(createLoanDto.payments() == null || createLoanDto.payments() <= 0 ){
            throw new IllegalArgumentException("the payments must be obligatory and must be greater than 0");
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
//------------------------------------------------------------------------------

//-----------------------------------------------------------------------------
    //metodo que verifica si el cliente ya tiene un prestamo del mismo tipo.
    @Override
    public void verifySameType(Client client, Loan loan) {

        // Verificar si el cliente ya tiene un préstamo del mismo tipo.
        if(clientLoanRepository.existsByClientAndLoanId(client, loan.getId())){
            throw new IllegalArgumentException("the client already has a loan of this type");
        }
    }
//---------------------------------------------------------------------------------

//Metodo que verifica que el monto solicitado no exceda el monto máximo del prestamo.
    @Override
    public void validateLoanAmount(CreateLoanDto createLoanDto, Loan loan) {
        // Verificar que el monto solicitado no exceda el monto máximo del préstamo.
        if(createLoanDto.amount() > loan.getMaxAmount()){
            throw new IllegalArgumentException("the amount requested exceeds the maximum loan amount");
        }
    }
//---------------------------------------------------------------------------------

    //Metodo que verifica que el numero de cuotas esté entre las disponibles en el préstamo.
    @Override
    public void validateLoanPayments(CreateLoanDto createLoanDto,Loan loan) {
        // Verificar que el número de cuotas esté entre las disponibles en el préstamo.
        if(!loan.getPayments().contains(createLoanDto.payments())){
            throw new IllegalArgumentException("the number of payments requested is not available");
        }
    }
//---------------------------------------------------------------------------------
    //Metodo que verifica que la cuenta de destino exista y pertenece al cliente autenticado.
    @Override
    public Account validateDestinationAccount(CreateLoanDto createLoanDto, Client client) {
        // Verificar que la cuenta de destino exista
        Account destinationAccount = accountService.getAccountByNumber(createLoanDto.destinationAccountNumber());
        if(destinationAccount == null){
            throw new IllegalArgumentException("the destination account does not exist");
        }

        // Verificar (por el Id) que la cuenta de destino pertenece al cliente autenticado.
        if(!destinationAccount.getClient().getId().equals(client.getId())){
            throw new IllegalArgumentException("the destination account does not belong to the authenticated client");
        }
        return destinationAccount;
    }
//---------------------------------------------------------------------------------

//---------------------------------------------------------------------------------
    //Metodo que calcula el monto total + interes basado en el número de cuotas.
    @Override
    public double calculateTotalLoanAmountWithInterest(CreateLoanDto createLoanDto) {

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
//---------------------------------------------------------------------------------

//---------------------------------------------------------------------------------
   //Metodo que crea una instancia de un prestamo y una instancia de una transacción.
    @Override
    public void createAndSaveLoanAndTransaction(CreateLoanDto createLoanDto, Client client, Loan loan, Account destinationAccount, double totalAmount){
        // Crear la solicitud de préstamo (ClientLoan).Es la entidad que registrará la solicitud de préstamo específica del cliente.
        ClientLoan clientLoan = new ClientLoan();
        clientLoan.setAmount(totalAmount);//Establece el monto total del préstamo en el objeto clientLoan.
        clientLoan.setPayment(createLoanDto.payments());//Establece el número de cuotas del préstamo en el objeto clientLoan.
        clientLoan.setLoan(loan);//Asocia el tipo de préstamo (Loan) con el ClientLoan. Esto indica que este ClientLoan está basado en el tipo de préstamo especificado.
        clientLoan.setClient(client); //Establece el cliente (Client) que está solicitando el préstamo. Este es el cliente que ha hecho la solicitud.
        client.addClientLoan(clientLoan);//estoy agregando el objeto ClientLoan a la lista de préstamos del cliente. Esto actualiza la relación entre el cliente y sus préstamos en memoria. Esto establece la relación desde el cliente hacia el ClientLoan.
        loan.addClientLoan(clientLoan);
        clientLoanRepository.save(clientLoan);//Guarda la instancia de ClientLoan en la base de datos a través del repositorio clientLoanRepository.
        clientService.saveClient(client);//Guarda el cliente actualizado (que ahora incluye la nueva relación de ClientLoan) en la base de datos a través del repositorio clientRepository.

        // Crear la transacción de "CRÉDITO"
        Transaction creditTransaction = new Transaction(
                totalAmount,
                loan.getName() + " Loan approved",
                LocalDateTime.now(),
                TransactionType.CREDIT
        );
        //lo que hace es Asociar la transaccion de credito con la cuenta de destino,El método addTransactions  añade la nueva transaccion de credito (creditTransaction) a esa lista.Tambien genera una relacion bidireccional asi la nueva transaccion de credito sabe a que cuenta le pertenece
        destinationAccount.addTransactions(creditTransaction);//Añade la transacción de crédito a la lista de transacciones de la cuenta de destino (destinationAccount). Esto actualiza el historial de transacciones de la cuenta.
        transactionService.saveTransaction(creditTransaction);

        // Actualiza el saldo de la cuenta de destino para reflejar el monto del préstamo recibido. Se suma el totalAmount al saldo actual de la cuenta.
        destinationAccount.setBalance(destinationAccount.getBalance() + totalAmount);
        accountService.saveAccount(destinationAccount);
    }
//---------------------------------------------------------------------------------

//---------------------------------------------------------------------------------
    //Metodo que obtiene los prestamos disponibles.
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
        //Se llama al método getClientLoans() del objeto Client el cual devuelve una lista de objetos ClientLoan
        // que representan los préstamos que el cliente ha solicitado.
        return client.getClientLoans().stream()
                .map(ClientLoan::getLoan) //devuelve el objeto loan asociado a ese clientLoan
                .collect(Collectors.toList());
    }
    @Override
    public List<LoanDto> getLoansNotRequestedByClient(List<Loan> allLoans, List<Loan> loansRequestedByClient) {
        return allLoans.stream()
                .filter(loan -> !loansRequestedByClient.contains(loan)) // Excluir los préstamos que el cliente ya solicitó. (loan) no debe estar en la lista de préstamos solicitados por el cliente
                .map(loan -> new LoanDto(//va a filtar los prestamos disponibles (allLoans) y se va a quedar con aquellos prestamos que no esten en la lista de prestamos que ya solicito.
                        loan.getId(),
                        loan.getName(),
                        loan.getMaxAmount(),
                        loan.getPayments()
                ))
                .collect(Collectors.toList());
    }
}


