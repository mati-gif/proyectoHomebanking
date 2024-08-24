package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDto;
import com.mindhub.homebanking.models.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5174") // Configuración CORS para este controlador

@RestController

@RequestMapping("/api/accounts")

public class AccountController {



    @Autowired
    private AccountRepository countRepository;



    @GetMapping("/hello")
    public String hello() {
        return "Hello accounts!";
    }


    @GetMapping("/all")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {

        List<Account>accounts = countRepository.findAll();

        List<AccountDto> accountsDto = accounts.stream()
                .map(account -> new AccountDto(account))
                .collect(Collectors.toList());

        return new ResponseEntity<>(accountsDto, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        Account account = countRepository.findById(id).orElse(null);

        if(account == null){
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }

        AccountDto accountDto = new AccountDto(account);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }


    @PatchMapping("/nuevaCuenta")
    public ResponseEntity<?> updateAccount(@RequestParam double balance,@RequestParam Long id) {
        Account account = countRepository.findById(id).orElse(null);
        if(account == null){
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }
        account.setBalance(balance);
        countRepository.save(account);

        AccountDto accountDto = new AccountDto(account);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);

    }


    @PostMapping("/new")
    public ResponseEntity<?> createCount(@RequestParam double balance, @RequestParam String number, @RequestParam LocalDate creationDate) {

        Account account = new Account();
        account.setBalance(balance);
        account.setNumber(number);
        account.setCreationDate(creationDate);

        countRepository.save(account);

        AccountDto accountDto = new AccountDto(account);

        return new ResponseEntity<>(accountDto, HttpStatus.CREATED);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCount(@PathVariable Long id) {
        if (countRepository.existsById(id)) {
            countRepository.deleteById(id);
            return new ResponseEntity<>("Cuenta eliminada", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }
    }



    @PutMapping("/modificar/{id}")
    public ResponseEntity<?> updateCount(@PathVariable Long id, @RequestParam double balance, @RequestParam String number, @RequestParam LocalDate creationDate) {
        Account cuentaAModificar = countRepository.findById(id).orElse(null);
        if(countRepository.existsById(id)){
            cuentaAModificar.setBalance(balance);
            cuentaAModificar.setNumber(number);
            cuentaAModificar.setCreationDate(creationDate);
            countRepository.save(cuentaAModificar);
            AccountDto accountDto = new AccountDto(cuentaAModificar);
            return new ResponseEntity<>(accountDto, HttpStatus.OK);
        }
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }




}
