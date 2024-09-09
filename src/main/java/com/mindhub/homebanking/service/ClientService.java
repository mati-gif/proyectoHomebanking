package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.ClientDto;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllClient();
    List<ClientDto> getAllClientDto();
    Client getClientById(Long id);
    ClientDto getClientDto(Client client);
    ClientDto saveUpdatedClient(Client client);  // Método para guardar actualizaciones en un cliente
    boolean clientExistsById(Long id);  // Método que devolverá true si el cliente existe por su ID
    Client findClientByEmail(String email);
    Client saveClient(Client client);

}
