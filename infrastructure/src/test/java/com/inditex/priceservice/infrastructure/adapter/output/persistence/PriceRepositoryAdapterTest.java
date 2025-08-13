package com.inditex.priceservice.infrastructure.adapter.output.persistence;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.infrastructure.adapter.output.persistence.entity.PriceEntity;
import com.inditex.priceservice.infrastructure.adapter.output.persistence.jpa.PriceJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest(properties = { "spring.sql.init.mode=never" }) // Disable automatic execution of data.sql for this test
@Import(PriceRepositoryAdapter.class) 
class PriceRepositoryAdapterTest {

    @Autowired
    private PriceRepositoryAdapter priceRepositoryAdapter;

    @Autowired
    private PriceJpaRepository priceJpaRepository; // Used to set up test data

    @Test
    @DisplayName("Should find applicable price and map entity to domain model correctly")
    void givenPriceExists_whenFindApplicablePrices_thenReturnsCorrectlyMappedPrice() {
        // Initialize data for the test
        PriceEntity entity = new PriceEntity();
        entity.setBrandId(1L);
        entity.setProductId(35455L);
        entity.setPriceList(1);
        entity.setPriority(0);
        entity.setPriceValue(99.99);
        entity.setCurrency("EUR");
        entity.setStartDate(LocalDateTime.parse("2020-01-01T00:00:00"));
        entity.setEndDate(LocalDateTime.parse("2020-12-31T23:59:59"));
        priceJpaRepository.save(entity);

        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14T10:00:00");

        // Launch the method under test
        List<Price> result = priceRepositoryAdapter.findApplicablePrices(35455L, 1L, applicationDate);

        // Assertss
        assertFalse(result.isEmpty(), "The result list should not be empty.");
        assertEquals(1, result.size(), "Exactly one price should be found.");

        Price foundPrice = result.get(0);
        assertEquals(entity.getProductId(), foundPrice.getProductId());
        assertEquals(entity.getBrandId(), foundPrice.getBrandId());
        assertEquals(entity.getPriceValue(), foundPrice.getPriceValue());
        assertEquals(entity.getPriority(), foundPrice.getPriority());
    }

    @Test
    @DisplayName("Should return an empty list when no applicable price is found")
    void givenNoPriceExists_whenFindApplicablePrices_thenReturnsEmptyList() {
        // Arrange: The database is empty by default in each test

        LocalDateTime applicationDate = LocalDateTime.parse("2025-01-01T10:00:00");

        // Act
        List<Price> result = priceRepositoryAdapter.findApplicablePrices(99999L, 9L, applicationDate);

        // Assert
        assertTrue(result.isEmpty(), "The result list should be empty when no prices match.");
    }
}
