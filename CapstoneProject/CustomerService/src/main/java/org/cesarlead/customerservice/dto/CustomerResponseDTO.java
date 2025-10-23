package org.cesarlead.customerservice.dto;

public record CustomerResponseDTO(
        Long id,
        String name,
        String email
) {
}
