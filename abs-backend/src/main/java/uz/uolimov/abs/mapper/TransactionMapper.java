package uz.uolimov.abs.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import uz.uolimov.abs.model.dto.kafka.TransactionEvent;
import uz.uolimov.abs.model.dto.transaction.ExecuteRequestDTO;
import uz.uolimov.abs.model.dto.transaction.ExecuteResponseDTO;
import uz.uolimov.abs.model.entity.TransactionEntity;
import uz.uolimov.abs.model.entity.UserEntity;
import uz.uolimov.abs.model.enums.TransactionTechStatus;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Component
@AllArgsConstructor
public class TransactionMapper {

    public TransactionEntity toEntity(ExecuteRequestDTO request, UserEntity sender, UserEntity receiver) {
        return new TransactionEntity(
            UUID.randomUUID(),
            request.amount(),
            TransactionTechStatus.IN_PROGRESS,
            Instant.now(),
            Instant.now(),
            sender,
            receiver,
            Collections.emptyList()
        );
    }

    public ExecuteResponseDTO toExecuteResponse(TransactionEntity entity) {
        return new ExecuteResponseDTO(
            entity.getStatus().toString()
        );
    }

    public TransactionEvent toEvent(TransactionEntity transactionEntity) {
        return new TransactionEvent(
            transactionEntity.getId(),
            transactionEntity.getAmount(),
            transactionEntity.getStatus()
        );
    }

    public TransactionEvent toEvent(TransactionEntity entity, TransactionTechStatus status) {
        return new TransactionEvent(
            entity.getId(),
            entity.getAmount(),
            status
        );
    }
}
