package com.algaworks.algashop.odering.domain.valueobject;

import java.time.LocalDate;
import java.util.Objects;

import static com.algaworks.algashop.odering.domain.exceptions.ErrorMessages.CUSTOMER_BIRTHDATE_INVALID;
import static java.time.temporal.ChronoUnit.YEARS;

public record BirthDate(LocalDate value) {
    public BirthDate(LocalDate value) {
        Objects.requireNonNull(value);

        if (value.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(CUSTOMER_BIRTHDATE_INVALID);
        }

        this.value = value;
    }

    public Integer age() {
        return (int) YEARS.between(value, LocalDate.now());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
