package com.mindhub.homebanking.servicesSecurity;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service//Servicio parar crear una instancia de User en nuestra aplicacion.
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //retorna un UserDetails que es el usuario que queremos tener en el contextHolder.

        Client client = clientRepository.findByEmail(username);

        if(client == null){
            throw new UsernameNotFoundException(username);
        }

        return User //retorna un UserDetails que es el usuario que queremos tener en el contextHolder.
                .withUsername(username)
                .password(client.getPassword())
                .roles("CLIENT")
                .build();
        //una vez que lo tenemos ahi queremos crearle un token.
    }
}
