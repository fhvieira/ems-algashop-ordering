package com.algaworks.algashop.odering.domain.valueobject;

import java.util.Objects;

import static com.algaworks.algashop.odering.domain.exceptions.ErrorMessages.CUSTOMER_DOCUMENT_BLANK;

public record Document(String value) {
    public Document(String value) {
        Objects.requireNonNull(value);

        if (value.isBlank()) {
            throw new IllegalArgumentException(CUSTOMER_DOCUMENT_BLANK);
        }

        this.value = value.trim();
    }

    @Override
    public String toString() {
        return value;
    }
}
