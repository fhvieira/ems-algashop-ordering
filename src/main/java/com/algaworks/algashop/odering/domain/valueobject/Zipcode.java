package com.algaworks.algashop.odering.domain.valueobject;

import lombok.Builder;

import java.util.Objects;

import static com.algaworks.algashop.odering.domain.exceptions.ErrorMessages.ZIPCODE_INVALID;

@Builder
public record Zipcode(String value) {
    private static final String BRAZIL_REGEX = "^\\d{5}-\\d{3}$";

    public Zipcode {
        Objects.requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException();
        }
        if ((value.trim().length() != 5) && !value.trim().matches(BRAZIL_REGEX)) {
            throw new IllegalArgumentException(ZIPCODE_INVALID);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
