package com.algaworks.algashop.odering.domain.valueobject;

import lombok.Builder;

import java.util.Objects;

import static com.algaworks.algashop.odering.validator.FieldValidation.requireNonBlank;

@Builder(toBuilder = true)
public record ShippingInfo(
        FullName fullName,
        Document document,
        Phone phone,
        Address address
) {
    public ShippingInfo {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(document);
        Objects.requireNonNull(phone);
        Objects.requireNonNull(address);
    }
}
