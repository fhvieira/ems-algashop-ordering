package com.algaworks.algashop.ordering.domain.model.valueobject;

import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.model.exceptions.ErrorMessages.CUSTOMER_DOCUMENT_BLANK;

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
