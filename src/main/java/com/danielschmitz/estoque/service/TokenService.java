package com.danielschmitz.estoque.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.danielschmitz.estoque.exception.InvalidTokenException;
import com.danielschmitz.estoque.model.User;

@Service
public class TokenService {

    private static final String STOCK_API_ISSUER_NAME = "stock-api";
    private static final Long STOCK_API_EXPIRATION_TIME_IN_HOURS = 2L;

    // Injeta o valor da nossa propriedade customizada (definiremos no application.properties)
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                .withIssuer(STOCK_API_ISSUER_NAME)
                .withSubject(user.getEmail())
                .withExpiresAt(generateExpirationDate())
                .sign(algorithm);

            return token;
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer(STOCK_API_ISSUER_NAME)
                .build()
                .verify(token)
                .getSubject();
        } catch (Exception e) {
           throw new InvalidTokenException("Invalid or expired token.");
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(STOCK_API_EXPIRATION_TIME_IN_HOURS).toInstant(ZoneOffset.of("-03:00"));
    }
}
