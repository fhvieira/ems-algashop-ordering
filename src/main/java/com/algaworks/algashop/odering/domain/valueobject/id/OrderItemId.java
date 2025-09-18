package com.algaworks.algashop.odering.domain.valueobject.id;

import com.algaworks.algashop.odering.domain.utility.IdGenerator;
import io.hypersistence.tsid.TSID;
import jakarta.annotation.Nonnull;

import java.util.Objects;

public record OrderItemId(TSID value) {
    public OrderItemId {
        Objects.requireNonNull(value);
    }

    public OrderItemId(Long value) {
        this(TSID.from(value));
    }

    public OrderItemId(String value) {
        this(TSID.from(value));
    }

    public OrderItemId() {
        this(IdGenerator.generateTSID());
    }

    @Override
    @Nonnull
    public String toString() {
        return value.toString();
    }
}
