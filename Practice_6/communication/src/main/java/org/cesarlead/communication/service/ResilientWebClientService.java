package org.cesarlead.communication.service;

import org.cesarlead.communication.dto.ProductDTO;
import org.cesarlead.communication.exeption.ProductNotFoundException;
import org.cesarlead.communication.exeption.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class ResilientWebClientService {

    private static final Logger log = LoggerFactory.getLogger(ResilientWebClientService.class);
    private final WebClient webClient;

    // Inyectamos nuestro WebClient configurado por su nombre específico
    public ResilientWebClientService(@Qualifier("resilientWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ProductDTO> findProductById(Long id) {
        return webClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                // --- MANEJO DE ERRORES ---
                // Traduce errores HTTP 4xx a nuestra excepción de dominio personalizada.
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new ProductNotFoundException("Product not found via WebClient with id: " + id)))
                // Traduce errores HTTP 5xx a nuestra excepción de dominio para reintentos.
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new ServiceUnavailableException("External service is unavailable via WebClient")))

                .bodyToMono(ProductDTO.class)

                // --- ESTRATEGIA DE REINTENTOS ---
                .retryWhen(Retry
                        // 1. Estrategia: Backoff exponencial. Espera 1s, luego 2s, luego 4s...
                        .backoff(3, Duration.ofSeconds(1))
                        // 2. Jitter: Añade aleatoriedad para evitar "tormentas de reintentos".
                        .jitter(0.5)
                        // 3. Filtro: ¡CRÍTICO! Solo reintentamos ante fallos transitorios (nuestra excepción 5xx).
                        .filter(throwable -> throwable instanceof ServiceUnavailableException)
                        // 4. Log: Informamos en cada reintento.
                        .doBeforeRetry(retrySignal -> log.warn("Retrying call... attempt #{}. Cause: {}",
                                retrySignal.totalRetries() + 1, retrySignal.failure().getMessage()))
                        // 5. Fallo Final: Si se agotan los reintentos, lanzamos una excepción final clara.
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                new ServiceUnavailableException("Service not available after " + retrySignal.totalRetries() + " retries"))
                )
                // --- ESTRATEGIA DE FALLBACK ---
                // Si al final de tod despues de reintentos aun hay un error de "Producto No Encontrado",
                // devolvemos un producto por defecto en lugar de un error.
                .onErrorResume(ProductNotFoundException.class, ex -> {
                    log.warn("Product not found, returning a default fallback product. Details: {}", ex.getMessage());
                    return Mono.just(new ProductDTO(id, "Default Fallback Product", "N/A", 0.0));
                });
    }
}
