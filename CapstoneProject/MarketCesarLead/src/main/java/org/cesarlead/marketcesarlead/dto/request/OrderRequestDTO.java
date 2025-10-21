package org.cesarlead.marketcesarlead.dto.request;

import java.util.List;

public record OrderRequestDTO(
        Long customerId,
        List<Long> productIds
) {
}
