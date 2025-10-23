package com.cesarlead.document.mapper;

import com.cesarlead.document.dto.ProductRequestDTO;
import com.cesarlead.document.dto.ProductResponseDTO;
import com.cesarlead.document.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements ProductMapper {
    @Override
    public ProductResponseDTO toResponseDTO(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductResponseDTO(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getPrice()
        );
    }

    @Override
    public Product toEntity(ProductRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        return new Product(
                requestDTO.sku(),
                requestDTO.name(),
                requestDTO.price(),
                requestDTO.stock()
        );

    }

    @Override
    public void updateEntityFromDTO(ProductRequestDTO requestDTO, Product entity) {
        if (requestDTO == null || entity == null) {
            return;
        }

        // Update fields on the existing, managed entity
        entity.setSku(requestDTO.sku());
        entity.setName(requestDTO.name());
        entity.setPrice(requestDTO.price());
        entity.setStock(requestDTO.stock());
    }
}
