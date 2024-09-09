package com.mindhub.homebanking.service.implement;


import com.mindhub.homebanking.Utils.AccountUtils;
import com.mindhub.homebanking.dtos.AccountDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.AuthService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.Utils.AccountUtils.generateAccountNumber;


@Service//Esto significa que Spring va a gestionar la instancia de esta clase y la inyectará donde sea necesario.// Lo ponemos dentro del contexto de spring como un componente de nuestra aplicacion.
public class AccountServiceImplement implements AccountService {


    @Autowired
   public AccountRepository accountRepository;

    @Autowired
    public ClientService clientService;

    @Autowired
    public AccountUtils accountUtils;



    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<AccountDto> getAllAccountsDto() {
        return getAllAccounts().stream()
                .map(AccountDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto getAccountDto(Account account) {
        return new AccountDto(account);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override// Método para guardar actualizaciones en un cuenta en la base de datos a travez del metodo save() y despues (devuelve un accountDto)
    public AccountDto saveUpdatedAccount(Account account) {
        Account updateAccount = accountRepository.save(account);
        return new AccountDto(updateAccount);
    }

    @Override
    public boolean accountExistsById(Long id) {
        // Usa el método existsById de JpaRepository para verificar si el cliente existe
        return accountRepository.existsById(id);
    }

    @Override
    public List<AccountDto> getAccountsDtoByClient(Client client) {
        return client.getAccounts().stream()
                .map(AccountDto::new)
                .collect(Collectors.toList());
    }

    @Override//metodo para guardar una cuenta en la base de datos a traves del metodo .save()
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public  AccountDto createAccountForClient(Authentication authentication){

        // Obtener el cliente autenticado
        Client client = clientService.findClientByEmail(authentication.getName());
        validateClientAccountLimit(client);
        Account newAccount = createNewAccount(client);
        saveAccount(newAccount);
        return  getAccountDto(newAccount);
    }

    @Override
    public void validateClientAccountLimit(Client client) {
        if (client.getAccounts().size() >= 3) {
            throw new IllegalStateException("The client already has 3 accounts");
        }
    }

    @Override
    public Account createNewAccount(Client client) {
        String accountNumber = generateUniqueAccountNumber();

        Account newAccount = new Account();
        newAccount.setNumber(accountNumber);
        newAccount.setCreationDate(LocalDate.now());
        newAccount.setBalance(0.0);
        client.addAccounts(newAccount);
        return newAccount;
    }

    @Override
    public Boolean existsAccountByNumber(String number) {
        return accountRepository.existsByNumber(number);
    }

    @Override
    public String generateUniqueAccountNumber() {
        String accountNumber; //declaro la variable pero no la inicializo.
        do{
            accountNumber = generateAccountNumber();
        }while(existsAccountByNumber(accountNumber));// // Verificar si ya existe
        return accountNumber;
    }


}
