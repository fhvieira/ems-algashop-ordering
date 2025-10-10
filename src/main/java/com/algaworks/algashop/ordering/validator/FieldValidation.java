package com.algaworks.algashop.ordering.validator;

import java.util.Objects;

public class FieldValidation {
    private FieldValidation() {

    }

    public static void requireNonBlank(String field) {
        requireNonBlank(field, "");
    }

    public static void requireNonBlank(String field, String errorMessage) {
        Objects.requireNonNull(field);
        if (field.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
