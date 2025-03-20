package uz.uolimov.commons.exceptions;

import uz.uolimov.commons.model.ErrorType;
import uz.uolimov.commons.model.IntegrationError;
import uz.uolimov.commons.model.InternalError;

public class IntegrationException extends AbsException {
    private final IntegrationError error;

    public IntegrationException(IntegrationError error) {
        super(ErrorType.INTEGRATION, error.getCode(), error.getMessage());
        this.error = error;
    }

    public IntegrationException(IntegrationError error, Throwable cause) {
        super(ErrorType.INTEGRATION, error.getCode(), error.getMessage(), cause);
        this.error = error;
    }
}
