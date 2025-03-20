package uz.uolimov.abs.service;

import org.springframework.stereotype.Service;
import uz.uolimov.abs.model.dto.transaction.ExecuteResponseDTO;
import uz.uolimov.abs.model.dto.transaction.ExecuteRequestDTO;
import uz.uolimov.abs.model.dto.transaction.StatusRequestDTO;
import uz.uolimov.abs.model.dto.transaction.TransactionDetailsDTO;

import java.util.UUID;

@Service
public interface TransactionService {

    ExecuteResponseDTO execute(ExecuteRequestDTO request, UUID userId);

    TransactionDetailsDTO status(StatusRequestDTO request, UUID userId);
}
