package com.mindhub.homebanking.service.implement;


import com.mindhub.homebanking.dtos.AccountDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AccountServiceImplement implements AccountService {


    @Autowired
   public AccountRepository accountRepository;


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






}
