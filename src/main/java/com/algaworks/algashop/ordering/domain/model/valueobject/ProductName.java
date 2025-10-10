package com.algaworks.algashop.ordering.domain.model.valueobject;

import java.util.Objects;

public record ProductName(String value) {

    public ProductName(String value) {
        Objects.requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
