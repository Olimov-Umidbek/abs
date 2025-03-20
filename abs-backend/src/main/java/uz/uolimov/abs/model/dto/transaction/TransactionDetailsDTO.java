package uz.uolimov.abs.model.dto.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDetailsDTO(
    UUID id,
    String senderName,
    String receiverName,
    BigDecimal amount,
    String status
) {
}
