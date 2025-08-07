package com.danielschmitz.estoque.api.service;

import com.danielschmitz.estoque.api.model.User;
import com.danielschmitz.estoque.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        User newUser = new User(null, user.getName(), user.getEmail(), encodedPassword);
        return userRepository.save(user);
    }
}
