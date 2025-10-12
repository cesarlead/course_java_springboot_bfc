package org.cesarlead.communication.dto;

public record ProductDTO(
        Long id,
        String name,
        String sku,
        double price
) {

}
