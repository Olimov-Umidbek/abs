package uz.uolimov.abs.model.dto.kafka;

import uz.uolimov.abs.model.enums.TransactionTechStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionEvent(
    UUID transactionId,
    BigDecimal amount,
    TransactionTechStatus status
) {
}
