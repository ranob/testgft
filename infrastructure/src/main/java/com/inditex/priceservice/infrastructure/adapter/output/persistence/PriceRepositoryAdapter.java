package com.inditex.priceservice.infrastructure.adapter.output.persistence;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.port.output.PriceRepositoryPort;
import com.inditex.priceservice.infrastructure.adapter.output.persistence.entity.PriceEntity;
import com.inditex.priceservice.infrastructure.adapter.output.persistence.jpa.PriceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Persistence Adapter (Output Adapter) that implements the PriceRepositoryPort.
 * It connects the domain to the persistence technology (JPA).
 */
@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {
    private final PriceJpaRepository priceJpaRepository;

    @Override
    public List<Price> findApplicablePrices(Long productId, Long brandId, LocalDateTime applicationDate) {
        List<PriceEntity> entities = priceJpaRepository.findApplicablePrices(productId, brandId, applicationDate);
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

    private Price toDomain(PriceEntity entity) {
        return Price.builder()
                .productId(entity.getProductId())
                .brandId(entity.getBrandId())
                .priceList(entity.getPriceList())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .priceValue(entity.getPriceValue())
                .priority(entity.getPriority())
                .currency(entity.getCurrency())
                .build();
    }
}

