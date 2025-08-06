package com.danielschmitz.estoque.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danielschmitz.estoque.api.dto.authentication.AuthenticationDTO;
import com.danielschmitz.estoque.api.dto.authentication.LoginResponseDTO;
import com.danielschmitz.estoque.api.dto.authentication.RegisterDTO;
import com.danielschmitz.estoque.api.model.User;
import com.danielschmitz.estoque.api.repository.UserRepository;
import com.danielschmitz.estoque.api.service.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        if (this.userRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }
        String encodedPassword = this.passwordEncoder.encode(data.password());
        User newUser = new User(null, data.name(), data.email(), encodedPassword);
        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }

}
