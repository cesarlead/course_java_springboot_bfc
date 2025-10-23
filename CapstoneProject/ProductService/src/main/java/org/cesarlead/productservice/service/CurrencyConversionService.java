package org.cesarlead.productservice.service;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CurrencyConversionService {

    Mono<BigDecimal> getConversionRate(String fromCurrency, String toCurrency);
}
