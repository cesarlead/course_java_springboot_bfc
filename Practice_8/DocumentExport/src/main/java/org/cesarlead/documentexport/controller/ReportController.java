package org.cesarlead.documentexport.controller;

import org.cesarlead.documentexport.dto.BankReportDTO;
import org.cesarlead.documentexport.service.IReportService;
import org.cesarlead.documentexport.service.ReportServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final IReportService reportService;

    public ReportController(ReportServiceImpl reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<BankReportDTO> getCustomerReport(@PathVariable Long customerId) {

        BankReportDTO report = reportService.generateCustomerReport(customerId);

        return ResponseEntity.ok(report);
    }
}
