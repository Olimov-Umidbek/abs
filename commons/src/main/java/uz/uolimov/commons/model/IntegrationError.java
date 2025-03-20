package uz.uolimov.commons.model;

import lombok.Getter;

@Getter
public enum IntegrationError {

    UNKNOWN_ERROR("UNKNOWN_ERROR", "Happened unknown error");
    private final String code;
    private final String message;

    IntegrationError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
