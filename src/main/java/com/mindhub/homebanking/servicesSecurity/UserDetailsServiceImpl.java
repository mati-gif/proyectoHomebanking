//package com.mindhub.homebanking.servicesSecurity;
//
//import com.mindhub.homebanking.models.Cliente;
//import com.mindhub.homebanking.repositories.ClientRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//public class UserDetailsServiceImpl implements UserDetailsService {
//    @Autowired
//    private ClientRepository clientRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //retorna un UserDetails que es el usuario que queremos tener en el contextHolder
//
//        Cliente cliente = clientRepository.findByEmail(username);
//
//        if(cliente == null){
//            throw new UsernameNotFoundException(username);
//        }
//
//        return User
//                .withUsername(username)
////                .password(cliente.getPassword())
//                .roles("CLIENT")
//                .build();
//    }
//}
