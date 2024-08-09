package com.mindhub.homebanking;

import com.mindhub.homebanking.dtos.ClienteDto;
import com.mindhub.homebanking.dtos.CuentaDto;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;//es una interfaz que permite ejecutar código al inicio de la aplicación.
import org.springframework.boot.SpringApplication;// se usa para arrancar la aplicación Spring Boot.
import org.springframework.boot.autoconfigure.SpringBootApplication; //es una anotación que indica que esta es una aplicación Spring Boot
import org.springframework.context.annotation.Bean;//es una anotación para definir un bean de Spring.

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean//un  bean es simplemente una clase Java normal, escrita para seguir algunas reglas importantes. Pedimos que se ejecute esto primero.
	public CommandLineRunner initData(ClientRepository clientRepository, CountRepository countRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) { // Define un bean de tipo CommandLineRunner que recibe un ClientRepository como parámetro.
		// initData toma una instancia de ClientRepository como parámetro y utiliza este repositorio para guardar algunos clientes en la base de datos al iniciar la aplicación.
//Es un singleton que se ejecutan una vez cada vez que se inicia la aplicación.

		return (args) -> {// Devuelve una implementación de CommandLineRunner que guarda varios objetos Cliente en la base de datos usando clientRepository.save().
			// save a couple of customers









			////------Creando los prestamos---------////

//			List<Integer> hipotecaPayments = Arrays.asList(12, 24, 36, 48, 60);
			Prestamo prestamo1 = new Prestamo("Hipoteca",500.000, Arrays.asList(12, 24, 36, 48, 60));

			loanRepository.save(prestamo1);

//			List<Integer> personalPayments2 = Arrays.asList( 6, 12, 24.);
			Prestamo prestamo2 = new Prestamo("Personal",100.000, Arrays.asList( 6, 12, 24));
			loanRepository.save(prestamo2);


//			List<Integer> automotrizPayments3 = Arrays.asList(6, 12, 24, 36);
			Prestamo prestamo3 = new Prestamo("Automotriz",300.000, Arrays.asList(6, 12, 24, 36));

			loanRepository.save(prestamo3);

			////-----------------------------------////


			LocalDate today = LocalDate.now();
			System.out.println(today);

			LocalDate tomorrow = today.plusDays(1);
			System.out.println(tomorrow);


			LocalDateTime now = LocalDateTime.now();        //Declaras una variable llamada now de tipo LocalDateTime
			System.out.println(now);

//			LocalDateTime later = now.plusDays(1);
//			System.out.println(later);






//----------------------------------------------------------------------------------------------------------------------------------------------



			Cliente cliente1 = new Cliente("Melba", "Morel", "M.morel@me.com");
//			ClienteDto clienteDto1 = new ClienteDto(cliente1);
			clientRepository.save(cliente1);//creando y guardando en la base de datos los clientes.


			Cuenta cuenta1 = new Cuenta("VIN001", today, 5000);
			Cuenta cuenta2 = new Cuenta("VIN002", tomorrow, 7500);

//			CuentaDto cuentaDto1 = new CuentaDto(cuenta1);
//			CuentaDto cuentaDto2 = new CuentaDto(cuenta2);

			cliente1.addCuentas(cuenta1);
			cliente1.addCuentas(cuenta2);

			countRepository.save(cuenta1);
			countRepository.save(cuenta2);




			Transaccion transaccion1 = new Transaccion(500,"ingreso",now, TransactionType.CREDITO);
			Transaccion transaccion2 = new Transaccion(-700,"egreso",now.plusHours(1), TransactionType.DEBITO);
			Transaccion transaccion3 = new Transaccion(1000,"ingreso",now.plusHours(2), TransactionType.CREDITO);
			cuenta1.addTransacciones(transaccion1);
			cuenta1.addTransacciones(transaccion2);
			cuenta1.addTransacciones(transaccion3);

			transactionRepository.save(transaccion1);
			transactionRepository.save(transaccion2);
			transactionRepository.save(transaccion3);


			Transaccion transaccion4 = new Transaccion(350,"ingreso",now, TransactionType.CREDITO);
			Transaccion transaccion5 = new Transaccion(-920,"egreso",now.plusHours(1), TransactionType.DEBITO);
			Transaccion transaccion6 = new Transaccion(1300,"ingreso",now.plusHours(2), TransactionType.CREDITO);
			cuenta2.addTransacciones(transaccion4);
			cuenta2.addTransacciones(transaccion5);
			cuenta2.addTransacciones(transaccion6);

			transactionRepository.save(transaccion4);
			transactionRepository.save(transaccion5);
			transactionRepository.save(transaccion6);



/////--------------------- Crear ClientLoan y asociar los préstamos al cliente Melba    --------------//


ClientePrestamo clientePrestamo1 = new ClientePrestamo(400000.0,60);

cliente1.addClientePrestamo(clientePrestamo1);
prestamo1.addClientePrestamo(clientePrestamo1);

clientLoanRepository.save(clientePrestamo1);



ClientePrestamo clientePrestamo2 = new ClientePrestamo(50000.0,12);

cliente1.addClientePrestamo(clientePrestamo2);
prestamo2.addClientePrestamo(clientePrestamo2);

clientLoanRepository.save(clientePrestamo2);



//--------------------------------------------------------------------------------------------------------------------------------------------


			Cliente cliente2 = new Cliente("Chloe", "O'Brian", "c.obrian@me.com");
			clientRepository.save(cliente2);


			Cuenta cuenta3 = new Cuenta("VIN003", today, 10000);
			Cuenta cuenta4 = new Cuenta("VIN004", tomorrow, 20000);
			cliente2.addCuentas(cuenta3);
			cliente2.addCuentas(cuenta4);


			countRepository.save(cuenta3);
			countRepository.save(cuenta4);



		Transaccion transaccion7 = new Transaccion(-270,"egreso",now, TransactionType.DEBITO);
		Transaccion transaccion8 = new Transaccion(1960,"ingreso",now.plusHours(1), TransactionType.CREDITO);
		Transaccion transaccion9 = new Transaccion(-2000,"egreso",now.plusHours(3), TransactionType.DEBITO);
		cuenta3.addTransacciones(transaccion7);
		cuenta3.addTransacciones(transaccion8);
		cuenta3.addTransacciones(transaccion9);

		transactionRepository.save(transaccion7);
		transactionRepository.save(transaccion8);
		transactionRepository.save(transaccion9);






/////--------------------- Crear ClientLoan y asociar los préstamos al cliente  Chloe   --------------//


ClientePrestamo clientePrestamo3 = new ClientePrestamo(10000.0,24);

cliente2.addClientePrestamo(clientePrestamo3);
prestamo3.addClientePrestamo(clientePrestamo3);



clientLoanRepository.save(clientePrestamo3);





ClientePrestamo clientePrestamo4 = new ClientePrestamo(200000.0,36);

cliente2.addClientePrestamo(clientePrestamo4);
prestamo3.addClientePrestamo(clientePrestamo4);



clientLoanRepository.save(clientePrestamo4);








//----------------------------------------------------------------------------------------------------------------------------------------------------------
			Cliente cliente3 = new Cliente("Kim", "Bauer", "k.bauer@me.com");
			clientRepository.save(cliente3);

			Cliente cliente4 = new Cliente("David", "Palmer", "d.palmer@me.com");
			clientRepository.save(cliente4);

			Cliente cliente5 = new Cliente("Michelle", "Dessler", "m.dessler@me.com");
			clientRepository.save(cliente5);

			Cliente cliente6 = new Cliente("Lionel", "Messi", "l.messi@me.com");
			clientRepository.save(cliente6);

			System.out.println(cliente6);

//			clientRepository.save(new Cliente("Jack", "Bauer", "j.bauer@me.com"));
//			clientRepository.save(new Cliente("Chloe", "O'Brian", "c.obrian@me.com"));
//			clientRepository.save(new Cliente("Kim", "Bauer",	"k.bauer@me.com"));
//			clientRepository.save(new Cliente("David", "Palmer", "d.palmer@me.com"));
//			clientRepository.save(new Cliente("Michelle", "Dessler", "m.dessler@me.com"));
//			clientRepository.save(new Cliente("Lionel", "Messi", "l.messi@me.com"));
		};
	}

}
