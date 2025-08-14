package com.inditex.priceservice.application.service;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.port.output.PriceRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the PriceApplicationService.
 * This test uses Mockito to mock the repository port and verify
 * that the application service correctly delegates the call to the domain service.
 */
@ExtendWith(MockitoExtension.class)
class PriceApplicationServiceTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    private PriceApplicationService priceApplicationService;

    @BeforeEach
    void setUp() {
        // We manually instantiate the service to test its construction,
        // which in turn instantiates the pure domain service.
        priceApplicationService = new PriceApplicationService(priceRepositoryPort);
    }

    @Test
    @DisplayName("Should delegate call to domain service and return its result")
    void givenValidRequest_whenFindApplicablePrice_thenDelegatesToDomain() {
        // Arrange
        LocalDateTime testDate = LocalDateTime.now();
        Long productId = 35455L;
        Long brandId = 1L;
        Price expectedPrice = Price.builder().priority(1).priceValue(99.99).build();

        // Configure the mock to return a specific result when called
        when(priceRepositoryPort.findApplicablePrices(productId, brandId, testDate))
                .thenReturn(List.of(expectedPrice));

        // Act
        Optional<Price> result = priceApplicationService.findApplicablePrice(productId, brandId, testDate);

        // Assert
        assertTrue(result.isPresent(), "The result should not be empty.");
        assertEquals(expectedPrice, result.get(), "The returned price should be the one from the domain logic.");

        // Verify that the repository port was indeed called, confirming the delegation.
        verify(priceRepositoryPort).findApplicablePrices(productId, brandId, testDate);
    }
}

