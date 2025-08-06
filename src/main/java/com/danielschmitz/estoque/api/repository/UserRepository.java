package com.danielschmitz.estoque.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danielschmitz.estoque.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
