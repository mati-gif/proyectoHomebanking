package com.mindhub.homebanking.servicesSecurity;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service//Servicio parar crear una instancia de User en nuestra aplicacion. Marca la clase como un servicio de Spring. Esto significa que Spring va a gestionar la instancia de esta clase y la inyectará donde sea necesario.
public class UserDetailsServiceImpl implements UserDetailsService {//UserDetailsServiceImpl: Implementa la interfaz UserDetailsService, que es utilizada por Spring Security para cargar datos específicos del usuario en el proceso de autenticación.
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //retorna un UserDetails que es el usuario que queremos tener en el contextHolder.
        //loadUserByUsername(String username): Este método busca en la base de datos un usuario con el nombre de usuario (en este caso, el email) proporcionado.
        ////username se refiere a el nombre de usuario utilizado para autenticarse.
        Client client = clientRepository.findByEmail(username);

        if(client == null){
            throw new UsernameNotFoundException(username);
        }

        if(client.getEmail().contains("admin")){

            return User
                    .withUsername(username)
                    .password(client.getPassword())
                    .roles("ADMIN")
                    .build();

        }


        return User //retorna un UserDetails que es el usuario que queremos tener en el contextHolder.
                .withUsername(username)  // Crea un objeto UserDetails con el nombre de usuario proporcionado.
                .password(client.getPassword())  // Establece la contraseña del usuario (normalmente esta estaría cifrada).
                .roles("CLIENT")  //Asigna un rol al usuario. Aquí se asigna el rol CLIENT.
                .build(); //Construye y retorna la instancia de UserDetails creada.
        //una vez que lo tenemos ahi queremos crearle un token.

    }
}
