package uz.uolimov.commons.model;

import lombok.Getter;

@Getter
public enum BusinessError {
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found"),
    USER_ALREADY_REGISTERED("USER_ALREADY_REGISTERED", "User already registered"),
    IDENTICAL_USER("IDENTICAL_USER", "The transaction cannot run for the same receiver"),

    ILLEGAL_OPERATION("ILLEGAL_OPERATION", "The user has not permission to do this action"),
    TRANSACTION_NOT_FOUND("TRANSACTION_NOT_FOUND", "Transaction not found"),
    INSUFFICIENT_BALANCE("INSUFFICIENT_BALANCE", "Insufficient balance to continue transaction"),
    TRANSACTION_EXPIRED("TRANSACTION_EXPIRED", "Transaction expired");

    private final String code;
    private final String message;

    BusinessError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
