package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Cliente;//importo la clase cliente.
import com.mindhub.homebanking.models.Cuenta;
import com.mindhub.homebanking.repositories.ClientRepository;// importo el repositorio , para poder hacer operaciones con la base de datos.
import com.mindhub.homebanking.repositories.CountRepository;
import org.springframework.boot.CommandLineRunner;//es una interfaz que permite ejecutar código al inicio de la aplicación.
import org.springframework.boot.SpringApplication;// se usa para arrancar la aplicación Spring Boot.
import org.springframework.boot.autoconfigure.SpringBootApplication; //es una anotación que indica que esta es una aplicación Spring Boot
import org.springframework.context.annotation.Bean;//es una anotación para definir un bean de Spring.

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean//un  bean es simplemente una clase Java normal, escrita para seguir algunas reglas importantes. Pedimos que se ejecute esto primero.
	public CommandLineRunner initData(ClientRepository clientRepository, CountRepository countRepository) { // Define un bean de tipo CommandLineRunner que recibe un ClientRepository como parámetro.
		// initData toma una instancia de ClientRepository como parámetro y utiliza este repositorio para guardar algunos clientes en la base de datos al iniciar la aplicación.
//Es un singleton que se ejecutan una vez cada vez que se inicia la aplicación.

		return (args) -> {// Devuelve una implementación de CommandLineRunner que guarda varios objetos Cliente en la base de datos usando clientRepository.save().
			// save a couple of customers


			LocalDate today = LocalDate.now();
			System.out.println(today);

			LocalDate tomorrow = today.plusDays(1);
			System.out.println(tomorrow);





			Cliente cliente1 = new Cliente("Melba", "Morel", "M.morel@me.com");
			clientRepository.save(cliente1);//creando y guardando en la base de datos los clientes.


			Cuenta cuenta1 = new Cuenta("VIN001", today, 5000);
			Cuenta cuenta2 = new Cuenta("VIN002", tomorrow, 7500);
			cliente1.addCuentas(cuenta1);
			cliente1.addCuentas(cuenta2);

			countRepository.save(cuenta1);
			countRepository.save(cuenta2);



			Cliente cliente2 = new Cliente("Chloe", "O'Brian", "c.obrian@me.com");
			clientRepository.save(cliente2);


			Cuenta cuenta3 = new Cuenta("VIN003", today, 10000);
			Cuenta cuenta4 = new Cuenta("VIN004", tomorrow, 20000);
			cliente2.addCuentas(cuenta3);
			cliente2.addCuentas(cuenta4);

			countRepository.save(cuenta3);
			countRepository.save(cuenta4);





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
