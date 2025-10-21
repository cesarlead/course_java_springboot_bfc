package org.cesarlead.marketcesarlead.repository;

import org.cesarlead.marketcesarlead.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
