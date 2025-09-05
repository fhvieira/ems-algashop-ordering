package com.algaworks.algashop.odering.domain.valueobject;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

import static com.algaworks.algashop.odering.domain.exceptions.ErrorMessages.*;

public record Email(String value) {
    public Email(String value) {
        Objects.requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException(EMAIL_BLANK);
        }
        if (!EmailValidator.getInstance().isValid(value.trim())) {
            throw new IllegalArgumentException(EMAIL_INVALID);
        }
        this.value = value.trim().toLowerCase();
    }

    @Override
    public String toString() {
        return value;
    }
}
