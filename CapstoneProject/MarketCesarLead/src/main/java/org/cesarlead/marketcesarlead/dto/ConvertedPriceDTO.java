package org.cesarlead.marketcesarlead.dto;

import java.math.BigDecimal;

public record ConvertedPriceDTO(
        Long productId,
        String productName,
        String currency,
        BigDecimal convertedPrice
) {
}
