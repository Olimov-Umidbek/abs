package uz.uolimov.commons.model;

import lombok.Getter;

@Getter
public enum InternalError {
    UNKNOWN_ERROR("UNKNOWN_ERROR", "Happened unknown error");
    private final String code;
    private final String message;

    InternalError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
