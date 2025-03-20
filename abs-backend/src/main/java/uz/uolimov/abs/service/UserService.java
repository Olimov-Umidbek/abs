package uz.uolimov.abs.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import uz.uolimov.commons.exceptions.BusinessException;
import uz.uolimov.commons.exceptions.InternalException;
import uz.uolimov.commons.model.BusinessError;
import uz.uolimov.commons.model.InternalError;
import uz.uolimov.abs.mapper.UserMapper;
import uz.uolimov.abs.model.dto.user.*;
import uz.uolimov.abs.model.entity.UserEntity;
import uz.uolimov.abs.model.enums.UserStatus;
import uz.uolimov.abs.repository.UserRepository;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository repository;
    private final TransactionTemplate transactionTemplate;

    public UserDetailsResponse get(UUID id) {
        return userMapper.toUserDetails(findById(id));
    }

    public Page<UserDetailsResponse> getAll(PageRequest pageRequest) {
        return repository.findAll(pageRequest).map(userMapper::toUserDetails);
    }

    public CreateUserResponse create(CreateUserRequestDTO request) {
        UserEntity entity = transactionTemplate.execute(
            (TransactionStatus status) -> repository.save(userMapper.toEntity(request))
        );

        return userMapper.toCreateUserResponse(entity);
    }

    public UserDetailsResponse update(UUID id, UpdateUserRequestDTO request) {
        UserEntity entity = findById(id);
        if (StringUtils.isNotBlank(request.name())) {
            entity.setName(request.name());
        }
        if (StringUtils.isNotBlank(request.surname())) {
            entity.setSurname(request.surname());
        }

        entity.setUpdatedAt(Instant.now());
        log.info("User details updated");

        return userMapper.toUserDetails(repository.save(entity));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(UUID id) {
        UserEntity entity = findById(id);
        entity.setStatus(UserStatus.DELETED);
        entity.setUpdatedAt(Instant.now());

        repository.save(entity);
    }

    public UserEntity findById(UUID id) {
        try {
            return repository.findById(id).orElseThrow(() ->
                new BusinessException(BusinessError.USER_NOT_FOUND)
            );
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            throw new InternalException(InternalError.UNKNOWN_ERROR, e);
        }
    }
}
