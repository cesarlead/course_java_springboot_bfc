package org.cesarlead.documentexport.mapper;

import org.cesarlead.documentexport.dto.AccountSummaryDTO;
import org.cesarlead.documentexport.dto.TransactionDTO;
import org.cesarlead.documentexport.model.Account;

import java.util.List;

public class AccountSummaryMapper {

    public static AccountSummaryDTO mapToAccountSummaryDTO(Account account) {

        // 1. Preparamos la lista de transacciones ANTES de crear el DTO principal.
        List<TransactionDTO> transactionDTOs = account.getTransactions().stream()
                .map(TransactionMapper::mapToTransactionDTO)
                .toList();

        // 2. Llamamos al constructor canónico con TODOS los campos a la vez.
        return new AccountSummaryDTO(
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                transactionDTOs // Pasamos la lista de transacciones aquí
        );
    }


}
