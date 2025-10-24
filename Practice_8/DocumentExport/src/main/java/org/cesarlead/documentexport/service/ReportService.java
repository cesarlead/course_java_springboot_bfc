package org.cesarlead.documentexport.service;

import org.cesarlead.documentexport.client.CustomerApiClient;
import org.cesarlead.documentexport.dto.AccountSummaryDTO;
import org.cesarlead.documentexport.dto.BankReportDTO;
import org.cesarlead.documentexport.dto.CustomerDTO;
import org.cesarlead.documentexport.dto.TransactionDTO;
import org.cesarlead.documentexport.model.Account;
import org.cesarlead.documentexport.model.Transaction;
import org.cesarlead.documentexport.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ReportService {

    private final AccountRepository accountRepository;
    private final CustomerApiClient customerApiClient;

    public ReportService(AccountRepository accountRepository, CustomerApiClient customerApiClient) {
        this.accountRepository = accountRepository;
        this.customerApiClient = customerApiClient;
    }

    public Optional<BankReportDTO> generateCustomerReport(Long customerId) {
        // 1. Obtener los detalles del cliente desde el microservicio de clientes
        Optional<CustomerDTO> customerOpt = customerApiClient.getCustomerById(customerId);

        if (customerOpt.isEmpty()) {
            // Si el cliente no existe, no podemos generar un reporte.
            return Optional.empty();
        }

        CustomerDTO customerDetails = customerOpt.get();

        // 2. Obtener los datos de las cuentas (de nuestra propia DB)
        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        // 3. Mapear y transformar los datos (Lógica de negocio)
        List<AccountSummaryDTO> accountSummaries = accounts.stream()
                .map(this::mapToAccountSummaryDTO)
                .toList();

        // 4. Calcular el balance total
        BigDecimal totalBalance = accountSummaries.stream()
                .map(AccountSummaryDTO::balance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 5. Ensamblar el DTO final
        BankReportDTO report = new BankReportDTO(
                customerDetails,
                accountSummaries,
                totalBalance
        );

        return Optional.of(report);
    }

    // Método de ayuda (Helper) para convertir Entidades a DTOs (DRY)
    private AccountSummaryDTO mapToAccountSummaryDTO(Account account) {

        // 1. Preparamos la lista de transacciones ANTES de crear el DTO principal.
        List<TransactionDTO> transactionDTOs = account.getTransactions().stream()
                .map(this::mapToTransactionDTO) // Llama al mapper de records
                .collect(Collectors.toList());

        // 2. Llamamos al constructor canónico con TODOS los campos a la vez.
        return new AccountSummaryDTO(
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                transactionDTOs // Pasamos la lista de transacciones aquí
        );
    }

    private TransactionDTO mapToTransactionDTO(Transaction transaction) {
        // Llamamos al constructor canónico del record
        return new TransactionDTO(
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getDescription()

        );
    }
}

