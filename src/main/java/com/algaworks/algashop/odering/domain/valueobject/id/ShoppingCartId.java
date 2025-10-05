package com.algaworks.algashop.odering.domain.valueobject.id;

import com.algaworks.algashop.odering.domain.utility.IdGenerator;
import io.hypersistence.tsid.TSID;
import jakarta.annotation.Nonnull;

import java.util.Objects;

public record ShoppingCartId(TSID value) {
    public ShoppingCartId {
        Objects.requireNonNull(value);
    }

    public ShoppingCartId(Long value) {
        this(TSID.from(value));
    }

    public ShoppingCartId(String value) {
        this(TSID.from(value));
    }

    public ShoppingCartId() {
        this(IdGenerator.generateTSID());
    }

    @Override
    @Nonnull
    public String toString() {
        return value.toString();
    }
}
