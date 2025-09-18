package com.algaworks.algashop.odering.domain.valueobject.id;

import com.algaworks.algashop.odering.domain.utility.IdGenerator;
import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID value) {
    public ProductId {
        Objects.requireNonNull(value);
    }

    public ProductId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    @Override
    @Nonnull
    public String toString() {
        return value.toString();
    }
}
