package uz.uolimov.abs.mapper;

import org.springframework.stereotype.Component;
import uz.uolimov.abs.model.dto.user.CreateUserRequestDTO;
import uz.uolimov.abs.model.dto.user.CreateUserResponse;
import uz.uolimov.abs.model.dto.user.UserDetailsResponse;
import uz.uolimov.abs.model.entity.UserEntity;
import uz.uolimov.abs.model.enums.UserStatus;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Component
public class UserMapper {

    public UserEntity toEntity(CreateUserRequestDTO request) {
        return new UserEntity(
            UUID.randomUUID(),
            request.name(),
            request.surname(),
            UserStatus.ACTIVE,
            request.balance(),
            null,
            Instant.now(),
            Instant.now(),
            Collections.emptyList(),
            Collections.emptyList()
        );
    }

    public CreateUserResponse toCreateUserResponse(UserEntity entity) {
        return new CreateUserResponse(
            entity.getId(),
            entity.getName(),
            entity.getSurname()
        );
    }

    public UserDetailsResponse toUserDetails(UserEntity entity) {
        return new UserDetailsResponse(
            entity.getId(),
            entity.getName(),
            entity.getSurname(),
            entity.getBalance(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
