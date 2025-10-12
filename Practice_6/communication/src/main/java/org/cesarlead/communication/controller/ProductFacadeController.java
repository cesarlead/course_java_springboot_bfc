package org.cesarlead.communication.controller;

import org.cesarlead.communication.dto.ProductCreationDTO;
import org.cesarlead.communication.dto.ProductDTO;
import org.cesarlead.communication.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/facade/products")
public class ProductFacadeController {

    private final ProductService productService;

    public ProductFacadeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.findProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreationDTO creationDTO) {
        ProductDTO createdProduct = productService.createNewProduct(creationDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
}
