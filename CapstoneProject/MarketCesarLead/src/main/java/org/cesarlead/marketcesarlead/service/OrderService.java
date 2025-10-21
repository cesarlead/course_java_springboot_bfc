package org.cesarlead.marketcesarlead.service;

import org.cesarlead.marketcesarlead.dto.OrderItemDTO;
import org.cesarlead.marketcesarlead.dto.request.OrderRequestDTO;
import org.cesarlead.marketcesarlead.dto.response.OrderResponseDTO;
import org.cesarlead.marketcesarlead.exception.InsufficientStockException;
import org.cesarlead.marketcesarlead.model.Customer;
import org.cesarlead.marketcesarlead.model.Order;
import org.cesarlead.marketcesarlead.model.Product;
import org.cesarlead.marketcesarlead.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    public OrderService(OrderRepository orderRepository, ProductService productService, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.customerService = customerService;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        // 1. Hablar con CustomerService para validar y obtener el cliente
        Customer customer = customerService.findCustomerById(request.customerId());

        Order order = new Order();
        order.setCustomer(customer);

        List<Product> productsForOrder = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // 2. Iterar sobre los productos solicitados
        for (Long productId : request.productIds()) {
            // 2.1 Hablar con ProductService para obtener la entidad del producto
            Product product = productService.findProductById(productId);

            // 2.2 Verificar y decrementar el stock
            if (product.getStock() <= 0) {
                throw new InsufficientStockException("Stock insuficiente para el producto: " + product.getName());
            }
            product.setStock(product.getStock() - 1);

            // La entidad 'product' está en estado "managed" por JPA.
            // Cualquier cambio en sus atributos (como el stock) será persistido
            // automáticamente cuando la transacción se complete con éxito.

            productsForOrder.add(product);
            total = total.add(product.getPrice());
        }

        order.setItems(productsForOrder);
        order.setTotal(total);

        // 3. Persistir la nueva orden y sus relaciones
        Order savedOrder = orderRepository.save(order);

        // 4. Mapear la entidad a un DTO de respuesta y devolver
        return toOrderResponseDTO(savedOrder);
    }

    /**
     * Método de utilidad privado para mapear la entidad Order a su DTO de respuesta.
     * Esto promueve el principio DRY (Don't Repeat Yourself).
     *
     * @param order La entidad Order persistida.
     * @return El DTO de respuesta correspondiente.
     */
    private OrderResponseDTO toOrderResponseDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(product -> new OrderItemDTO(
                        product.getId(),
                        product.getName(),
                        product.getPrice()))
                .toList();

        return new OrderResponseDTO(
                order.getId(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                order.getOrderDate(),
                itemDTOs,
                order.getTotal()
        );
    }
}
