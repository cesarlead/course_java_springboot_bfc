package org.cesarlead.communication.controller;

import org.cesarlead.communication.dto.ProductDTO;
import org.cesarlead.communication.exeption.ProductNotFoundException;
import org.cesarlead.communication.exeption.ServiceUnavailableException;
import org.cesarlead.communication.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/resilient-facade/products")
public class ResilientFacadeController {

    private final ProductService productService;

    public ResilientFacadeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        // La llamada al servicio ahora puede lanzar nuestras excepciones personalizadas
        return ResponseEntity.ok(productService.findProductById(id));
    }

    // --- Manejadores de Excepciones Específicos para este Controlador ---

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(ProductNotFoundException ex) {
        return new ResponseEntity<>(Map.of("error", "Product was not found", "details", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleServiceUnavailable(ServiceUnavailableException ex) {
        return new ResponseEntity<>(Map.of("error", "External service is currently unavailable", "details", ex.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
