package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.Utils.AccountUtils;
import com.mindhub.homebanking.dtos.LoginDto;
import com.mindhub.homebanking.dtos.RegisterDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.AuthService;
import com.mindhub.homebanking.servicesSecurity.JwtUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class AuthServiceImplement implements AuthService {

    @Autowired
    public ClientRepository clientRepository;

    @Autowired
    public AccountService accountService;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public AccountRepository accountRepository;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private UserDetailsService userDetailsService;


    // Método principal que gestiona todo el flujo de creación de cliente y cuenta

    @Override
    public void registerClientWithAccount(RegisterDto registerDto) {

        // 1. Validar los datos del DTO de registro
        validateRegisterDto(registerDto);
        //2.Verificar si el email ya existe en la base de datos.
        validateEmail(registerDto);
        // 3. Crear y guardar un nuevo cliente
        Client client = saveClient(createClient(registerDto));
        // 4. Crear y guardar una nueva cuenta para el cliente
        createAndSaveAccountForClient( client);

    }


    @Override
    public void validateRegisterDto(RegisterDto registerDto) {
        if (registerDto.firstName().isBlank()) {
            throw new IllegalArgumentException("The name field must not be empty");
        }
        if (registerDto.lastName().isBlank()) {
            throw new IllegalArgumentException("The last name field must not be empty");
        }
        if (registerDto.email().isBlank()) {
            throw new IllegalArgumentException("The email field must not be empty");
        }
        if (registerDto.password().isBlank()) {
            throw new IllegalArgumentException("The password field must not be empty");
        }
        if (registerDto.password().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    @Override
    public void validateEmail(RegisterDto registerDto) {
        if (clientRepository.findByEmail(registerDto.email()) != null) {
            throw new IllegalArgumentException("Email is already in use");
        }
    }

    @Override
    public Client createClient(RegisterDto registerDto) {
        Client client = new Client(
                registerDto.firstName(),
                registerDto.lastName(),
                registerDto.email(),
                passwordEncoder.encode(registerDto.password()));
        return client;

    }

    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Account createAndSaveAccountForClient(Client client) {
        //4.declaro una varibale pasar usar el metodo que genera un numero de cuenta aleatorio
        String accountNumber = AccountUtils.generateAccountNumber();

          Account newAccount = new Account();
        newAccount.setNumber(accountNumber);
        newAccount.setCreationDate(LocalDate.now());
        newAccount.setBalance(0.0);
        newAccount.setClient(client);
        client.addAccounts(newAccount);


        // Guardar la cuenta en el repositorio
        accountRepository.save(newAccount);;

        return newAccount;
    }


// Método principal que autentica al usuario y genera el JWT
public String loginAndGenerateToken(LoginDto loginDto) {
    authenticateUser(loginDto);
    return generateJwtForUser(loginDto.email());
}

// Autenticar al usuario
public void authenticateUser(LoginDto loginDto) {
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password())
        );
    } catch (Exception e) {
        throw new IllegalArgumentException("Email or Password invalid");
    }
}

// Generar el JWT para el usuario autenticado
public String generateJwtForUser(String email) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    return jwtUtilService.generateToken(userDetails);
}

}