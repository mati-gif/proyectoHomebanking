package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CreateLoanDto;
import com.mindhub.homebanking.dtos.LoanDto;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {


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


    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<?> createLoan(@RequestBody CreateLoanDto createLoanDto,Authentication authentication){

        // Obtener el cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName());

        try{



            // Verificar que los datos no estén vacíos
            if(createLoanDto.amount() == null || createLoanDto.amount() <= 0 ){
                return new ResponseEntity<>("the amount must be obligatory and must be greater than 0", HttpStatus.FORBIDDEN);
            }

            if(createLoanDto.payments() == null || createLoanDto.payments() <= 0 ){
                return new ResponseEntity<>("the payments must be obligatory and must be greater than 0", HttpStatus.FORBIDDEN);
            }

            if(createLoanDto.destinationAccountNumber().isBlank()){
                return new ResponseEntity<>("the destination account number must not be empty", HttpStatus.FORBIDDEN);
            }

            // Verificar que el préstamo exista

            Loan loan = loanRepository.findById(createLoanDto.loanId()).orElse(null);
            if(loan == null){
                return new ResponseEntity<>("the loan does not exist", HttpStatus.FORBIDDEN);
            }




            // Verificar si el cliente ya tiene un préstamo del mismo tipo
            if (clientLoanRepository.existsByClientAndLoanId(client, loan.getId())) {
                return new ResponseEntity<>("the client already has a loan of this type", HttpStatus.FORBIDDEN);
            }


            // Verificar que el monto solicitado no exceda el monto máximo del préstamo
            if (createLoanDto.amount() > loan.getMaxAmount()) {
                return new ResponseEntity<>("the amount requested exceeds the maximum loan amount", HttpStatus.BAD_REQUEST);
            }

            // Verificar que el número de cuotas esté entre las disponibles en el préstamo
            if (!loan.getPayments().contains(createLoanDto.payments())) {
                return new ResponseEntity<>("the number of payments requested is not available", HttpStatus.BAD_REQUEST);
            }

            // Verificar que la cuenta de destino exista
            Account destinationAccount = accountRepository.findByNumber(createLoanDto.destinationAccountNumber());
            if (destinationAccount == null) {
                return new ResponseEntity<>("the destination account does not exist", HttpStatus.NOT_FOUND);
            }

            // Verificar (por el Id) que la cuenta de destino pertenece al cliente autenticado.
            if (!destinationAccount.getClient().getId().equals(client.getId())) {
                return new ResponseEntity<>("the destination account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
            }

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

            // Crear la solicitud de préstamo (ClientLoan)
            ClientLoan clientLoan = new ClientLoan();
            clientLoan.setAmount(totalAmount);
            clientLoan.setPayment(createLoanDto.payments());
            clientLoan.setClient(client);
            clientLoan.setLoan(loan);
            clientLoanRepository.save(clientLoan);

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

            return new ResponseEntity<>("Loan approved successfully", HttpStatus.CREATED);


        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }


    }

    @GetMapping("/loans")
    public ResponseEntity<List<LoanDto>> getLoans() {
        // Obtener todos los préstamos de la base de datos
        List<Loan> loans = loanRepository.findAll();

        // Convertir los préstamos en DTOs
        List<LoanDto> loanDTOs = loans.stream()
                .map(loan -> new LoanDto(
                        loan.getId(),
                        loan.getName(),
                        loan.getMaxAmount(),
                        loan.getPayments()
                ))
                .collect(Collectors.toList());

        // Devolver la lista de DTOs
        return new ResponseEntity<>(loanDTOs, HttpStatus.OK);
    }


}
