package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.ClientDto;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    public ClientRepository clientRepository;


    @Override
    public List<Client> getAllClient() {
        return clientRepository.findAll();
    }


    @Override
    public List<ClientDto> getAllClientDto() {
        return getAllClient().stream()
                .filter(client -> client.isActive())  // Usa una expresión lambda para filtrar clientes activos
                .map(ClientDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDto getClientDto(Client client) {
        return new ClientDto(client);
    }
    //Este método solo convierte un objeto Client en un objeto ClientDto.
    //No interactúa con la base de datos.
    //Es útil cuando quieres transformar un cliente en un DTO sin modificar ni guardar el cliente en la base de datos.

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);//Busca en el repositorio clientRepository un cliente con el id especificado. Si no se encuentra un cliente con ese id, retorna null.
    }

    @Override
    public ClientDto saveUpdatedClient(Client client) {
        Client updatedClient = clientRepository.save(client);  // Guardamos el cliente con los cambios
        return new ClientDto(updatedClient);  // Devolvemos el cliente guardado convertido a DTO
    }
    //guarda un cliente en la base de datos.
    //Se utiliza para persistir los cambios realizados en un cliente utilizando el repositorio (clientRepository.save()).
    //Retorna un ClientDto para reflejar los datos del cliente después de ser guardado en la base de datos.
    @Override
    public boolean clientExistsById(Long id) {
        // Usa el método existsById de JpaRepository para verificar si el cliente existe
        return clientRepository.existsById(id);
    }

    @Override
    public Client findClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }
}
