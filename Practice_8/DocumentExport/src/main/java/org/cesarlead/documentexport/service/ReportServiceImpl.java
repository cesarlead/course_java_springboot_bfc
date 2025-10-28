package org.cesarlead.documentexport.service;

import org.cesarlead.documentexport.client.CustomerApiClient;
import org.cesarlead.documentexport.dto.AccountSummaryDTO;
import org.cesarlead.documentexport.dto.BankReportDTO;
import org.cesarlead.documentexport.dto.CustomerDTO;
import org.cesarlead.documentexport.exception.ResourceNotFoundException;
import org.cesarlead.documentexport.mapper.AccountSummaryMapper;
import org.cesarlead.documentexport.model.Account;
import org.cesarlead.documentexport.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class ReportServiceImpl implements IReportService {

    private final AccountRepository accountRepository;
    private final CustomerApiClient customerApiClient;

    public ReportServiceImpl(AccountRepository accountRepository, CustomerApiClient customerApiClient) {
        this.accountRepository = accountRepository;
        this.customerApiClient = customerApiClient;
    }

    public BankReportDTO generateCustomerReport(Long customerId) {


        // 1. Obtener los detalles del cliente desde el microservicio de clientes
        CustomerDTO customerOpt = customerApiClient.getCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede generar reporte: Cliente no encontrado con ID: " + customerId
                ));


        // 2. Obtener los datos de las cuentas (de nuestra propia DB)
        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        // 3. Mapear y transformar los datos (Lógica de negocio)
        List<AccountSummaryDTO> accountSummaries = accounts.stream()
                .map(AccountSummaryMapper::mapToAccountSummaryDTO)
                .toList();

        // 4. Calcular el balance total
        BigDecimal totalBalance = accountSummaries.stream()
                .map(AccountSummaryDTO::balance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 5. Ensamblar el DTO final
        return new BankReportDTO(
                customerOpt,
                accountSummaries,
                totalBalance
        );

    }
}

