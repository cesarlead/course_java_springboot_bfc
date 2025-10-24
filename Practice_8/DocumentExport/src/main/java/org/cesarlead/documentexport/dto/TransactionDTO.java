package org.cesarlead.documentexport.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record TransactionDTO(
        BigDecimal amount,
        LocalDateTime transactionDate,
        String description
) {
}
