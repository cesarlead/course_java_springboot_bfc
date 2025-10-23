package org.cesarlead.orderservice.service;

import org.cesarlead.orderservice.dto.request.OrderRequestDTO;
import org.cesarlead.orderservice.dto.response.OrderResponseDTO;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<OrderResponseDTO> createOrder(OrderRequestDTO request);
}
