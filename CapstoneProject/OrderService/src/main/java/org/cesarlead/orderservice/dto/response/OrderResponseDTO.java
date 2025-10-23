package org.cesarlead.orderservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long orderId,
        LocalDateTime orderDate,
        String customerName,
        String customerEmail,
        List<OrderItemResponseDTO> items,
        BigDecimal total
) {
}
