package org.cesarlead.orderservice.controller;

import jakarta.validation.Valid;
import org.cesarlead.orderservice.dto.request.OrderRequestDTO;
import org.cesarlead.orderservice.dto.response.OrderResponseDTO;
import org.cesarlead.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Mono<ResponseEntity<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        return orderService.createOrder(request)
                // Cuando el Mono se complete, mapeamos a ResponseEntity 201
                .map(orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.CREATED));
    }
}
