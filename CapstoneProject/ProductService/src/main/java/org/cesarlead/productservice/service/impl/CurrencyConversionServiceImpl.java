package org.cesarlead.productservice.service.impl;

import org.cesarlead.productservice.dto.ExchangeRateResponse;
import org.cesarlead.productservice.exception.ExternalApiException;
import org.cesarlead.productservice.service.CurrencyConversionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionServiceImpl.class);
    private final WebClient webClient;

    public CurrencyConversionServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<BigDecimal> getConversionRate(String fromCurrency, String toCurrency) {
        logger.info("Cache miss. Llamando a la API externa para obtener la tasa de cambio de {} a {}", fromCurrency, toCurrency);

        String uri = "/latest/" + fromCurrency.toUpperCase();
        String targetCurrencyUpper = toCurrency.toUpperCase();

        return webClient.get()
                .uri(uri)
                .retrieve()
                // ! MEJOR PRÁCTICA: Manejar errores de la API externa
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    String errorMsg = String.format("Error de la API externa: %s - %s", response.statusCode(), errorBody);
                                    logger.warn(errorMsg);
                                    return Mono.error(new ExternalApiException(errorMsg));
                                }))
                .bodyToMono(ExchangeRateResponse.class)
                .flatMap(response -> {
                    // Usamos Optional para manejar de forma segura la posible ausencia de la moneda
                    BigDecimal rate = Optional.ofNullable(response.conversion_rates())
                            .map(rates -> rates.get(targetCurrencyUpper))
                            .orElse(null);

                    if (rate == null) {
                        logger.warn("La moneda de destino '{}' no fue encontrada en la respuesta de la API.", targetCurrencyUpper);
                        // Convertimos el 'null' en un error controlado
                        return Mono.error(new ExternalApiException("La moneda de destino no fue encontrada: " + targetCurrencyUpper));
                    }

                    return Mono.just(rate);
                })

                .doOnError(throwable -> logger.error("Fallo al obtener la tasa de cambio", throwable))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(500))
                        .filter(throwable -> throwable instanceof ExternalApiException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                new ExternalApiException("Fallaron todos los reintentos para la API de conversión.")))
                .cache();
    }
}
