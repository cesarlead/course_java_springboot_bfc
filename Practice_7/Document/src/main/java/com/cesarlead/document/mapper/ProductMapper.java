package com.cesarlead.document.mapper;

import com.cesarlead.document.dto.ProductRequestDTO;
import com.cesarlead.document.dto.ProductResponseDTO;
import com.cesarlead.document.model.Product;

public interface ProductMapper {

    ProductResponseDTO toResponseDTO(Product product);

    Product toEntity(ProductRequestDTO requestDTO);

    void updateEntityFromDTO(ProductRequestDTO requestDTO, Product entity);
}
