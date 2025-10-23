package org.cesarlead.orderservice.client;

import org.cesarlead.orderservice.dto.client.CustomerDTO;
import org.cesarlead.orderservice.exception.ExternalServiceException;
import org.cesarlead.orderservice.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CustomerServiceClient {

    private final WebClient webClient;

    public CustomerServiceClient(WebClient.Builder webClientBuilder,
                                 @Value("${services.customer.url}") String customerServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(customerServiceUrl).build();
    }

    public Mono<CustomerDTO> getCustomerById(Long customerId) {
        return webClient.get()
                .uri("/{id}", customerId)
                .retrieve()
                // Manejo de errores 404
                .onStatus(status -> status == HttpStatus.NOT_FOUND,
                        response -> Mono.error(new ResourceNotFoundException("Cliente no encontrado con id: " + customerId)))
                // Manejo de errores 5xx o 4xx genéricos
                .onStatus(status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ExternalServiceException("Error en servicio de clientes: " + errorBody))))
                .bodyToMono(CustomerDTO.class);
    }
}
