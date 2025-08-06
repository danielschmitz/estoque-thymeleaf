package com.danielschmitz.estoque.api.dto.authentication;

public record RegisterDTO(
    String name,
    String email,
    String password
) {

}
