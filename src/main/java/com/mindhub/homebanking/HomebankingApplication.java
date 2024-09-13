package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;//es una interfaz que permite ejecutar código al inicio de la aplicación.
import org.springframework.boot.SpringApplication;// se usa para arrancar la aplicación Spring Boot.
import org.springframework.boot.autoconfigure.SpringBootApplication; //es una anotación que indica que esta es una aplicación Spring Boot
import org.springframework.context.annotation.Bean;//es una anotación para definir un bean de Spring.
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {

//	@Autowired
//	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean//un  bean es simplemente una clase Java normal, escrita para seguir algunas reglas importantes. Pedimos que se ejecute esto primero.
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository,CardRepository cardRepository) { // Define un bean de tipo CommandLineRunner que recibe un ClientRepository como parámetro.
		// initData toma una instancia de ClientRepository como parámetro y utiliza este repositorio para guardar algunos clientes en la base de datos al iniciar la aplicación.
		//Es un singleton que se ejecutan una vez cada vez que se inicia la aplicación.

		return (args) -> {// Devuelve una implementación de CommandLineRunner que guarda varios objetos Cliente en la base de datos usando clientRepository.save().
			// save a couple of customers
//
//
//		////--------------Creando localDate fromDate y thruDate-------------////
//			LocalDate fromDate = LocalDate.now();
//			LocalDate thruDate = fromDate.plusYears(5);
//			System.out.println(fromDate);
//			System.out.println(thruDate);
//
//
///////----------------------------------------------------------------------////
//
//
//
//			////------Creando los prestamos---------////
//
////			List<Integer> hipotecaPayments = Arrays.asList(12, 24, 36, 48, 60);
//			Loan loan1 = new Loan("Hipoteca",500000.000, Arrays.asList(12, 24, 36, 48, 60));
//			loanRepository.save(loan1);
//
////			List<Integer> personalPayments2 = Arrays.asList( 6, 12, 24.);
//			Loan loan2 = new Loan("Personal",100000.000, Arrays.asList( 6, 12, 24));
//			loanRepository.save(loan2);
//
//
////			List<Integer> automotrizPayments3 = Arrays.asList(6, 12, 24, 36);
//			Loan loan3 = new Loan("Automotriz",300000.000, Arrays.asList(6, 12, 24, 36));
//			loanRepository.save(loan3);
//
//			////-----------------------------------////
//
//
//			LocalDate today = LocalDate.now();
//			System.out.println(today);
//
//			LocalDate tomorrow = today.plusDays(1);
//			System.out.println(tomorrow);
//
//			LocalDateTime now = LocalDateTime.now();        //Declaras una variable llamada now de tipo LocalDateTime
//			System.out.println(now);
//
//
////----------------------------------------------------------------------------------------------------------------------------------------------
//
//
//
//			Client client1 = new Client("Melba", "Morel", "M.morel@me.com", passwordEncoder.encode("123"));
////			ClientDto clientDto1 = new ClientDto(client1);
//			clientRepository.save(client1);//creando y guardando en la base de datos los clientes.
//
//
//			Account account1 = new Account("VIN001", today, 5000);
//			Account account2 = new Account("VIN002", tomorrow, 7500);
//
////			CuentaDto cuentaDto1 = new CuentaDto(cuenta1);
////			CuentaDto cuentaDto2 = new CuentaDto(cuenta2);
//
//			client1.addAccounts(account1);
//			client1.addAccounts(account2);
//
//			accountRepository.save(account1);
//			accountRepository.save(account2);
//
//
//			Transaction transaction1 = new Transaction(500,"ingreso",now, TransactionType.CREDIT);
//			Transaction transaction2 = new Transaction(-700,"egreso",now.plusHours(1), TransactionType.DEBIT);
//			Transaction transaction3 = new Transaction(1000,"ingreso",now.plusHours(2), TransactionType.CREDIT);
//			account1.addTransactions(transaction1);
//			account1.addTransactions(transaction2);
//			account1.addTransactions(transaction3);
//
//			transactionRepository.save(transaction1);
//			transactionRepository.save(transaction2);
//			transactionRepository.save(transaction3);
//
//
//			Transaction transaction4 = new Transaction(350,"ingreso",now, TransactionType.CREDIT);
//			Transaction transaction5 = new Transaction(-920,"egreso",now.plusHours(1), TransactionType.DEBIT);
//			Transaction transaction6 = new Transaction(1300,"ingreso",now.plusHours(2), TransactionType.CREDIT);
//			account2.addTransactions(transaction4);
//			account2.addTransactions(transaction5);
//			account2.addTransactions(transaction6);
//
//			transactionRepository.save(transaction4);
//			transactionRepository.save(transaction5);
//			transactionRepository.save(transaction6);
//
//
//
///////--------------------- Crear ClientLoan y asociar los préstamos al cliente Melba    --------------//
//
//
//ClientLoan clientLoan1 = new ClientLoan(400000.0,60);
//
//client1.addClientLoan(clientLoan1);
//loan1.addClientLoan(clientLoan1);
//
//clientLoanRepository.save(clientLoan1);
//
//
//
//ClientLoan clientLoan2 = new ClientLoan(50000.0,12);
//
//client1.addClientLoan(clientLoan2);
//loan2.addClientLoan(clientLoan2);
//
//clientLoanRepository.save(clientLoan2);
//
//
//////////---------------------------Creando las tarjetas para el cliente Melba-------------------------///////
//
//Card card1 = new Card(CardType.DEBIT,ColorType.GOLD,fromDate,thruDate);
//Card card2 = new Card(CardType.CREDIT,ColorType.PLATINUM,fromDate,thruDate);
//
//
//client1.addCards(card1);
//client1.addCards(card2);
//
//cardRepository.save(card1);
//cardRepository.save(card2);
//
//
//
//
//
//
////--------------------------------------------------------------------------------------------------------------------------------------------
//
//
//			Client client2 = new Client("Chloe", "O'Brian", "c.obrian@me.com",passwordEncoder.encode("456"));
////			ClientDto clientDto2 = new ClientDto(client2);
//			clientRepository.save(client2);
//
//
//			Account account3 = new Account("VIN003", today, 10000);
//			Account account4 = new Account("VIN004", tomorrow, 20000);
//
//			//			CuentaDto cuentaDto3 = new CuentaDto(cuenta3);
//			//			CuentaDto cuentaDto4 = new CuentaDto(cuenta4);
//
//
//			client2.addAccounts(account3);
//			client2.addAccounts(account4);
//
//
//
//			accountRepository.save(account3);
//			accountRepository.save(account4);
//
//
//
//		Transaction transaction7 = new Transaction(-270,"egreso",now, TransactionType.DEBIT);
//		Transaction transaction8 = new Transaction(1960,"ingreso",now.plusHours(1), TransactionType.CREDIT);
//		Transaction transaction9 = new Transaction(-2000,"egreso",now.plusHours(3), TransactionType.DEBIT);
//		account3.addTransactions(transaction7);
//		account3.addTransactions(transaction8);
//		account3.addTransactions(transaction9);
//
//		transactionRepository.save(transaction7);
//		transactionRepository.save(transaction8);
//		transactionRepository.save(transaction9);
//
//
//
//
//
//
///////--------------------- Crear ClientLoan y asociar los préstamos al cliente  Chloe   --------------//
//
//
//ClientLoan clientLoan3 = new ClientLoan(10000.0,24);
//
//client2.addClientLoan(clientLoan3);
//loan3.addClientLoan(clientLoan3);
//
//
//
//clientLoanRepository.save(clientLoan3);
//
//
//
//
//
//ClientLoan clientLoan4 = new ClientLoan(200000.0,36);
//
//client2.addClientLoan(clientLoan4);
//loan2.addClientLoan(clientLoan4);
//
//
//
//clientLoanRepository.save(clientLoan4);
//
//
//
//////////---------------------------Creando las tarjetas para el cliente Chloe-------------------------///////
//
//			Card card3 = new Card(CardType.DEBIT,ColorType.GOLD,fromDate,thruDate);
//			Card card4 = new Card(CardType.CREDIT,ColorType.PLATINUM,fromDate,thruDate);
//
//
//			client2.addCards(card3);
//			client2.addCards(card4);
//
//			cardRepository.save(card3);
//			cardRepository.save(card4);
//
//
//
//
//
//
//
////----------------------------------------------------------------------------------------------------------------------------------------------------------
//			Client client3 = new Client("Kim", "Bauer", "k.bauer@me.com",passwordEncoder.encode("789"));
//			clientRepository.save(client3);
//
//			Client client4 = new Client("David", "Palmer", "d.palmer@me.com",passwordEncoder.encode("321"));
//			clientRepository.save(client4);
//
//			Client client5 = new Client("Michelle", "Dessler", "m.dessler@me.com",passwordEncoder.encode("654"));
//			clientRepository.save(client5);
//
//			Client client6 = new Client("Lionel", "Messi", "l.messi@me.com",passwordEncoder.encode("987"));
//			clientRepository.save(client6);
//
//			System.out.println(client6);


		};
	}

}
