package com.inditex.priceservice.infrastructure.adapter.input.web;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.port.input.PriceUseCase;
import com.inditex.priceservice.infrastructure.adapter.input.web.dto.PriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

/**
 * REST Controller (Input Adapter) that exposes the price-finding functionality.
 */
@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceUseCase priceUseCase;

    @GetMapping("/applicable")
    public ResponseEntity<PriceResponse> getApplicablePrice(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam Long productId,
            @RequestParam Long brandId) {
        return priceUseCase.findApplicablePrice(productId, brandId, applicationDate)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private PriceResponse toResponse(Price price) {
        return new PriceResponse(
                price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPriceValue()
        );
    }
}

