package com.inditex.priceservice.domain.port.output;

import com.inditex.priceservice.domain.model.Price;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Output Port for the Price domain.
 * Defines the contract for data persistence operations needed by the domain.
 * This interface is implemented by a persistence adapter in the infrastructure layer.
 */
public interface PriceRepositoryPort {
    /**
     * Finds all prices that match the given criteria from the data source.
     *
     * @param productId The ID of the product.
     * @param brandId The ID of the brand.
     * @param applicationDate The date that must be within the price's start and end dates.
     * @return A list of matching Price domain objects.
     */
    List<Price> findApplicablePrices(Long productId, Long brandId, LocalDateTime applicationDate);
}
