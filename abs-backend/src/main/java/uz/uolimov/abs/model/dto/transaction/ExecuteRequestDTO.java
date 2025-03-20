package uz.uolimov.abs.model.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(title = "Prepare request structure")
public record ExecuteRequestDTO(
    @Schema(title = "Receiver user id", requiredMode = Schema.RequiredMode.REQUIRED)
    UUID receiverId,

    @Schema(title = "Transaction amount", requiredMode = Schema.RequiredMode.REQUIRED)
    BigDecimal amount
) {
}
