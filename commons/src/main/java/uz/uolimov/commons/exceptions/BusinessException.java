package uz.uolimov.commons.exceptions;

import uz.uolimov.commons.model.BusinessError;
import uz.uolimov.commons.model.ErrorType;

public class BusinessException extends AbsException {
    private final BusinessError error;

    public BusinessException(BusinessError error) {
        super(ErrorType.BUSINESS, error.getCode(), error.getMessage());
        this.error = error;
    }

    public BusinessException(BusinessError error, Throwable cause) {
        super(ErrorType.BUSINESS, error.getCode(), error.getMessage(), cause);
        this.error = error;
    }
}
