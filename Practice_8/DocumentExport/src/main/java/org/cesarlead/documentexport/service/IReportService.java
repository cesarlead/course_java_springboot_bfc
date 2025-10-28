package org.cesarlead.documentexport.service;

import org.cesarlead.documentexport.dto.BankReportDTO;

public interface IReportService {
    BankReportDTO generateCustomerReport(Long customerId);
}
