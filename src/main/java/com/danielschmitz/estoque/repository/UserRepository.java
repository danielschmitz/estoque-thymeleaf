package com.danielschmitz.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danielschmitz.estoque.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
