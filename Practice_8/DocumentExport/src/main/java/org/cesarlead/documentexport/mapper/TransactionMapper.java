package org.cesarlead.documentexport.mapper;

import org.cesarlead.documentexport.dto.TransactionDTO;
import org.cesarlead.documentexport.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public static TransactionDTO mapToTransactionDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getDescription()
        );
    }
}
