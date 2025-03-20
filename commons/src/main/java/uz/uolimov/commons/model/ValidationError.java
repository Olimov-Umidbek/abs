package uz.uolimov.commons.model;

import lombok.Getter;

@Getter
public enum ValidationError {
    INVALID_VALUE("INVALID_VALUE", "Invalid value"),
    INVALID_REQUEST("INVALID_REQUEST", "Invalid request");
    private final String code;
    private final String message;

    ValidationError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
