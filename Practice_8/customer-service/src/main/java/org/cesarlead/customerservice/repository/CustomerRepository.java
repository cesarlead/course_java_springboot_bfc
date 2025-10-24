package org.cesarlead.customerservice.repository;

import org.cesarlead.customerservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Spring Data JPA proveerá la implementación de findById, save, etc.
}
