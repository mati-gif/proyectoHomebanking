package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.AccountDto;
import com.mindhub.homebanking.dtos.ClientDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountService {
    List<Account> getAllAccounts();
    List<AccountDto> getAllAccountsDto();
    Account getAccountById(Long id);
    AccountDto getAccountDto(Account account);
    AccountDto saveUpdatedAccount(Account account);  // Método para guardar actualizaciones en un cuenta en la base de datos a travez del metodo save() y despues (devuelve un accountDto)
    boolean accountExistsById(Long id);  // Método que devolverá true si la cuenta existe por su ID
    List<AccountDto> getAccountsDtoByClient(Client client);// metodo para comvertir la cuentas de un cliente en una lista de cuentasDto
    Account saveAccount(Account account); //metodo para guardar una cuenta en la base de datos a traves del metodo .save()

    AccountDto createAccountForClient(Authentication authentication);
   void validateClientAccountLimit(Client client);
    Account createNewAccount(Client client);

    Boolean existsAccountByNumber(String number);
    String generateUniqueAccountNumber();
    Account getAccountByNumber(String number);


    Account getAccountByClientId(Long id);
}
