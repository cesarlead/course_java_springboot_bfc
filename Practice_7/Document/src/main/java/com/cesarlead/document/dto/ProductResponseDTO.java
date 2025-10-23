package com.cesarlead.document.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Represents a product returned by the API.")
public record ProductResponseDTO(

        @Schema(description = "Unique identifier for the product.", example = "1")
        Long id,

        @Schema(description = "Stock Keeping Unit.", example = "SKU-12345-XYZ")
        String sku,

        @Schema(description = "Product name.", example = "Laptop Pro 16-inch")
        String name,

        @Schema(description = "Product price in USD.", example = "2499.99")
        BigDecimal price
) {
}
