package com.algaworks.algashop.odering.validator;

import com.algaworks.algashop.odering.domain.valueobject.Email;
import org.apache.commons.validator.routines.EmailValidator;

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
