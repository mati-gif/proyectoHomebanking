/*
package com.mindhub.homebanking;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.Utils.AccountUtils.generateAccountNumber;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
     CardRepository cardRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;


//-----------------------  Test LoanRepository ---------------------------//
//    @Test
//    public void existLoans(){
//        List<Loan> loans = loanRepository.findAll();
//        assertThat(loans,is(not(empty())));
//    }

//    @Test
//    public void existPersonalLoan(){
//
//        List<Loan> loans = loanRepository.findAll();
//        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
//    }



//-----------------------  Test TransactionRepository ---------------------------//
//   @Test
//    public void existTransactions() {
//
//        List<Transaction> transactions = transactionRepository.findAll();
//
//        assertThat(transactions, is(not(empty())));
//    }

//    @Test
//    public void createTransaction() {
//        // Create a new transaction
//        Transaction transaction = new Transaction();
//        transaction.setDescription("Test Transaction");
//        transaction.setAmount(100.0);
//        transaction.setDate(LocalDateTime.now());
//
//        // Save the transaction
//        transactionRepository.save(transaction);
//
//        // Fetch the transaction from the database
//        Transaction fetchedTransaction = transactionRepository.findById(transaction.getId()).orElse(null);
//
//        assertThat(fetchedTransaction, is(notNullValue()));
//        assertThat(fetchedTransaction.getDescription(), is("Test Transaction"));
//        assertThat(fetchedTransaction.getAmount(), is(100.0));
//    }

//-----------------------  Test CardRepository ---------------------------//

//    @Test
//    public void existsCardNumberByNumber() {
//        // Crear cliente
//        Client client = new Client("Jane", "Smith", "jane.smith@example.com");
//        clientRepository.save(client);
//
//        // Crear tarjeta asociada al cliente
//        Card card = new Card(  CardType.CREDIT,ColorType.SILVER , LocalDate.now(), LocalDate.now().plusYears(5));
//        client.addCards(card);
//        cardRepository.save(card);
//        clientRepository.save(client);
//
//        // Verificar si existe una tarjeta con el número especificado
//        Boolean exists = cardRepository.existsCardNumberByNumber("9876-5432-1234-5678");
//
//        // Assert que la tarjeta existe
//        assertThat(exists, is(false));
//    }


//    @Test
//    public void findAllCards() {
//        // Llamar a findAll para obtener todas las tarjetas
//        List<Card> cards = cardRepository.findAll();
//
//        // Verificar que se recuperaron las tarjetas esperadas
//        assertThat(cards, hasSize(16));
//
//    }


//    @Test
//    public void findCardById() {
//
//        // Llamar a findById para buscar la tarjeta
//        Card foundCard = cardRepository.findById(1L).orElse(null);
//
//        // Verificar que la tarjeta encontrada es la correcta
//        assertThat(foundCard, is(notNullValue()));
//
//    }

//-----------------------  Test AccountRepository ---------------------------//
//
//    @Test
//    public void findAccountByNumber() {
//
//        // Buscar la cuenta por número
//        Account foundAccount = accountRepository.findByNumber("VIN001");
//
//        // Verificar que la cuenta encontrada es la correcta
//        assertThat(foundAccount, is(notNullValue()));
//        assertThat(foundAccount.getNumber(), is("VIN001"));
//    }

//    @Test
//    public void existsAccountByNumber() {
//
//        // Verificar que la cuenta existe por número
//        Boolean exists = accountRepository.existsByNumber("VIN002");
//
//        // Verificar que el resultado es verdadero
//        assertThat(exists, is(true));
//    }

//-----------------------  Test ClientRepository ---------------------------//

//    @Test
//    public void findClientByEmail() {
//        // Buscar el cliente por email
//        Client foundClient = clientRepository.findByEmail("john.doe@example.com");
//
//        // Verificar que el cliente encontrado es el correcto
//        assertThat(foundClient, is(notNullValue()));
//        assertThat(foundClient.getEmail(), is("john.doe@example.com"));
//    }

//    @Test
//    public void findAllClients() {
//
//        // Llamar a findAll para obtener todos los clientes
//        List<Client> clients = clientRepository.findAll();
//
//        // Verificar que se recuperaron los clientes esperados
//        assertThat(clients, is(notNullValue()));
//        assertThat(clients, hasSize(15));
////        assertThat(clients, hasItems(client1, client2));
//    }




}
*/
