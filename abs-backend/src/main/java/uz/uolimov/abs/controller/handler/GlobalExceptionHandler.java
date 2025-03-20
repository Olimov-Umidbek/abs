package uz.uolimov.abs.controller.handler;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.uolimov.commons.exceptions.AbsException;
import uz.uolimov.commons.exceptions.BusinessException;
import uz.uolimov.commons.exceptions.IntegrationException;
import uz.uolimov.commons.exceptions.InternalException;
import uz.uolimov.commons.model.ErrorType;
import uz.uolimov.commons.model.ValidationError;
import uz.uolimov.commons.model.dto.ErrorDTO;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body(e.buildDTO());
    }

    @ExceptionHandler(IntegrationException.class)
    public ResponseEntity<ErrorDTO> handleIntegrationException(IntegrationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body(e.buildDTO());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(ValidationException e) {
        Throwable ex = e;

        while (ex.getCause() != null) {

            if (ex.getCause() instanceof uz.uolimov.commons.exceptions.ValidationException exception) {
                return ResponseEntity.badRequest().body(exception.buildDTO());
            }
            ex = ex.getCause();
        }
        log.error(e.getMessage(), e);
        ErrorDTO errorDTO = new ErrorDTO(
            ErrorType.VALIDATION,
            ValidationError.INVALID_REQUEST.getCode(),
            e.getMessage()
        );

        return ResponseEntity.badRequest().body(errorDTO);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ErrorDTO> handleInternalException(InternalException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body(e.buildDTO());
    }

    @ExceptionHandler(AbsException.class)
    public ResponseEntity<ErrorDTO> handleAbsException(AbsException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body(e.buildDTO());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().build();
    }
}
