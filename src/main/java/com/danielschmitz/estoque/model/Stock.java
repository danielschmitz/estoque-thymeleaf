package com.danielschmitz.estoque.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate; // data de entrada

    @Column(name = "expiry_date")
    private LocalDate expiryDate; // data de validade (opcional)

    @Column(name = "price", precision = 19, scale = 2, nullable = false)
    private BigDecimal price; // preço unitário

    @Column(name = "quantity", precision = 19, scale = 3, nullable = false)
    private BigDecimal quantity; // quantidade movimentada

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 10)
    private StockMovementType movementType; // IN ou OUT
}
