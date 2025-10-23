package org.cesarlead.orderservice.service.impl;

import org.cesarlead.orderservice.client.CustomerServiceClient;
import org.cesarlead.orderservice.client.ProductServiceClient;
import org.cesarlead.orderservice.dto.client.CustomerDTO;
import org.cesarlead.orderservice.dto.client.ProductDTO;
import org.cesarlead.orderservice.dto.request.OrderItemRequestDTO;
import org.cesarlead.orderservice.dto.request.OrderRequestDTO;
import org.cesarlead.orderservice.dto.response.OrderItemResponseDTO;
import org.cesarlead.orderservice.dto.response.OrderResponseDTO;
import org.cesarlead.orderservice.exception.InsufficientStockException;
import org.cesarlead.orderservice.model.Order;
import org.cesarlead.orderservice.model.OrderItem;
import org.cesarlead.orderservice.repository.OrderRepository;
import org.cesarlead.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final CustomerServiceClient customerServiceClient;
    private final ProductServiceClient productServiceClient;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CustomerServiceClient customerServiceClient,
                            ProductServiceClient productServiceClient) {
        this.orderRepository = orderRepository;
        this.customerServiceClient = customerServiceClient;
        this.productServiceClient = productServiceClient;
    }

    @Override
    public Mono<OrderResponseDTO> createOrder(OrderRequestDTO request) {

        // 1. Validar al cliente
        Mono<CustomerDTO> customerMono = customerServiceClient.getCustomerById(request.customerId())
                .doOnSuccess(customer -> log.info("Cliente validado: {}", customer.name()));

        // 2. Crear un mapa de cantidades solicitadas para fácil acceso
        Map<Long, Integer> requestedQuantities = request.products().stream()
                .collect(Collectors.toMap(OrderItemRequestDTO::productId, OrderItemRequestDTO::quantity));

        // 3. Obtener info de TODOS los productos solicitados en paralelo
        List<Mono<ProductDTO>> productMonos = request.products().stream()
                .map(item -> productServiceClient.getProductById(item.productId()))
                .toList();

        // 4. Esperar a que el cliente Y todos los productos lleguen
        return Mono.zip(customerMono, Flux.merge(productMonos).collectList())
                .flatMap(tuple -> {
                    CustomerDTO customer = tuple.getT1();
                    List<ProductDTO> products = tuple.getT2();

                    // 5. Lógica de negocio: Validar stock y construir la orden
                    Order order = new Order();
                    order.setCustomerId(customer.id());
                    BigDecimal total = BigDecimal.ZERO;

                    for (ProductDTO product : products) {
                        int quantity = requestedQuantities.get(product.id());

                        // 5.1 Validar Stock
                        if (product.stock() < quantity) {
                            return Mono.error(new InsufficientStockException(
                                    "Stock insuficiente para " + product.name() + ". Solicitados: " + quantity + ", Disponibles: " + product.stock()
                            ));
                        }

                        // 5.2 Crear OrderItem con datos SNAPSHOT
                        OrderItem item = new OrderItem();
                        item.setProductId(product.id());
                        item.setQuantity(quantity);
                        item.setProductNameSnapshot(product.name()); // SNAPSHOT
                        item.setPriceSnapshot(product.price());      // SNAPSHOT

                        order.addItem(item);
                        total = total.add(product.price().multiply(BigDecimal.valueOf(quantity)));
                    }
                    order.setTotal(total);

                    // 6. Guardar la orden en nuestra DB (Operación Bloqueante)
                    // ! MEJOR PRÁCTICA: Mover la llamada bloqueante de JPA a un hilo
                    // de 'boundedElastic' para no bloquear el hilo de Event Loop de Reactor.
                    return saveOrderLocally(order)
                            .flatMap(savedOrder -> {
                                log.info("Pedido {} guardado localmente.", savedOrder.getId());
                                // 7. Si el guardado fue exitoso, confirmar reducción de stock
                                return decrementStockInProductService(savedOrder)
                                        .then(Mono.just(mapToResponseDTO(savedOrder, customer)));
                            });
                });
    }

    /**
     * Ejecuta la operación de guardado (bloqueante) de JPA en un hilo separado.
     * La anotación @Transactional se aplica aquí.
     */
    @Transactional
    public Order performSave(Order order) {
        // Esta operación es transaccional. Si falla al guardar un OrderItem,
        // hará rollback del Order y todos los demás OrderItems.
        return orderRepository.save(order);
    }

    private Mono<Order> saveOrderLocally(Order order) {
        return Mono.fromCallable(() -> this.performSave(order))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Llama al servicio de productos para decrementar el stock de cada item.
     */
    private Mono<Void> decrementStockInProductService(Order order) {
        // Creamos un Flux de llamadas Mono<Void>
        return Flux.fromIterable(order.getItems())
                .flatMap(item ->
                        productServiceClient.decrementStock(item.getProductId(), item.getQuantity())
                )
                .doOnError(ex -> {
                    // ! MEJOR PRÁCTICA: Patrón Saga (Compensación)
                    // Si esto falla, tenemos una inconsistencia de datos.
                    // El pedido está CREADO pero el stock no se decrementó.
                    // Una Saga real revertiría el pedido (ej. marcándolo como FALLIDO).
                    log.error(
                            "¡FALLO CRÍTICO DE CONSISTENCIA! El pedido {} se guardó, pero falló la reducción de stock. " +
                                    "Se requiere compensación manual. Causa: {}",
                            order.getId(),
                            ex.getMessage()
                    );
                    // (Aquí se podría publicar un evento "OrderFailed" a Kafka/RabbitMQ)
                })
                .then(); // Espera a que todos los Monos terminen
    }

    /**
     * Mapeador de Entidad+DTO a DTO de Respuesta (DRY)
     */
    private OrderResponseDTO mapToResponseDTO(Order order, CustomerDTO customer) {
        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getProductId(),
                        item.getProductNameSnapshot(), // Usamos el snapshot
                        item.getQuantity(),
                        item.getPriceSnapshot()      // Usamos el snapshot
                ))
                .toList();

        return new OrderResponseDTO(
                order.getId(),
                order.getOrderDate(),
                customer.name(), // Datos del DTO de cliente
                customer.email(), // Datos del DTO de cliente
                itemDTOs,
                order.getTotal()
        );
    }
}
