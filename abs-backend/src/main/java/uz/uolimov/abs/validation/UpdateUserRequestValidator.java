package uz.uolimov.abs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import uz.uolimov.commons.exceptions.ValidationException;
import uz.uolimov.commons.model.ValidationError;
import uz.uolimov.abs.model.dto.user.UpdateUserRequestDTO;

import java.util.LinkedList;
import java.util.List;

public class UpdateUserRequestValidator implements ConstraintValidator<UpdateUserRequest, UpdateUserRequestDTO> {
    private static final int FULL_ERRORS_COUNT = 2;

    @Override
    public boolean isValid(UpdateUserRequestDTO request, ConstraintValidatorContext context) {
        if (request == null) {
            throw new ValidationException(ValidationError.INVALID_REQUEST);
        }

        List<String> errors = new LinkedList<>();
        if (StringUtils.isBlank(request.name())) {
            errors.add("Invalid value of name field in the request");
        }
        if (StringUtils.isBlank(request.surname())) {
            errors.add("Invalid value of surname field in the request");
        }

        if (errors.size() == FULL_ERRORS_COUNT) {
            throw new ValidationException(
                ValidationError.INVALID_REQUEST, errors.stream().findFirst().get()
            );
        }

        return true;
    }
}
