package com.danielschmitz.estoque.config;

import com.danielschmitz.estoque.model.User;
import com.danielschmitz.estoque.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@email.com") == null){
            User admin = new  User(null, "admin", "admin@email.com", passwordEncoder.encode("123"));
            userRepository.save(admin);
            logger.info("Usuário admin criado com sucesso");
        }
    }
}
