package com.danielschmitz.estoque.repository;

import com.danielschmitz.estoque.model.Product;
import com.danielschmitz.estoque.model.dto.CategoryProductCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategory_Id(Long categoryId);
    List<Product> findByNameContainingIgnoreCaseAndCategory_Id(String name, Long categoryId);
    boolean existsByCodigoDeBarras(String codigoDeBarras);
    @Query("SELECT new com.danielschmitz.estoque.model.dto.CategoryProductCountDTO(p.category.name, COUNT(p)) " +
            "FROM Product p GROUP BY p.category ORDER BY p.category.id")
    List<CategoryProductCountDTO> countProductsByCategory();
}
