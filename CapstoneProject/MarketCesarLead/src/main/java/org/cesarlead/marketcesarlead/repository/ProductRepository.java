package org.cesarlead.marketcesarlead.repository;

import org.cesarlead.marketcesarlead.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA genera la implementación de esta consulta automáticamente!
    Optional<Product> findByName(String name);
}
