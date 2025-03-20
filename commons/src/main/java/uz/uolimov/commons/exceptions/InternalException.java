package uz.uolimov.commons.exceptions;

import uz.uolimov.commons.model.ErrorType;
import uz.uolimov.commons.model.InternalError;

public class InternalException extends AbsException {
    private final InternalError error;

    public InternalException(InternalError error) {
        super(ErrorType.INTERNAL, error.getCode(), error.getMessage());
        this.error = error;
    }

    public InternalException(InternalError error, Throwable throwable) {
        super(ErrorType.INTERNAL, error.getCode(), error.getMessage(), throwable);
        this.error = error;
    }
}
