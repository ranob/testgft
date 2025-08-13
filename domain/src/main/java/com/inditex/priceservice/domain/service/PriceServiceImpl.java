package com.inditex.priceservice.domain.service;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.port.output.PriceRepositoryPort;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

/**
 * Contains the core business logic for the Price domain.
 * This is a pure, framework-agnostic class orchestrated by the application layer.
 */
public class PriceServiceImpl {

    private final PriceRepositoryPort priceRepositoryPort;

    public PriceServiceImpl(PriceRepositoryPort priceRepositoryPort) {
        this.priceRepositoryPort = priceRepositoryPort;
    }

    /**
     * Implements the business rule for finding the correct price.
     * It fetches all applicable prices and then applies the priority rule.
     *
     * @param productId The ID of the product.
     * @param brandId The ID of the brand.
     * @param applicationDate The date for which to find the price.
     * @return An Optional containing the highest-priority Price if found, otherwise empty.
     */
    public Optional<Price> findApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate) {
        // Step 1: Fetch all potentially applicable prices from the persistence layer.
        var applicablePrices = priceRepositoryPort.findApplicablePrices(productId, brandId, applicationDate);

        // Step 2: Apply the business rule: find the one with the highest priority.
        return applicablePrices.stream()
                .max(Comparator.comparing(Price::getPriority));
    }
}
