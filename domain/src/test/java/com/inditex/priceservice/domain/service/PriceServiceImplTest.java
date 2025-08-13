package com.inditex.priceservice.domain.service;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.port.output.PriceRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PriceServiceImpl class.
 * These tests are pure Java and do not require the Spring context.
 */
class PriceServiceImplTest {

    private PriceServiceImpl priceService;
    private PriceRepositoryPort priceRepositoryPort;

    @Test
    @DisplayName("Should return highest priority price when multiple prices are applicable")
    void givenMultiplePrices_whenFindApplicablePrice_thenReturnsHighestPriority() {
        // Arrange: Create a stub for the repository port that returns a predefined list of prices.
        priceRepositoryPort = (pid, bid, date) -> List.of(
                Price.builder().productId(35455L).brandId(1L).priceList(1).priority(0).priceValue(35.50).build(), // Lower priority
                Price.builder().productId(35455L).brandId(1L).priceList(2).priority(1).priceValue(25.45).build()  // Higher priority
        );
        priceService = new PriceServiceImpl(priceRepositoryPort);

        // Act
        Optional<Price> result = priceService.findApplicablePrice(35455L, 1L, LocalDateTime.now());

        // Assert
        assertTrue(result.isPresent(), "An applicable price should have been found.");
        assertEquals(1, result.get().getPriority(), "The price with the highest priority should be selected.");
        assertEquals(25.45, result.get().getPriceValue(), "The price value should match the highest priority entry.");
        assertEquals(2, result.get().getPriceList(), "The price list should match the highest priority entry.");
    }

    @Test
    @DisplayName("Should return empty optional when no prices are applicable")
    void givenNoPrices_whenFindApplicablePrice_thenReturnsEmpty() {
        // Arrange: Create a stub that returns an empty list.
        priceRepositoryPort = (pid, bid, date) -> Collections.emptyList();
        priceService = new PriceServiceImpl(priceRepositoryPort);

        // Act
        Optional<Price> result = priceService.findApplicablePrice(99999L, 1L, LocalDateTime.now());

        // Assert
        assertFalse(result.isPresent(), "No price should be found when the repository returns an empty list.");
    }

    @Test
    @DisplayName("Should return the only price when just one is applicable")
    void givenSinglePrice_whenFindApplicablePrice_thenReturnsThatPrice() {
        // Arrange
        Price singlePrice = Price.builder().productId(35455L).brandId(1L).priceList(4).priority(1).priceValue(38.95).build();
        priceRepositoryPort = (pid, bid, date) -> List.of(singlePrice);
        priceService = new PriceServiceImpl(priceRepositoryPort);

        // Act
        Optional<Price> result = priceService.findApplicablePrice(35455L, 1L, LocalDateTime.now());

        // Assert
        assertTrue(result.isPresent(), "The single applicable price should be found.");
        assertEquals(singlePrice, result.get(), "The returned price should be the only one available.");
    }
}
