package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.Utils.AccountUtils;
import com.mindhub.homebanking.dtos.AccountDto;
import com.mindhub.homebanking.dtos.ClientDto;
import com.mindhub.homebanking.dtos.LoginDto;
import com.mindhub.homebanking.dtos.RegisterDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.servicesSecurity.JwtUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")//todas las rutas que empiecen por /api/auth van a ser manejadas por este controlador.
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
     AccountRepository accountRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @PostMapping("/login")//
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
    try{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.email(),loginDto.password()));
        final UserDetails userDetails =  userDetailsService.loadUserByUsername(loginDto.email());
        final String jwt = jwtUtilService.generateToken(userDetails);
        return ResponseEntity.ok(jwt);
    } catch (Exception e){
        return new ResponseEntity<>("Email o Password invalid" , HttpStatus.BAD_REQUEST);
    }


    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){

        if(registerDto.firstName().isBlank() ){
            return new ResponseEntity<>("the name field must not be empty",HttpStatus.FORBIDDEN);
        }
        if (registerDto.lastName().isBlank()){
            return new ResponseEntity<>("the last name field must not be empty",HttpStatus.FORBIDDEN);
        }
        if (registerDto.email().isBlank()){
            return new ResponseEntity<>("the email field must not be empty",HttpStatus.FORBIDDEN);
        }
        if(registerDto.password().isBlank()){
            return new ResponseEntity<>("the password field must not be empty",HttpStatus.FORBIDDEN);
        }

        // Verifica si la contraseña cumple con el requisito mínimo de longitud.
        if (registerDto.password().length() < 8) {
            return new ResponseEntity<>("Password must be at least 8 characters long", HttpStatus.BAD_REQUEST);
        }

        // Verificar si el correo electrónico ya está registrado
        if (clientRepository.findByEmail(registerDto.email()) != null) {
            return new ResponseEntity<>("Email is already in use", HttpStatus.CONFLICT);
        }

        Client client = new Client(
                registerDto.firstName(),
                registerDto.lastName(),
                registerDto.email(),
                passwordEncoder.encode(registerDto.password()));

        clientRepository.save(client);


// Generar un número de cuenta utilizando la clase utilitaria
        String accountNumber = AccountUtils.generateAccountNumber();

        // Crear una nueva cuenta para el cliente
//        Account newAccount = new Account(accountNumber, LocalDate.now(),0.0,  client);

        Account newAccount = new Account();
        newAccount.setNumber(accountNumber);
        newAccount.setCreationDate(LocalDate.now());
        newAccount.setBalance(0.0);
        newAccount.setClient(client);


        // Guardar la cuenta en el repositorio
        accountRepository.save(newAccount);

        return new ResponseEntity<>("the user has been created and account added", HttpStatus.CREATED);

    }



    @GetMapping("/current")//metodo para obtener el usuario logueado(es decir autenticado).
    public ResponseEntity<?> getClient(Authentication authentication){

        Client client = clientRepository.findByEmail(authentication.getName());
        return new ResponseEntity<>(new ClientDto(client),HttpStatus.OK);

}




}
