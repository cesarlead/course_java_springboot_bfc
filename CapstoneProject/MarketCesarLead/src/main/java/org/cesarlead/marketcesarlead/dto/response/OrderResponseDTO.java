package org.cesarlead.marketcesarlead.dto.response;

import org.cesarlead.marketcesarlead.dto.OrderItemDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long orderId,
        String customerName,
        String customerEmail,
        LocalDateTime orderDate,
        List<OrderItemDTO> items,
        BigDecimal total
) {
}
