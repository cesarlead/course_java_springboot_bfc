package org.cesarlead.marketcesarlead.dto;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long productId,
        String productName,
        BigDecimal price
) {
}
