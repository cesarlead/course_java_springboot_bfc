package org.cesarlead.productservice.dto;

import java.math.BigDecimal;

public record ConvertedPriceDTO(
        Long productId,
        String productName,
        String currency,
        BigDecimal convertedPrice
) {
}
