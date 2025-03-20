package uz.uolimov.abs.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import uz.uolimov.abs.kafka.ProducerService;
import uz.uolimov.abs.mapper.TransactionMapper;
import uz.uolimov.abs.model.dto.transaction.ExecuteRequestDTO;
import uz.uolimov.abs.model.dto.transaction.ExecuteResponseDTO;
import uz.uolimov.abs.model.dto.transaction.StatusRequestDTO;
import uz.uolimov.abs.model.dto.transaction.TransactionDetailsDTO;
import uz.uolimov.abs.model.entity.TransactionEntity;
import uz.uolimov.abs.model.entity.UserEntity;
import uz.uolimov.abs.repository.TransactionRepository;
import uz.uolimov.abs.service.TransactionHistoryService;
import uz.uolimov.abs.service.TransactionService;
import uz.uolimov.abs.service.UserService;
import uz.uolimov.commons.exceptions.BusinessException;
import uz.uolimov.commons.exceptions.InternalException;
import uz.uolimov.commons.model.BusinessError;
import uz.uolimov.commons.model.InternalError;

import java.util.UUID;

@Slf4j
@Service
@Qualifier("distributedTransactionServiceImpl")
@ConditionalOnProperty(prefix = "abs", value = "is-standalone", havingValue = "false")
@AllArgsConstructor
public class DistributedTransactionServiceImpl implements TransactionService {

    private final UserService userService;
    private final TransactionMapper mapper;
    private final ProducerService producerService;
    private final TransactionRepository repository;
    private final TransactionTemplate transactionTemplate;
    private final TransactionHistoryService historyService;

    @Override
    public ExecuteResponseDTO execute(ExecuteRequestDTO request, UUID userId) {
        if (userId == request.receiverId()) {
            log.error("Cannot do transaction with the same client");
            throw new BusinessException(BusinessError.IDENTICAL_USER);
        }

        try {
            UserEntity sender = userService.findById(userId);
            UserEntity receiver = userService.findById(request.receiverId());
            TransactionEntity transactionEntity = transactionTemplate.execute(
                (TransactionStatus status) -> {
                    TransactionEntity entity = repository.save(mapper.toEntity(request, sender, receiver));
                    historyService.createHistory(entity, entity.getStatus());
                    return entity;
                }
            );

            producerService.sendToKafka(mapper.toEvent(transactionEntity));
            log.info("Transaction event sent to the kafka");
            return mapper.toExecuteResponse(transactionEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new InternalException(InternalError.UNKNOWN_ERROR, e);
        }
    }

    @Override
    public TransactionDetailsDTO status(StatusRequestDTO request, UUID userId) {
        return repository.loadTransactionDetailsByIdAndUserId(request.transactionId(), userId).orElseThrow(
            () -> new BusinessException(BusinessError.TRANSACTION_NOT_FOUND)
        );
    }
}
