package com.algaworks.algashop.ordering.domain.model.valueobject;

import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.model.exceptions.ErrorMessages.CUSTOMER_PHONE_BLANK;
import static com.algaworks.algashop.ordering.domain.model.exceptions.ErrorMessages.CUSTOMER_PHONE_INVALID;

public record Phone(String value) {
    public Phone(String value) {
        Objects.requireNonNull(value);

        if (value.isBlank()) {
            throw new IllegalArgumentException(CUSTOMER_PHONE_BLANK);
        }

        String cleanedNumber = value.replaceAll("[^0-9+]", "");
        if (cleanedNumber.length() < 8) {
            throw new IllegalArgumentException(CUSTOMER_PHONE_INVALID);
        }

        this.value = cleanedNumber;
    }

    @Override
    public String toString() {
        return value;
    }
}
