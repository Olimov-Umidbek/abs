package uz.uolimov.commons.model.dto;

import uz.uolimov.commons.model.ErrorType;

public record ErrorDTO (
    ErrorType type,
    String code,
    String message
) {}
