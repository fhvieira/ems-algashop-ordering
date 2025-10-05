package com.algaworks.algashop.odering.domain.valueobject.id;

import com.algaworks.algashop.odering.domain.utility.IdGenerator;
import io.hypersistence.tsid.TSID;
import jakarta.annotation.Nonnull;

import java.util.Objects;

public record ShoppingCartItemId(TSID value) {
    public ShoppingCartItemId {
        Objects.requireNonNull(value);
    }

    public ShoppingCartItemId(Long value) {
        this(TSID.from(value));
    }

    public ShoppingCartItemId(String value) {
        this(TSID.from(value));
    }

    public ShoppingCartItemId() {
        this(IdGenerator.generateTSID());
    }

    @Override
    @Nonnull
    public String toString() {
        return value.toString();
    }
}
