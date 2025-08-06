package com.danielschmitz.estoque.api.dto.authentication;

public record AuthenticationDTO(
    String email,
    String password
) {

}
