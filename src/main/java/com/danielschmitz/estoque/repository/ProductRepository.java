package com.danielschmitz.estoque.repository;

import com.danielschmitz.estoque.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategory_Id(Long categoryId);
    List<Product> findByNameContainingIgnoreCaseAndCategory_Id(String name, Long categoryId);
    boolean existsByCodigoDeBarras(String codigoDeBarras);
}
