package com.danielschmitz.estoque.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.danielschmitz.estoque.api.repository.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    /**
     * Este método é chamado pelo Spring Security quando um usuário tenta se autenticar.
     * Ele recebe o email (que estamos usando como username) e deve retornar os
     * detalhes do usuário correspondente.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return user;
    }

    

}
