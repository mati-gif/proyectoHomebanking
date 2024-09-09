package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.Utils.AccountUtils;
import com.mindhub.homebanking.dtos.AccountDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
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
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello accounts!";
    }

    @GetMapping("/")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {

        return new ResponseEntity<>(accountService.getAllAccountsDto(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {

        if(accountService.getAccountById(id) == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(accountService.getAccountDto(accountService.getAccountById(id)), HttpStatus.OK);
    }


        @PostMapping("/clients/current/accounts")
        public ResponseEntity<?> createAccount(Authentication authentication) {

            try{
                accountService.createAccountForClient(authentication);
                return new ResponseEntity<>("Account created successfully",HttpStatus.CREATED);

            } catch (IllegalArgumentException  e){

                // Manejar el error en caso de que el cliente tenga ya el máximo de cuentas
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);

            } catch ( Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
            }

        }

    @GetMapping("/clients/current/accounts")
    public ResponseEntity<List<AccountDto>> getAccounts(Authentication authentication) {

        // Obtener el cliente autenticado
        Client client = clientService.findClientByEmail(authentication.getName());

        // Devolver la lista de cuentas del cliente
        return new ResponseEntity<>(accountService.getAccountsDtoByClient(client), HttpStatus.OK);
    }




    @PatchMapping("/newAccount")
    public ResponseEntity<?> updateAccount(@RequestParam double balance,@RequestParam Long id) {
        Account account = accountService.getAccountById(id);
        if(account == null){
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
        account.setBalance(balance);

        //guarda la cuenta actualizada usando el servicio accountService
        AccountDto updatedAccountDto = accountService.saveUpdatedAccount(account);


        return new ResponseEntity<>(updatedAccountDto, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {

        Account account = accountService.getAccountById(id);
        if (account == null) {
            return new ResponseEntity<>("not found the account with id " + id,HttpStatus.NO_CONTENT);
        } else {
            accountService.saveAccount(account);
            return new ResponseEntity<>("The account with id " + id + " was successfully deleted.", HttpStatus.OK);
        }
    }


    @PutMapping("/modify/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestParam double balance, @RequestParam String number, @RequestParam LocalDate creationDate) {
        Account cuentaAModificar = accountService.getAccountById(id);
        if(accountService.accountExistsById(id)) {
            cuentaAModificar.setBalance(balance);
            cuentaAModificar.setNumber(number);
            cuentaAModificar.setCreationDate(creationDate);
            accountService.saveUpdatedAccount(cuentaAModificar);
            AccountDto accountDto = accountService.getAccountDto(cuentaAModificar);
            return new ResponseEntity<>(accountDto, HttpStatus.OK);
        }
        return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
    }


}
