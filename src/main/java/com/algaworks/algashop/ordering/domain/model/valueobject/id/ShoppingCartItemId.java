package com.algaworks.algashop.ordering.domain.model.valueobject.id;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import io.hypersistence.tsid.TSID;
import jakarta.annotation.Nonnull;

import java.util.Objects;
import java.util.UUID;

public record ShoppingCartItemId(UUID value) {
    public ShoppingCartItemId {
        Objects.requireNonNull(value);
    }

    public ShoppingCartItemId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    public ShoppingCartItemId(String value) {
        this(UUID.fromString(value));
    }

    @Override
    @Nonnull
    public String toString() {
        return value.toString();
    }
}
