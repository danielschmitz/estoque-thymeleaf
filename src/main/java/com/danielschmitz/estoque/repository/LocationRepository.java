package com.danielschmitz.estoque.repository;

import com.danielschmitz.estoque.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByNameIgnoreCase(String name);
}
