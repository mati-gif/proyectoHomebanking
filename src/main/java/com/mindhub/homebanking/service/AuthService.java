package com.mindhub.homebanking.service;


import com.mindhub.homebanking.dtos.LoginDto;
import com.mindhub.homebanking.dtos.RegisterDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

public interface AuthService {
    void registerClientWithAccount(RegisterDto registerDto);
    void validateRegisterDto(RegisterDto registerDto);
    void validateEmail(RegisterDto registerDto);
    Client createClient(RegisterDto registerDto);
    Client saveClient(Client client);
    Account createAndSaveAccountForClient(Client client);

    String generateJwtForUser(String email);
    void authenticateUser(LoginDto loginDto);
    String loginAndGenerateToken(LoginDto loginDto);
}
