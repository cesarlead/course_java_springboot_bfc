package org.cesarlead.communication.dto;

public record ProductUpdateDTO(
        String name,
        String description,
        Double price
) {
}
