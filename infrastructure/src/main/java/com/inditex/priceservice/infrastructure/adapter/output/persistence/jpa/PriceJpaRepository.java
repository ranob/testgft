package com.inditex.priceservice.infrastructure.adapter.output.persistence.jpa;

import com.inditex.priceservice.infrastructure.adapter.output.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for PriceEntity.
 */
public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {
    @Query("SELECT p FROM PriceEntity p WHERE p.productId = :productId AND p.brandId = :brandId AND :applicationDate BETWEEN p.startDate AND p.endDate")
    List<PriceEntity> findApplicablePrices(
            @Param("productId") Long productId,
            @Param("brandId") Long brandId,
            @Param("applicationDate") LocalDateTime applicationDate
    );
}