package com.algaworks.algashop.odering.domain.valueobject;

import lombok.Builder;

import java.util.Objects;

import static com.algaworks.algashop.odering.validator.FieldValidation.requireNonBlank;

@Builder(toBuilder = true)
public record Address(
        String street,
        String additionalInfo,
        String neighborhood,
        String city,
        String state,
        Zipcode zipcode
) {
    public Address {
        requireNonBlank(street);
        requireNonBlank(neighborhood);
        requireNonBlank(city);
        requireNonBlank(state);
        Objects.requireNonNull(zipcode);
    }
}
