package com.cesarlead.document.service;

import com.cesarlead.document.dto.ProductRequestDTO;
import com.cesarlead.document.dto.ProductResponseDTO;
import com.cesarlead.document.exception.DuplicateResourceException;
import com.cesarlead.document.exception.ResourceNotFoundException;
import com.cesarlead.document.mapper.ProductMapper;
import com.cesarlead.document.model.Product;
import com.cesarlead.document.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper; // <-- NUEVA INYECCIÓN

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        if (productRepository.existsBySku(requestDTO.sku())) {
            throw new DuplicateResourceException("Product with SKU " + requestDTO.sku() + " already exists.");
        }

        // Delegamos la creación de la entidad al mapper
        Product product = productMapper.toEntity(requestDTO);
        Product savedProduct = productRepository.save(product);

        // Delegamos la conversión de respuesta al mapper
        return productMapper.toResponseDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = findProductById(id);
        return productMapper.toResponseDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponseDTO) // Uso de referencia de método, muy limpio
                .toList();
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        Product existingProduct = findProductById(id);

        if (!existingProduct.getSku().equals(requestDTO.sku())) {
            if (productRepository.existsBySku(requestDTO.sku())) {
                throw new DuplicateResourceException("Product with SKU " + requestDTO.sku() + " already exists.");
            }
        }

        // Delegamos la actualización de campos al mapper
        // Esto modifica la entidad "existingProduct" en memoria
        productMapper.updateEntityFromDTO(requestDTO, existingProduct);

        // Guardamos la entidad ya actualizada
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toResponseDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product with id " + id + " not found.");
        }
        productRepository.deleteById(id);
    }

    // El método privado de búsqueda sigue siendo responsabilidad del servicio
    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found."));
    }
}
