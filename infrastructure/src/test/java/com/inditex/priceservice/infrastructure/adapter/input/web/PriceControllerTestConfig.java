package com.inditex.priceservice.infrastructure.adapter.input.web;

import com.inditex.priceservice.domain.port.input.PriceUseCase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import static org.mockito.Mockito.mock;

/**
 * Test-specific configuration for PriceControllerTest.
 * This class defines a mock bean for PriceUseCase, which will replace the real one
 * in the test context.
 */
@TestConfiguration
public class PriceControllerTestConfig {

    @Bean
    @Primary // Ensures this mock bean takes precedence over any real implementation
    public PriceUseCase priceUseCase() {
        // Creates and returns a mock of the PriceUseCase interface
        return mock(PriceUseCase.class);
    }
}

