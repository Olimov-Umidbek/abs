package uz.uolimov.abs.model.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(title = "Status request structure")
public record StatusRequestDTO(
    @Schema(title = "Transaction id", requiredMode = Schema.RequiredMode.REQUIRED)
    UUID transactionId
) {
}
