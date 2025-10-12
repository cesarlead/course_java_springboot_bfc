package org.cesarlead.communication.client;

import org.cesarlead.communication.dto.ProductCreationDTO;
import org.cesarlead.communication.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "${product-service.url}")
public interface ProductFeignClient {

    @GetMapping("/products")
    List<ProductDTO> getAllProducts();

    @GetMapping("/products/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);

    @PostMapping("/products")
    ProductDTO createProduct(@RequestBody ProductCreationDTO newProduct);
}
