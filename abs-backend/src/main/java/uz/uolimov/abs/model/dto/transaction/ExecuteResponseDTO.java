package uz.uolimov.abs.model.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Execute response structure")
public record ExecuteResponseDTO(
    @Schema(title = "Transaction status")
    String status
) {
}
