package org.cesarlead.persistencia.service;

import jakarta.transaction.Transactional;
import org.cesarlead.persistencia.model.Order;
import org.cesarlead.persistencia.model.OrderItem;
import org.cesarlead.persistencia.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    public OrderServiceImpl(OrderRepository orderRepository, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }

    @Override
    @Transactional
    public Order placeOrder(Order order) {
        // Lógica de negocio: Validar la orden, calcular totales, etc.
        Order savedOrder = orderRepository.save(order);

        // Actualizar el inventario por cada ítem en la orden
        for (OrderItem item : savedOrder.getItems()) {
            inventoryService.updateStock(item.getProductId(), item.getQuantity());
        }

        return savedOrder;
    }
}
