package com.algaworks.algashop.odering.domain.valueobject;

import lombok.Builder;

import java.util.Objects;

import static com.algaworks.algashop.odering.validator.FieldValidation.requireNonBlank;

@Builder(toBuilder = true)
public record Billing(
        FullName fullName,
        Document document,
        Phone phone,
        Email email,
        Address address
) {
    public Billing {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
        Objects.requireNonNull(email);
        Objects.requireNonNull(address);
    }
}
