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
import com.mindhub.homebanking.service.AuthService;
import com.mindhub.homebanking.service.ClientService;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ClientService clientService;

    @PostMapping("/login")//
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
    try{
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.email(),loginDto.password()));
//        final UserDetails userDetails =  userDetailsService.loadUserByUsername(loginDto.email());
//        final String jwt = jwtUtilService.generateToken(userDetails);
//        return ResponseEntity.ok(jwt);

        // Llamar al método del servicio para autenticar y generar el token JWT
        String jwt = authService.loginAndGenerateToken(loginDto);
        return ResponseEntity.ok(jwt);
    } catch (Exception e){
        return new ResponseEntity<>("Email o Password invalid" , HttpStatus.UNAUTHORIZED);
    }


    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){

        try{
                authService.registerClientWithAccount(registerDto);
                return new ResponseEntity<>("the user has been created and account added",HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }

    }


    @GetMapping("/current")//metodo para obtener el usuario logueado(es decir autenticado).
    public ResponseEntity<?> getClient(Authentication authentication){

        Client client = clientService.findClientByEmail(authentication.getName());
        return new ResponseEntity<>(clientService.getClientDto(client),HttpStatus.OK);

}




}
