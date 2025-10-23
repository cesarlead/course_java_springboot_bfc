package org.cesarlead.productservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.cesarlead.productservice.dto.ConvertedPriceDTO;
import org.cesarlead.productservice.dto.ProductRequestDTO;
import org.cesarlead.productservice.dto.ProductResponseDTO;
import org.cesarlead.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Validated // Necesario para validar @RequestParam
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO request) {
        ProductResponseDTO createdProduct = productService.createProduct(request);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Endpoint para obtener info de un producto.
     * CRÍTICO para 'servicio-pedidos'.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Endpoint para decrementar stock.
     * CRÍTICO para 'servicio-pedidos'.
     */
    @PostMapping("/{id}/decrement-stock")
    public ResponseEntity<Void> decrementStock(
            @PathVariable Long id,
            @RequestParam @Min(value = 1, message = "La cantidad debe ser al menos 1") Integer quantity) {

        productService.decrementStock(id, quantity);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint público para conversión de moneda.
     */
    @GetMapping("/{id}/price/{currency}")
    public Mono<ResponseEntity<ConvertedPriceDTO>> getProductPriceInCurrency(
            @PathVariable Long id,
            @PathVariable String currency) {

        return productService.getProductPriceInCurrency(id, currency)
                .map(ResponseEntity::ok)

                // --- ! ESTA ES LA LÍNEA CLAVE DE DEBUGGING ---
                .doOnError(error ->
                        // Esto imprimirá el stack trace exacto en tu consola
                        logger.error("¡FALLO en el flujo de getProductPriceInCurrency!", error)
                )
                // ------------------------------------------------

                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
