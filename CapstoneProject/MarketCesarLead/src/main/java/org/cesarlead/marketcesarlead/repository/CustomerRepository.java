package org.cesarlead.marketcesarlead.repository;

import org.cesarlead.marketcesarlead.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
