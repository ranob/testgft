package com.inditex.priceservice.domain.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Represents the core Price domain object.
 * This class is framework-agnostic and contains only business data.
 */
@Data
@Builder
public class Price {
    private Long brandId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer priceList;
    private Long productId;
    private Integer priority;
    private Double priceValue;
    private String currency;
}
