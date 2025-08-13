package com.inditex.priceservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * JPA Entity representing the PRICES table.
 * This is an infrastructure detail, separate from the domain model.
 */
@Entity
@Table(name = "PRICES")
@Data
public class PriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BRAND_ID")
    private Long brandId;

    @Column(name = "START_DATE")
    private LocalDateTime startDate;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @Column(name = "PRICE_LIST")
    private Integer priceList;

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "PRICE_VALUE")
    private Double priceValue;

    @Column(name = "CURR")
    private String currency;
}

