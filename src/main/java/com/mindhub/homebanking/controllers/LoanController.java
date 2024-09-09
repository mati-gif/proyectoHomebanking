package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CreateLoanDto;
import com.mindhub.homebanking.dtos.LoanDto;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.service.LoanService;
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
    private LoanService loanService;

    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<?> createLoan(@RequestBody CreateLoanDto createLoanDto,Authentication authentication){

        try{
            loanService.createLoan(createLoanDto, authentication);
            return new ResponseEntity<>("Loan approved successfully", HttpStatus.CREATED);

        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/loans")
    public ResponseEntity<?> getLoansAvaliable(Authentication authentication) {
//        // Obtener al cliente autenticado
//        Client client = clientRepository.findByEmail(authentication.getName());
//
//        // Obtener todos los préstamos disponibles en la base de datos
//        List<Loan> allLoans = loanRepository.findAll();
//
//        // Obtener los préstamos que ya ha solicitado el cliente
//        List<Loan> loansRequestedByClient = client.getClientLoans().stream()
//                .map(ClientLoan::getLoan) // Obtener los préstamos de las relaciones de préstamos del cliente
//                .collect(Collectors.toList());
//
//        // Filtrar los préstamos que el cliente aún no ha solicitado
//        List<LoanDto> availableLoans = allLoans.stream()
//                .filter(loan -> !loansRequestedByClient.contains(loan)) // Excluir los préstamos que el cliente ya solicitó
//                .map(loan -> new LoanDto(
//                        loan.getId(),
//                        loan.getName(),
//                        loan.getMaxAmount(),
//                        loan.getPayments()
//                ))
//                .collect(Collectors.toList());
//
//        // Si no hay préstamos disponibles (ya solicitó todos), devolver un mensaje apropiado
//        if (availableLoans.isEmpty()) {
//            return new ResponseEntity<>("No hay más préstamos disponibles.", HttpStatus.OK);
//        }
//
//        // Devolver la lista de préstamos disponibles que el cliente aún no ha solicitado
//        return new ResponseEntity<>(availableLoans, HttpStatus.OK);

        try {
            List<LoanDto> availableLoans = loanService.getAvailableLoans(authentication);
            if (availableLoans.isEmpty()) {
                return new ResponseEntity<>("No more loans available.", HttpStatus.OK);
            }
            return new ResponseEntity<>(availableLoans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



}
