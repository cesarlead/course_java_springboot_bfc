package org.cesarlead.documentexport.dto;

import java.math.BigDecimal;
import java.util.List;

public record AccountSummaryDTO(
        String accountNumber,
        String accountType,
        BigDecimal balance,
        List<TransactionDTO> recentTransactions
) {
}
