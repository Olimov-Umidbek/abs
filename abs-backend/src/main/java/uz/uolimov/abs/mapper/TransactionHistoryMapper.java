package uz.uolimov.abs.mapper;

import org.springframework.stereotype.Component;
import uz.uolimov.abs.model.entity.TransactionEntity;
import uz.uolimov.abs.model.entity.TransactionHistoryEntity;
import uz.uolimov.abs.model.enums.TransactionTechStatus;

import java.time.Instant;

@Component
public class TransactionHistoryMapper {

    public TransactionHistoryEntity toTransactionHistoryEntity(TransactionEntity entity, TransactionTechStatus status) {
        return new TransactionHistoryEntity(
            null,
            status,
            Instant.now(),
            Instant.now(),
            entity
        );
    }
}
