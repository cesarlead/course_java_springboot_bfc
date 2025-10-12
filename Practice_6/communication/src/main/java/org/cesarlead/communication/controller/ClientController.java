package org.cesarlead.communication.controller;

import org.cesarlead.communication.dto.ProductCreationDTO;
import org.cesarlead.communication.dto.ProductDTO;
import org.cesarlead.communication.dto.ProductUpdateDTO;
import org.cesarlead.communication.dto.UserDTO;
import org.cesarlead.communication.service.ApiClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ApiClientService apiClientService;

    public ClientController(ApiClientService apiClientService) {
        this.apiClientService = apiClientService;
    }

    @GetMapping("/users/{id}")
    public Mono<UserDTO> getUser(@PathVariable Long id) {
        return apiClientService.getUserById(id);
    }

    @GetMapping("/users")
    public Flux<UserDTO> getUsers(@RequestParam(required = false) String role) {
        return apiClientService.getAllUsers(role);
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductDTO> createNewProduct(@RequestBody ProductCreationDTO newProduct) {
        return apiClientService.createProduct(newProduct);
    }

    @PutMapping("/products/{id}")
    public Mono<ProductDTO> updateExistingProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO productUpdate) {
        return apiClientService.updateProduct(id, productUpdate);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteExistingProduct(@PathVariable Long id) {
        return apiClientService.deleteProduct(id);
    }
}