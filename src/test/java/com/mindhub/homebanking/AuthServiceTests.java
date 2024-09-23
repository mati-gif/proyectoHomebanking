/*
package com.mindhub.homebanking;


import static java.beans.Beans.isInstanceOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import com.mindhub.homebanking.dtos.CreateLoanDto;
import com.mindhub.homebanking.dtos.LoginDto;
import com.mindhub.homebanking.dtos.RegisterDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.LoanService;
import com.mindhub.homebanking.service.implement.AuthServiceImplement;
import com.mindhub.homebanking.servicesSecurity.JwtUtilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;


import java.util.Optional;

@SpringBootTest
public class AuthServiceTests {

    @Mock
    private ClientService clientService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountService accountService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtilService jwtUtilService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImplement authServiceImplement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterClientWithAccount() {
        RegisterDto registerDto = new RegisterDto("John", "Doe", "john.doe@example.com", "password123");

        Client client = new Client("John", "Doe", "john.doe@example.com", "encodedPassword");
        Account account = new Account();
        account.setNumber("12345");

        // Configurar los mocks
        when(passwordEncoder.encode(registerDto.password())).thenReturn("encodedPassword");
        when(clientService.saveClient(any(Client.class))).thenReturn(client);
        when(accountService.generateUniqueAccountNumber()).thenReturn("12345");
        when(accountService.saveAccount(any(Account.class))).thenReturn(account);

        // Llamar al método que estamos probando
        authServiceImplement.registerClientWithAccount(registerDto);

        // Verificar que los métodos se llamaron con los objetos correctos
        verify(clientService).saveClient(argThat(c -> c.getEmail().equals("john.doe@example.com")));
        verify(accountService).saveAccount(argThat(a -> a.getNumber().equals("12345")));
    }

    @Test
    void shouldValidateRegisterDto() {
        RegisterDto validDto = new RegisterDto("John", "Doe", "john.doe@example.com", "password123");

        authServiceImplement.validateRegisterDto(validDto);

        // If no exception is thrown, the test passes
    }

    @Test
    void shouldThrowExceptionForEmptyFirstName() {
        RegisterDto invalidDto = new RegisterDto("", "Doe", "john.doe@example.com", "password123");

        assertThatThrownBy(() -> authServiceImplement.validateRegisterDto(invalidDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The name field must not be empty");
    }

    @Test
    void shouldValidateEmail() {
        RegisterDto registerDto = new RegisterDto("John", "Doe", "john.doe@example.com", "password123");
        when(clientService.findClientByEmail(registerDto.email())).thenReturn(null);

        authServiceImplement.validateEmail(registerDto);

        // If no exception is thrown, the test passes
    }

    @Test
    void shouldThrowExceptionForExistingEmail() {
        RegisterDto registerDto = new RegisterDto("John", "Doe", "john.doe@example.com", "password123");
        when(clientService.findClientByEmail(registerDto.email())).thenReturn(new Client());

        assertThatThrownBy(() -> authServiceImplement.validateEmail(registerDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email is already in use");
    }

    @Test
    void shouldCreateClient() {
        RegisterDto registerDto = new RegisterDto("John", "Doe", "john.doe@example.com", "password123");
        when(passwordEncoder.encode(registerDto.password())).thenReturn("encodedPassword");

        Client client = authServiceImplement.createClient(registerDto);

        assertThat(client,notNullValue());
        assertThat(client.getEmail(),equalTo("john.doe@example.com"));
        assertThat(client.getPassword(),equalTo("encodedPassword"));
    }

    @Test
    void shouldAuthenticateUser() {
        LoginDto loginDto = new LoginDto("john.doe@example.com", "password123");

        authServiceImplement.authenticateUser(loginDto);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password())
        );
    }

    @Test
    void shouldGenerateJwtForUser() {
        LoginDto loginDto = new LoginDto("john.doe@example.com", "password123");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(loginDto.email())).thenReturn(userDetails);
        when(jwtUtilService.generateToken(userDetails)).thenReturn("jwtToken");

        String token = authServiceImplement.loginAndGenerateToken(loginDto);

        assertThat(token,equalTo("jwtToken"));
    }
}
*/
