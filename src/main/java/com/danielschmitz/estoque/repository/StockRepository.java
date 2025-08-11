package com.danielschmitz.estoque.repository;

import com.danielschmitz.estoque.model.Stock;
import com.danielschmitz.estoque.model.StockMovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByProduct_Id(Long productId);
    List<Stock> findByLocation_Id(Long locationId);
    List<Stock> findByProduct_IdAndLocation_Id(Long productId, Long locationId);

    @Query("select coalesce(sum(case when s.movementType = com.danielschmitz.estoque.model.StockMovementType.IN then s.quantity else -s.quantity end), 0) " +
           "from Stock s where (:productId is null or s.product.id = :productId) and (:locationId is null or s.location.id = :locationId)")
    BigDecimal getBalance(@Param("productId") Long productId, @Param("locationId") Long locationId);

    @Query("select coalesce(sum(case when s.movementType = com.danielschmitz.estoque.model.StockMovementType.IN then s.quantity else -s.quantity end), 0) " +
           "from Stock s where s.product.id = :productId and s.location.id = :locationId")
    BigDecimal getBalanceForProductLocation(@Param("productId") Long productId, @Param("locationId") Long locationId);
}
