package org.cesarlead.marketcesarlead.config;

import org.cesarlead.marketcesarlead.model.Customer;
import org.cesarlead.marketcesarlead.model.Product;
import org.cesarlead.marketcesarlead.repository.CustomerRepository;
import org.cesarlead.marketcesarlead.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public DataInitializer(ProductRepository productRepository, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Precargar productos
        Product p1 = new Product(null, "Laptop Pro", new BigDecimal("1499.99"), 15);
        Product p2 = new Product(null, "Teclado Mecánico", new BigDecimal("120.00"), 30);
        productRepository.saveAll(Arrays.asList(p1, p2));

        // Precargar clientes
        Customer c1 = new Customer(null, "César Fernández", "cesar.fernandez@example.com");
        customerRepository.save(c1);

        System.out.println("---- DATOS DE PRUEBA CARGADOS ----");
    }
}
