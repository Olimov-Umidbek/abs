package uz.uolimov.abs.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uolimov.abs.model.dto.transaction.StatusRequestDTO;
import uz.uolimov.abs.model.dto.transaction.TransactionDetailsDTO;
import uz.uolimov.commons.exceptions.BusinessException;
import uz.uolimov.commons.model.BusinessError;
import uz.uolimov.abs.mapper.TransactionMapper;
import uz.uolimov.abs.model.dto.transaction.ExecuteResponseDTO;
import uz.uolimov.abs.model.dto.transaction.ExecuteRequestDTO;
import uz.uolimov.abs.model.entity.TransactionEntity;
import uz.uolimov.abs.model.entity.UserEntity;
import uz.uolimov.abs.model.enums.TransactionTechStatus;
import uz.uolimov.abs.repository.TransactionRepository;
import uz.uolimov.abs.service.TransactionHistoryService;
import uz.uolimov.abs.service.TransactionService;
import uz.uolimov.abs.service.UserService;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty(prefix = "abs", value = "is-standalone", havingValue = "true")
public class StandaloneTransactionServiceImpl implements TransactionService {
    private final UserService userService;
    private final TransactionMapper mapper;
    private final TransactionRepository repository;
    private final TransactionHistoryService historyService;

    @Override
    @Transactional(noRollbackFor = BusinessException.class)
    public ExecuteResponseDTO execute(ExecuteRequestDTO request, UUID userId) {
        if (userId == request.receiverId()) {
            log.error("Cannot do transaction with the same client");
            throw new BusinessException(BusinessError.IDENTICAL_USER);
        }

        UserEntity sender = userService.findById(userId);
        UserEntity receiver = userService.findById(request.receiverId());
        TransactionEntity transactionEntity = mapper.toEntity(request, sender, receiver);

        if (sender.getBalance().compareTo(request.amount()) < 0) {
            log.error("Insufficient balance to continue transaction, id = " + transactionEntity.getId());
            transactionEntity.setStatus(TransactionTechStatus.ERROR);
            transactionEntity = repository.save(transactionEntity);
            historyService.createHistory(transactionEntity, TransactionTechStatus.ERROR);
            throw new BusinessException(BusinessError.INSUFFICIENT_BALANCE);
        }

        try {
            sender.setBalance(
                sender.getBalance().subtract(request.amount())
            );
            receiver.setBalance(
                receiver.getBalance().add(request.amount())
            );
            transactionEntity.setStatus(TransactionTechStatus.COMPLETED);
            transactionEntity = repository.save(transactionEntity);
            historyService.createHistory(transactionEntity, TransactionTechStatus.COMPLETED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            transactionEntity.setStatus(TransactionTechStatus.ERROR);
            repository.save(transactionEntity);
            historyService.createHistory(transactionEntity, TransactionTechStatus.ERROR);
        }

        return mapper.toExecuteResponse(transactionEntity);
    }

    @Override
    public TransactionDetailsDTO status(StatusRequestDTO request, UUID userId) {
        return repository.loadTransactionDetailsByIdAndUserId(request.transactionId(), userId).orElseThrow(
            () -> new BusinessException(BusinessError.TRANSACTION_NOT_FOUND)
        );
    }
}
