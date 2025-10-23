package org.cesarlead.orderservice.dto.client;

public record CustomerDTO(
        Long id,
        String name,
        String email
) {
}
