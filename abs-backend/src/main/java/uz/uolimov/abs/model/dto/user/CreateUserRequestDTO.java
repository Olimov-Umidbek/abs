package uz.uolimov.abs.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(title = "Create user request structure")
public record CreateUserRequestDTO(
    @Schema(title = "User name", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,
    @Schema(title = "User surname", requiredMode = Schema.RequiredMode.REQUIRED)
    String surname,
    @Schema(title = "Balance", requiredMode = Schema.RequiredMode.REQUIRED)
    BigDecimal balance
) {}