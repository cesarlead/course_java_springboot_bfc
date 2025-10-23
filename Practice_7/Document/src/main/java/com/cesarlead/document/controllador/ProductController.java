package com.cesarlead.document.controllador;

import com.cesarlead.document.dto.ProductRequestDTO;
import com.cesarlead.document.dto.ProductResponseDTO;
import com.cesarlead.document.exception.ErrorResponseDTO;
import com.cesarlead.document.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "API for creating, reading, updating, and deleting products.")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get a product by its ID",
            description = "Fetches complete details for a specific product using its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(description = "Unique ID of the product to retrieve", example = "1")
            @PathVariable Long id
    ) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Get all products",
            description = "Retrieves a list of all products in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Create a new product",
            description = "Adds a new product to the system. The SKU must be unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate SKU",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO requestDTO
    ) {
        ProductResponseDTO createdProduct = productService.createProduct(requestDTO);
        URI location = URI.create(String.format("/api/v1/products/%d", createdProduct.id()));
        return ResponseEntity.created(location).body(createdProduct);
    }

    @Operation(summary = "Update an existing product",
            description = "Updates the details of an existing product identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate SKU",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(description = "Unique ID of the product to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO
    ) {
        ProductResponseDTO updatedProduct = productService.updateProduct(id, requestDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Delete a product",
            description = "Deletes a product from the system using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Unique ID of the product to delete", example = "1")
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


//    @GetMapping
//    public List<TaskDto> getAllTasks(
//            @Parameter(
//                    in = ParameterIn.QUERY,
//                    name = "status",
//                    description = "Filtrar tareas por su estado (e.g., PENDING, COMPLETED)",
//                    example = "PENDING"
//            )
//            @RequestParam(required = false) String status
//    ) {
//        //...
//    }
//
//    @GetMapping("/with-header")
//    public ResponseEntity<String> getWithCustomHeader(
//            @Parameter(
//                    in = ParameterIn.HEADER,
//                    name = "X-Request-ID",
//                    description = "ID de correlación para la trazabilidad de la solicitud",
//                    required = true
//            )
//            @RequestHeader("X-Request-ID") String requestId
//    ) {
//        //...
//    }

}
