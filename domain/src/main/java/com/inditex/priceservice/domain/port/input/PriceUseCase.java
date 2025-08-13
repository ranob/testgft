package com.inditex.priceservice.domain.port.input;

import com.inditex.priceservice.domain.model.Price;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Input Port for the Price domain.
 * Defines the use case for finding an applicable price.
 * This interface is implemented by the application layer.
 */
public interface PriceUseCase {
    /**
     * Finds the applicable price for a given product, brand, and date.
     *
     * @param productId The ID of the product.
     * @param brandId The ID of the brand.
     * @param applicationDate The date for which to find the price.
     * @return An Optional containing the applicable Price if found, otherwise empty.
     */
    Optional<Price> findApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate);
}
