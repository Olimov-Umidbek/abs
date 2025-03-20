package uz.uolimov.abs.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;
@Schema(title = "Create user response structure")
public record CreateUserResponse(
    @Schema(title = "User identification id")
    UUID id,
    @Schema(title = "First name")
    String name,
    @Schema(title = "Surname")
    String surname
) {
}
