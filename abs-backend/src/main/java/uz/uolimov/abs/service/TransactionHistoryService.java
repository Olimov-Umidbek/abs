package uz.uolimov.abs.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.uolimov.abs.mapper.TransactionHistoryMapper;
import uz.uolimov.abs.model.entity.TransactionEntity;
import uz.uolimov.abs.model.enums.TransactionTechStatus;
import uz.uolimov.abs.repository.TransactionHistoryRepository;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionHistoryService {
    private final TransactionHistoryMapper mapper;
    private final TransactionHistoryRepository repository;

    public void createHistory(TransactionEntity entity, TransactionTechStatus newStatus) {
        repository.save(mapper.toTransactionHistoryEntity(entity, newStatus));
        log.info("Created new status history for transaction = " + entity.getId());
    }

    public Integer getHistoryCountWithStatus(UUID transactionId, TransactionTechStatus status) {
        return repository.getCountByTransactionIdAndStatus(transactionId, status.name());
    }

}
