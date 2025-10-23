package org.cesarlead.orderservice.client;

import org.cesarlead.orderservice.dto.client.ProductDTO;
import org.cesarlead.orderservice.exception.ExternalServiceException;
import org.cesarlead.orderservice.exception.InsufficientStockException;
import org.cesarlead.orderservice.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ProductServiceClient {

    private final WebClient webClient;

    public ProductServiceClient(WebClient.Builder webClientBuilder,
                                @Value("${services.product.url}") String productServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(productServiceUrl).build();
    }

    public Mono<ProductDTO> getProductById(Long productId) {
        return webClient.get()
                .uri("/{id}", productId)
                .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND,
                        response -> Mono.error(new ResourceNotFoundException("Producto no encontrado con id: " + productId)))
                .onStatus(status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ExternalServiceException("Error en servicio de productos: " + errorBody))))
                .bodyToMono(ProductDTO.class);
    }

    public Mono<Void> decrementStock(Long productId, Integer quantity) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/decrement-stock")
                        .queryParam("quantity", quantity)
                        .build(productId))
                .retrieve()
                // El servicio de productos devuelve 409 CONFLICT si no hay stock
                .onStatus(status -> status == HttpStatus.CONFLICT,
                        response -> response.bodyToMono(String.class) // Asumimos que devuelve un ErrorResponseDTO con el mensaje
                                .flatMap(errorBody -> Mono.error(new InsufficientStockException("Fallo al decrementar stock: " + errorBody))))
                .onStatus(status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new ExternalServiceException("Error al decrementar stock: " + errorBody))))
                .bodyToMono(Void.class);
    }
}
