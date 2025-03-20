package uz.uolimov.abs.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import uz.uolimov.abs.mapper.TransactionMapper;
import uz.uolimov.abs.model.entity.UserEntity;
import uz.uolimov.commons.exceptions.BusinessException;
import uz.uolimov.commons.model.BusinessError;
import uz.uolimov.abs.config.props.ApplicationProperties;
import uz.uolimov.abs.model.dto.kafka.TransactionEvent;
import uz.uolimov.abs.model.entity.TransactionEntity;
import uz.uolimov.abs.model.enums.TransactionTechStatus;
import uz.uolimov.abs.repository.TransactionRepository;
import uz.uolimov.abs.service.TransactionHistoryService;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "abs", value = "is-standalone", havingValue = "false")
@AllArgsConstructor
public class TransactionManagerService {
    private final TransactionMapper mapper;
    private final ProducerService producerService;
    private final ApplicationProperties properties;
    private final TransactionRepository repository;
    private final TransactionTemplate transactionTemplate;
    private final TransactionHistoryService historyService;

    public void handleInProgress(TransactionEvent event) {
        try {
            TransactionEntity transactionEntity = transactionTemplate.execute(
                (TransactionStatus status) -> {
                    TransactionEntity entity = getEntity(event.transactionId());
                    if (entity.getSender().getBalance().compareTo(entity.getAmount()) < 0) {
                        log.error("Insufficient balance to continue transaction, id = " + entity.getId());
                        updateTransaction(entity, TransactionTechStatus.NOT_COMPLETED);
                        throw new BusinessException(BusinessError.INSUFFICIENT_BALANCE);
                    }
                    UserEntity sender = entity.getSender();
                    sender.setBalance(sender.getBalance().subtract(entity.getAmount()));
                    updateTransaction(entity, TransactionTechStatus.HOLD);
                    return entity;
                }
            );
            sendEvent(transactionEntity, TransactionTechStatus.HOLD);
        } catch (Exception e) {
            log.error("Happened an error while handling in progress event, " +
                "transactionId = " + event.transactionId() + ", message = " + e.getMessage(), e);
            handleException(event.transactionId());
        }
    }

    public void handleHoldEvent(TransactionEvent event) {
        try {
            transactionTemplate.executeWithoutResult(
                (TransactionStatus status) -> {
                    TransactionEntity entity = getEntity(event.transactionId());
                    UserEntity receiver = entity.getReceiver();
                    receiver.setBalance(receiver.getBalance().add(entity.getAmount()));
                    updateTransaction(entity, TransactionTechStatus.COMPLETED);
                }
            );
        } catch (Exception e) {
            log.error("Happened an error while handling hold event, " +
                "transactionId = " + event.transactionId() + ", message = " + e.getMessage(), e);
            handleException(event.transactionId());
        }
    }

    public void handleRetryEvent(TransactionEvent event) {
        try {
            TransactionEntity entity = getEntity(event.transactionId());
            historyService.createHistory(entity, TransactionTechStatus.RETRY);
            sendEvent(entity, entity.getStatus());
        } catch (Exception e) {
            log.error("Happened and error while handling retry event, " +
                "transactionId = " + event.transactionId() + ", message = " + e.getMessage(), e);
            updateTransaction(getEntity(event.transactionId()), TransactionTechStatus.ERROR);
        }
    }

    public void handleRollbackEvent(TransactionEvent event) {
        TransactionEntity entity = getEntity(event.transactionId());
        if (entity.getStatus() == TransactionTechStatus.HOLD) {
            UserEntity sender = entity.getSender();
            sender.setBalance(sender.getBalance().add(event.amount()));
            repository.save(entity);
            log.info("Transaction amount returned to the sender user balance");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void updateTransaction(TransactionEntity entity, TransactionTechStatus newStatus) {
        TransactionTechStatus oldStatus = entity.getStatus();
        entity.setUpdatedAt(Instant.now());

        if (newStatus != TransactionTechStatus.RETRY) {
            entity.setStatus(newStatus);
            log.info("The tech status updated from " +  oldStatus + " to " + newStatus + " for transactionId=" + entity.getId());
        }

        repository.save(entity);
        historyService.createHistory(entity, newStatus);
    }

    private void handleException(UUID transactionId) {
        TransactionEntity entity = getEntity(transactionId);
        int retryCount = historyService.getHistoryCountWithStatus(transactionId, TransactionTechStatus.RETRY);
        if (retryCount < properties.retryCount()) {
            updateTransaction(entity, TransactionTechStatus.RETRY);
            sendEvent(entity, TransactionTechStatus.RETRY);
            return;
        }
        log.info("Retry limit was reached for transaction with id = " + transactionId);
        if (entity.getStatus() == TransactionTechStatus.HOLD) {
            updateTransaction(entity, TransactionTechStatus.ROLLBACK);
            sendEvent(entity, TransactionTechStatus.ROLLBACK);
        } else {
            updateTransaction(entity, TransactionTechStatus.ERROR);
        }
    }

    private void sendEvent(TransactionEntity entity, TransactionTechStatus status) {
        TransactionEvent event = mapper.toEvent(entity, status);
        producerService.sendToKafka(event);
    }

    private TransactionEntity getEntity(UUID transactionId) {
        return repository.findById(transactionId).orElseThrow(
            () -> new BusinessException(BusinessError.TRANSACTION_NOT_FOUND)
        );
    }
}
