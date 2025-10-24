package org.cesarlead.documentexport.dto;

import java.math.BigDecimal;
import java.util.List;

public record BankReportDTO(
        CustomerDTO customerDetails,
        List<AccountSummaryDTO> accountSummaries,
        BigDecimal totalBalance
) {
}
