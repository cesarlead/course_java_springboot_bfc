package org.cesarlead.productservice.service;

import org.cesarlead.productservice.dto.ConvertedPriceDTO;
import org.cesarlead.productservice.dto.ProductRequestDTO;
import org.cesarlead.productservice.dto.ProductResponseDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductService {

    List<ProductResponseDTO> getAllProducts();

    ProductResponseDTO getProductById(Long id);

    ProductResponseDTO createProduct(ProductRequestDTO request);

    void decrementStock(Long id, Integer quantity);

    Mono<ConvertedPriceDTO> getProductPriceInCurrency(Long id, String currency);
}
