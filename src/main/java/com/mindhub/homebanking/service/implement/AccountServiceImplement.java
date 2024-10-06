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

    //Toma un objeto Account y lo convierte en un objeto AccountDto.
    @Override
    public AccountDto getAccountDto(Account account) {
        return new AccountDto(account);
    }

    //Busca una cuenta por su ID usando el método findById(). Si no la encuentra, devuelve null.
    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override// Método para guardar actualizaciones en un cuenta en la base de datos a travez del metodo save() y despues (devuelve un accountDto)
    public AccountDto saveUpdatedAccount(Account account) {
        Account updateAccount = accountRepository.save(account);
        return new AccountDto(updateAccount);
    }

    //Verifica si una cuenta existe por su ID utilizando el método existsById() del repositorio.
    @Override
    public boolean accountExistsById(Long id) {
        // Usa el método existsById de JpaRepository para verificar si el cliente existe
        return accountRepository.existsById(id);
    }

    //Obtiene todas las cuentas de un cliente, las convierte en objetos AccountDto y las devuelve como una lista.
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

    //Busca una cuenta por su número de cuenta usando el método findByNumber() del repositorio.
    @Override
    public Account getAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    //----------------------------------------------------------------------

    //Metodo para crear una cuenta para un cliente.

    @Override
    public  AccountDto createAccountForClient(Authentication authentication){

        // Obtener el cliente autenticado
        Client client = clientService.findClientByEmail(authentication.getName());
        validateClientAccountLimit(client);
        Account newAccount = createNewAccount(client);
        saveAccount(newAccount);
        return  getAccountDto(newAccount);
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    //Verifica que un cliente no tenga más de 3 cuentas; si las tiene, lanza una excepción.
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
        client.addAccounts(newAccount); //lo que hace es Asociar la cuenta con el cliente,El método addAccounts probablemente añade la nueva cuenta (newAccount) a esa lista.Tambien genera una relacion bidireccional asi la nueva cuenta sabe a que cliente le pertenece
        return newAccount;
    }
//-------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------
//Verifica si ya existe una cuenta con el número proporcionado usando existsByNumber().
    @Override
    public Boolean existsAccountByNumber(String number) {
        return accountRepository.existsByNumber(number);
    }

    //Genera un número de cuenta y verifica que no exista en la base de datos mediante un bucle do-while.
    // Si existe, sigue generando hasta encontrar uno único. Luego lo devuelve.
    @Override
    public String generateUniqueAccountNumber() {
        String accountNumber; //declaro la variable pero no la inicializo.
        do{
            accountNumber = generateAccountNumber();
        }while(existsAccountByNumber(accountNumber));// // Verificar si ya existe
        return accountNumber;
    }

    @Override
    public Account getAccountByClientId(Long id) {
        return accountRepository.findByClientId(id);
    }

}
