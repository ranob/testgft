package com.inditex.priceservice.application.service;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.port.input.PriceUseCase;
import com.inditex.priceservice.domain.port.output.PriceRepositoryPort;
import com.inditex.priceservice.domain.service.PriceServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Application Service that implements the PriceUseCase input port.
 * This class acts as an orchestrator, handling transactional boundaries
 * and delegating the core business logic to the domain service.
 * It is a Spring-managed bean.
 */
@Service
public class PriceApplicationService implements PriceUseCase {

    private final PriceServiceImpl domainService;

    /**
     * Constructs the application service.
     * It receives the repository port to instantiate the pure domain service.
     * @param priceRepositoryPort The persistence port implementation.
     */
    public PriceApplicationService(PriceRepositoryPort priceRepositoryPort) {
        this.domainService = new PriceServiceImpl(priceRepositoryPort);
    }

    /**
     * {@inheritDoc}
     * This implementation is wrapped in a read-only transaction.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Price> findApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate) {
        // Delegate the call to the pure domain service
        return domainService.findApplicablePrice(productId, brandId, applicationDate);
    }
}
