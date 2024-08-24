package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDto> accounts;   // Declara una variable privada de tipo Set<CuentaDto> llamada cuentas. Tambien se puede decir : Ceclara un set llamado cuentas que tendra objetos de tipo CuentaDto.
    private Set<ClientLoanDto> loans;   // Declara una variable privada de tipo Set<ClientePrestamoDto> llamada prestamos. Tambien se puede decir : Ceclara un set llamado prestamos que tendra objetos de tipo ClientePrestamoDto.
    private Set<CardsDto> cards;   // Declara una variable privada de tipo Set<TarjetaDto> llamada prestamos. Tambien se puede decir : Ceclara un set llamado prestamos que tendra objetos de tipo TarjetaDto.


    public ClientDto(Client client){
        this.id = client.getId(); //Asigna el valor del id del objeto Cliente al id del ClienteDto.
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();   // Asigna el valor del lastName del objeto Cliente al lastName del ClienteDto.
        this.email = client.getEmail();
        this.accounts = convertAccountsToDto(client.getAccounts()); //Convierte el conjunto de Cuenta del objeto Cliente a un conjunto de CuentaDto y lo asigna a la variable cuentas del ClienteDto.
        this.loans = convertClientLoansToDto(client.getClientLoans());
        this.cards = convertCardsToDto(client.getCards());

    }

    private Set<AccountDto> convertAccountsToDto(Set<Account> accounts) {   //Declara un método privado que toma un conjunto de Cuenta y devuelve un conjunto de CuentaDto.
        return accounts.stream()             //Convierte el conjunto de Cuenta en un Stream de CuentaDto.
                .map(AccountDto::new)          //Mapea cada objeto Cuenta a un nuevo objeto CuentaDto.
                .collect(Collectors.toSet());     // Recoge los objetos CuentaDto en un conjunto y lo devuelve.
    }

    private Set<ClientLoanDto> convertClientLoansToDto(Set<ClientLoan> loans) { //Declara un método privado que toma un conjunto de ClientePrestamo y devuelve un conjunto de ClientePrestamoDto.
        return loans.stream()           //Convierte el conjunto de ClientePrestamo en un Stream de ClientePrestamoDto.
                .map(ClientLoanDto::new)   //Mapea cada objeto ClientePrestamo a un nuevo objeto ClientePrestamoDto.
                .collect(Collectors.toSet());      // Recoge los objetos ClientePrestamoDto en un conjunto y lo devuelve.

    }

    private Set<CardsDto> convertCardsToDto(Set<Card> cards) {   //Declara un método privado que toma un conjunto de Cuenta y devuelve un conjunto de CuentaDto.
        return cards.stream()             //Convierte el conjunto de Cuenta en un Stream de CuentaDto.
                .map(CardsDto::new)          //Mapea cada objeto Cuenta a un nuevo objeto CuentaDto.
                .collect(Collectors.toSet());     // Recoge los objetos CuentaDto en un conjunto y lo devuelve.
    }




    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDto> getAccounts() {
        return accounts;
    }


    public Set<ClientLoanDto> getLoans() {
        return loans;
    }


    public Set<CardsDto> getCards() {
        return cards;
    }
}
