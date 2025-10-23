package org.cesarlead.productservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeRateResponse(
        Map<String, BigDecimal> conversion_rates
) {
}
