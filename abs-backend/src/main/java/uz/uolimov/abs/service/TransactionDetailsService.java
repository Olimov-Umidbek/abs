package uz.uolimov.abs.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.uolimov.commons.exceptions.BusinessException;
import uz.uolimov.commons.model.BusinessError;
import uz.uolimov.abs.model.dto.transaction.TransactionDetailsDTO;
import uz.uolimov.abs.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransactionDetailsService {

    private final TransactionRepository repository;

    public TransactionDetailsDTO getOne(UUID transactionId, UUID userId) {
        return repository.loadTransactionDetailsByIdAndUserId(transactionId, userId)
            .orElseThrow(() -> new BusinessException(BusinessError.TRANSACTION_NOT_FOUND));
    }

    public Page<TransactionDetailsDTO> getAllByUserId(UUID userId, PageRequest pageRequest) {
        return repository.loadAllByUserId(userId, pageRequest);
    }

    public Page<TransactionDetailsDTO> getAllByDateRange(
        UUID userId,
        LocalDate startDate,
        LocalDate endDate,
        PageRequest pageRequest
    ) {
        return repository.loadAllByDateRange(userId, startDate, endDate, pageRequest);
    }
}
