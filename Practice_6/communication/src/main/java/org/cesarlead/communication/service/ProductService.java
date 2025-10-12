package org.cesarlead.communication.service;

import org.cesarlead.communication.client.ProductFeignClient;
import org.cesarlead.communication.dto.ProductCreationDTO;
import org.cesarlead.communication.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductFeignClient productFeignClient;

    public ProductService(ProductFeignClient productFeignClient) {
        this.productFeignClient = productFeignClient;
    }

    public List<ProductDTO> findAllProducts() {
        log.info("Solicitando todos los productos");
        return productFeignClient.getAllProducts();
    }

    public ProductDTO findProductById(Long id) {
        log.info("Solicitando producto con ID: {}", id);
        return productFeignClient.getProductById(id);
    }

    public ProductDTO createNewProduct(ProductCreationDTO creationDTO) {
        log.info("Creando nuevo producto: {}", creationDTO.name());
        // Aquí podríamos añadir validaciones antes de enviar la petición.
        return productFeignClient.createProduct(creationDTO);
    }
}
