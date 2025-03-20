package uz.uolimov.abs.model.enums;

public enum TransactionTechStatus {
    IN_PROGRESS,
    RETRY,
    COMPLETED,
    TTL_EXPIRED,
    NOT_COMPLETED,
    ROLLBACK,
    HOLD,
    ERROR;
}
