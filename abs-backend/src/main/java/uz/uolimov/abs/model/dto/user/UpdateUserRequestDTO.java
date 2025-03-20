package uz.uolimov.abs.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    title = "Update user request structure",
    description = "One of field must be filled to update user details"
)
public record UpdateUserRequestDTO(
    @Schema(title = "First name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String name,
    @Schema(title = "Surname", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String surname
) {
}
