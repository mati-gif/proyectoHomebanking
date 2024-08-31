package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.Utils.AccountUtils;
import com.mindhub.homebanking.dtos.AccountDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173") // Configuración CORS para este controlador

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello accounts!";
    }

    @GetMapping("/")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<Account>accounts = accountRepository.findAll();
        List<AccountDto> accountsDto = accounts.stream()
                .map(account -> new AccountDto(account))
                .collect(Collectors.toList());

        return new ResponseEntity<>(accountsDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if(account == null){
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }

        AccountDto accountDto = new AccountDto(account);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @PatchMapping("/newAccount")
    public ResponseEntity<?> updateAccount(@RequestParam double balance,@RequestParam Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if(account == null){
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }
        account.setBalance(balance);
        accountRepository.save(account);

        AccountDto accountDto = new AccountDto(account);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<?> createAccountt(@RequestParam double balance, @RequestParam String number, @RequestParam LocalDate creationDate) {

        Account account = new Account();
        account.setBalance(balance);
        account.setNumber(number);
        account.setCreationDate(creationDate);
        accountRepository.save(account);

        AccountDto accountDto = new AccountDto(account);
        return new ResponseEntity<>(accountDto, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return new ResponseEntity<>("Cuenta eliminada", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/modify/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestParam double balance, @RequestParam String number, @RequestParam LocalDate creationDate) {
        Account cuentaAModificar = accountRepository.findById(id).orElse(null);
        if(accountRepository.existsById(id)){
            cuentaAModificar.setBalance(balance);
            cuentaAModificar.setNumber(number);
            cuentaAModificar.setCreationDate(creationDate);
            accountRepository.save(cuentaAModificar);
            AccountDto accountDto = new AccountDto(cuentaAModificar);
            return new ResponseEntity<>(accountDto, HttpStatus.OK);
        }
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }


        @PostMapping("/clients/current/accounts")
        public ResponseEntity<?> createAccount(Authentication authentication) {

            // Obtener el cliente autenticado
            Client client = clientRepository.findByEmail(authentication.getName());

            // Verificar si el cliente tiene 3 cuentas o más
            if (client.getAccounts().size() >= 3) {
                return new ResponseEntity<>("El cliente ya tiene 3 cuentas", HttpStatus.FORBIDDEN);
            }

            // Generar un número de cuenta utilizando la clase utilitaria
            String accountNumber = AccountUtils.generateAccountNumber();

            // Crear la nueva cuenta
//            Account newAccount = new Account(accountNumber, LocalDate.now(),0.0 , client);

            Account newAccount = new Account();
            newAccount.setNumber(accountNumber);
            newAccount.setCreationDate(LocalDate.now());
            newAccount.setBalance(0.0);
            newAccount.setClient(client);

            // Guardar la cuenta en el repositorio
            accountRepository.save(newAccount);


            return new ResponseEntity<>(new AccountDto(newAccount), HttpStatus.CREATED);
        }



    @GetMapping("/clients/current/accounts")
    public ResponseEntity<List<AccountDto>> getAccounts(Authentication authentication) {

        // Obtener el cliente autenticado
        Client client = clientRepository.findByEmail(authentication.getName());

        // Obtener las cuentas del cliente
        Set<Account> accounts = client.getAccounts();

        // Convertir las cuentas a DTOs
        List<AccountDto> accountsDto = accounts.stream()
                .map(account -> new AccountDto(account))
                .collect(Collectors.toList());

        // Devolver la lista de cuentas del cliente
        return new ResponseEntity<>(accountsDto, HttpStatus.OK);
    }


}
