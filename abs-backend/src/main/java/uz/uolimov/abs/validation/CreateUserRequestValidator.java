package uz.uolimov.abs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import uz.uolimov.commons.exceptions.ValidationException;
import uz.uolimov.commons.model.ValidationError;
import uz.uolimov.abs.model.dto.user.CreateUserRequestDTO;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class CreateUserRequestValidator implements ConstraintValidator<CreateUserRequest, CreateUserRequestDTO> {

    private static final Pattern REGEX = Pattern.compile("^([A-Z]{2})([0-9]{7})$");
    @Override
    public boolean isValid(CreateUserRequestDTO request, ConstraintValidatorContext constraintValidatorContext) {
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
        if (!errors.isEmpty()) {
            throw new ValidationException(
                ValidationError.INVALID_REQUEST, errors.stream().findFirst().get()
            );
        }

        return true;
    }
}
