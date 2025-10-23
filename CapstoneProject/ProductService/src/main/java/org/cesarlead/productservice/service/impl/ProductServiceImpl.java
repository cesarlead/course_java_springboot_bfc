package org.cesarlead.productservice.service.impl;

import org.cesarlead.productservice.dto.ConvertedPriceDTO;
import org.cesarlead.productservice.dto.ProductRequestDTO;
import org.cesarlead.productservice.dto.ProductResponseDTO;
import org.cesarlead.productservice.exception.DuplicateResourceException;
import org.cesarlead.productservice.exception.InsufficientStockException;
import org.cesarlead.productservice.exception.ResourceNotFoundException;
import org.cesarlead.productservice.model.Product;
import org.cesarlead.productservice.repository.ProductRepository;
import org.cesarlead.productservice.service.CurrencyConversionService;
import org.cesarlead.productservice.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CurrencyConversionService currencyConversionService;

    // Asumimos que la moneda base de la tienda es USD
    private static final String BASE_CURRENCY = "USD";

    public ProductServiceImpl(ProductRepository productRepository, CurrencyConversionService currencyConversionService) {
        this.productRepository = productRepository;
        this.currencyConversionService = currencyConversionService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = findProductEntityById(id);
        return mapToProductResponseDTO(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        // Validación de duplicados
        productRepository.findByName(request.name()).ifPresent(p -> {
            throw new DuplicateResourceException("Un producto con el nombre '" + request.name() + "' ya existe.");
        });

        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());

        Product savedProduct = productRepository.save(product);
        return mapToProductResponseDTO(savedProduct);
    }

    @Override
    @Transactional // Esta operación debe ser atómica
    public void decrementStock(Long id, Integer quantity) {
        Product product = findProductEntityById(id);

        if (product.getStock() < quantity) {
            throw new InsufficientStockException("Stock insuficiente para " + product.getName() +
                    ". Solicitados: " + quantity + ", Disponibles: " + product.getStock());
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product); // JPA actualiza la entidad
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ConvertedPriceDTO> getProductPriceInCurrency(Long id, String currency) {
        // 1. Obtener el producto (síncrono, desde nuestra DB)
        Product product = findProductEntityById(id);

        // 2. Obtener la tasa de cambio (asíncrono, desde la API externa)
        return currencyConversionService.getConversionRate(BASE_CURRENCY, currency)
                .map(rate -> {
                    // 3. Calcular y mapear el DTO
                    BigDecimal convertedPrice = product.getPrice()
                            .multiply(rate)
                            .setScale(2, RoundingMode.HALF_UP);

                    return new ConvertedPriceDTO(
                            product.getId(),
                            product.getName(),
                            currency.toUpperCase(),
                            convertedPrice
                    );
                });
    }

    // --- Métodos de Ayuda Privados (DRY) ---

    private Product findProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    private ProductResponseDTO mapToProductResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }
}
