package uz.uolimov.abs.model.dto.user;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record UserDetailsResponse(
    UUID id,
    String name,
    String surname,
    BigDecimal balance,
    Instant createdAt,
    Instant updatedAt
) {
}
