package org.cesarlead.orderservice.dto.response;

import java.math.BigDecimal;

// DTO para mostrar los items en la respuesta del pedido
public record OrderItemResponseDTO(
        Long productId,
        String productName, // Viene del snapshot
        Integer quantity,
        BigDecimal priceAtPurchase // Viene del snapshot
) {
}
