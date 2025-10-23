package com.cesarlead.document.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Data required to create or update a product. SKU must be unique.")
public record ProductRequestDTO(

        @Schema(description = "Stock Keeping Unit (SKU). Must be unique.",
                example = "SKU-12345-XYZ",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(min = 5, max = 50)
        String sku,

        @Schema(description = "Product name.",
                example = "Laptop Pro 16-inch",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(min = 3, max = 100)
        String name,

        @Schema(description = "Product price in USD.",
                example = "2499.99",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @Positive
        BigDecimal price,

        @Schema(description = "Available stock quantity.",
                example = "150",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @Positive
        Integer stock
) {
}
