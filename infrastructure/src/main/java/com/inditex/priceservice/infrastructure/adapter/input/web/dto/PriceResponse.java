package com.inditex.priceservice.infrastructure.adapter.input.web.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for the API response.
 * Using a Java Record for immutability and conciseness.
 */
public record PriceResponse(
    Long productId,
    Long brandId,
    Integer priceList,
    LocalDateTime startDate,
    LocalDateTime endDate,
    Double finalPrice
) {}
