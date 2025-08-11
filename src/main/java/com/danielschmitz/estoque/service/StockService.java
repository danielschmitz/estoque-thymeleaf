package com.danielschmitz.estoque.service;

import com.danielschmitz.estoque.model.Location;
import com.danielschmitz.estoque.model.Product;
import com.danielschmitz.estoque.model.Stock;
import com.danielschmitz.estoque.model.StockMovementType;
import com.danielschmitz.estoque.repository.LocationRepository;
import com.danielschmitz.estoque.repository.ProductRepository;
import com.danielschmitz.estoque.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public List<Stock> search(Long productId, Long locationId) {
        if (productId != null && locationId != null) {
            return stockRepository.findByProduct_IdAndLocation_Id(productId, locationId);
        } else if (productId != null) {
            return stockRepository.findByProduct_Id(productId);
        } else if (locationId != null) {
            return stockRepository.findByLocation_Id(locationId);
        }
        return findAll();
    }

    public BigDecimal getBalance(Long productId, Long locationId) {
        return stockRepository.getBalance(productId, locationId);
    }

    public Stock createMovement(Long productId,
                                Long locationId,
                                LocalDate entryDate,
                                LocalDate expiryDate,
                                BigDecimal price,
                                BigDecimal quantity,
                                StockMovementType movementType) {
        validate(productId, locationId, entryDate, price, quantity, movementType);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto inválido"));
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Local inválido"));

        // valida saldo para saída
        if (movementType == StockMovementType.OUT) {
            BigDecimal current = stockRepository.getBalanceForProductLocation(productId, locationId);
            if (current.subtract(quantity).compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Saldo insuficiente para saída. Saldo atual: " + current);
            }
        }

        Stock s = new Stock();
        s.setProduct(product);
        s.setLocation(location);
        s.setEntryDate(entryDate);
        s.setExpiryDate(expiryDate);
        s.setPrice(price);
        s.setQuantity(quantity);
        s.setMovementType(movementType);
        return stockRepository.save(s);
    }

    private void validate(Long productId,
                          Long locationId,
                          LocalDate entryDate,
                          BigDecimal price,
                          BigDecimal quantity,
                          StockMovementType movementType) {
        if (productId == null) throw new RuntimeException("Produto é obrigatório");
        if (locationId == null) throw new RuntimeException("Local é obrigatório");
        if (entryDate == null) throw new RuntimeException("Data de entrada é obrigatória");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) throw new RuntimeException("Preço deve ser positivo");
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException("Quantidade deve ser maior que zero");
        if (movementType == null) throw new RuntimeException("Tipo de movimento é obrigatório");
    }
}
