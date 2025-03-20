package uz.uolimov.commons.exceptions;

import uz.uolimov.commons.model.ErrorType;
import uz.uolimov.commons.model.ValidationError;

public class ValidationException extends AbsException {
    private final ValidationError error;


    public ValidationException(ValidationError error) {
        super(ErrorType.VALIDATION, error.getCode(), error.getMessage());
        this.error = error;
    }

    public ValidationException(ValidationError error, Throwable throwable) {
        super(ErrorType.VALIDATION, error.getCode(), error.getMessage(), throwable);
        this.error = error;
    }

    public ValidationException(ValidationError error, String message) {
        super(ErrorType.VALIDATION, error.getCode(), error.getMessage());
        this.error = error;
    }
}
