package org.cesarlead.marketcesarlead.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public record ExchangeRateResponse(
        Map<String, BigDecimal> conversion_rates
) {
}
