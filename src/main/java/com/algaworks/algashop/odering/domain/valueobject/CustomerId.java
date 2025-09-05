package com.algaworks.algashop.odering.domain.valueobject;

import com.algaworks.algashop.odering.domain.utility.IdGenerator;
import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID value) {
    public CustomerId(UUID value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public CustomerId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    @Override
    @Nonnull
    public String toString() {
        return value.toString();
    }
}
