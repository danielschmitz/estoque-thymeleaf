package com.danielschmitz.estoque.api.config;

import com.danielschmitz.estoque.api.model.User;
import com.danielschmitz.estoque.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@email.com") == null){
            User admin = new  User(null, "admin", "admin@email.com", passwordEncoder.encode("123"));
            userRepository.save(admin);
            System.out.println("Usuário admin criado com sucesso");
        }
    }
}
