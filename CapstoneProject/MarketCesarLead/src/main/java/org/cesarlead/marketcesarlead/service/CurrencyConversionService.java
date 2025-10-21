package org.cesarlead.marketcesarlead.service;

import org.cesarlead.marketcesarlead.dto.response.ExchangeRateResponse;
import org.cesarlead.marketcesarlead.exception.ExternalApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class CurrencyConversionService {

    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionService.class);

    public CurrencyConversionService(WebClient webClient) {
        this.webClient = webClient;
    }

    // El resultado de este método se cacheará. La clave del caché será la moneda "toCurrency".
    @Cacheable(value = "currency-rates", key = "#toCurrency")
    public Mono<BigDecimal> getConversionRate(String fromCurrency, String toCurrency) {
        logger.info("Llamando a la API externa para obtener la tasa de cambio de {} a {}", fromCurrency, toCurrency);

        // La URI se construye para consultar desde la moneda base (USD en nuestro caso)
        String uri = "/latest/" + fromCurrency;

        return webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    String errorMessage = String.format("Error de la API externa: %s - %s", response.statusCode(), errorBody);
                                    return Mono.error(new ExternalApiException(errorMessage));
                                }))
                // 1. El cuerpo del JSON se convierte al objeto ExchangeRateResponse
                .bodyToMono(ExchangeRateResponse.class)
                // 2. Extraemos el mapa 'conversion_rates' y obtenemos la tasa de la moneda destino
                .map(response -> response.conversion_rates().get(toCurrency.toUpperCase()))
                .switchIfEmpty(Mono.error(new ExternalApiException("La moneda de destino no fue encontrada: " + toCurrency)));
    }

}
