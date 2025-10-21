package org.cesarlead.marketcesarlead.controller;

import jakarta.validation.Valid;
import org.cesarlead.marketcesarlead.dto.ConvertedPriceDTO;
import org.cesarlead.marketcesarlead.dto.request.ProductRequestDTO;
import org.cesarlead.marketcesarlead.dto.response.ProductResponseDTO;
import org.cesarlead.marketcesarlead.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequest) {
        ProductResponseDTO createdProduct = productService.createProduct(productRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/price/{currency}")
    public Mono<ResponseEntity<ConvertedPriceDTO>> getProductPriceInCurrency(
            @PathVariable Long id,
            @PathVariable String currency) {
        return productService.getProductPriceInCurrency(id, currency)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
