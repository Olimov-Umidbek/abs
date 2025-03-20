package uz.uolimov.commons.exceptions;

import uz.uolimov.commons.model.ErrorType;
import uz.uolimov.commons.model.dto.ErrorDTO;

public class AbsException extends RuntimeException {
    private final String code;
    private final ErrorType type;


    public AbsException(ErrorType type, String code, String message) {
        super(message);
        this.type = type;
        this.code = code;
    }

    public AbsException(ErrorType type, String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.type = type;
    }

    public ErrorDTO buildDTO() {
        return new ErrorDTO(
            type,
            code,
            getMessage()
        );
    }
}
