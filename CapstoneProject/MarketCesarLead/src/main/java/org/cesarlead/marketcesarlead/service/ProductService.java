package org.cesarlead.marketcesarlead.service;

import org.cesarlead.marketcesarlead.dto.ConvertedPriceDTO;
import org.cesarlead.marketcesarlead.dto.request.ProductRequestDTO;
import org.cesarlead.marketcesarlead.dto.response.ProductResponseDTO;
import org.cesarlead.marketcesarlead.exception.ResourceNotFoundException;
import org.cesarlead.marketcesarlead.model.Product;
import org.cesarlead.marketcesarlead.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CurrencyConversionService currencyConversionService;

    public ProductService(ProductRepository productRepository, CurrencyConversionService currencyConversionService) {
        this.productRepository = productRepository;
        this.currencyConversionService = currencyConversionService;
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        // Lógica de validación (e.g., no duplicar nombres) puede ir aquí
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());

        Product savedProduct = productRepository.save(product);

        return new ProductResponseDTO(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice());
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(p -> new ProductResponseDTO(p.getId(), p.getName(), p.getPrice()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productId));
    }

    @Transactional(readOnly = true)
    public Mono<ConvertedPriceDTO> getProductPriceInCurrency(Long productId, String currency) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        // Asumimos que la moneda base de nuestros precios es "USD"
        return currencyConversionService.getConversionRate("USD", currency)
                .map(rate -> {
                    BigDecimal convertedPrice = product.getPrice().multiply(rate);
                    return new ConvertedPriceDTO(productId, product.getName(), currency.toUpperCase(), convertedPrice);
                });
    }
}
