package com.cesarlead.document.config;

import com.cesarlead.document.model.Product;
import com.cesarlead.document.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            log.info("No products found. Seeding database...");

            Product p1 = new Product(
                    "SKU-A123",
                    "Laptop Pro 16-inch",
                    new BigDecimal("2399.99"),
                    50
            );

            Product p2 = new Product(
                    "SKU-B456",
                    "Mouse Inalámbrico Pro",
                    new BigDecimal("129.50"),
                    200
            );

            Product p3 = new Product(
                    "SKU-C789",
                    "Teclado Mecánico RGB",
                    new BigDecimal("189.90"),
                    120
            );

            productRepository.saveAll(List.of(p1, p2, p3));
            log.info("Database seeded with {} products.", productRepository.count());

        } else {
            log.info("Database already contains data. Skipping seeding.");
        }
    }
}
