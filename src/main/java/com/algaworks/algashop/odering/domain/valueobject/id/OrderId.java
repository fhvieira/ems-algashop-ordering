package com.algaworks.algashop.odering.domain.valueobject.id;

import com.algaworks.algashop.odering.domain.utility.IdGenerator;
import io.hypersistence.tsid.TSID;
import jakarta.annotation.Nonnull;

import java.util.Objects;

public record OrderId(TSID value) {
    public OrderId {
        Objects.requireNonNull(value);
    }

    public OrderId(Long value) {
        this(TSID.from(value));
    }

    public OrderId(String value) {
        this(TSID.from(value));
    }

    public OrderId() {
        this(IdGenerator.generateTSID());
    }

    @Override
    @Nonnull
    public String toString() {
        return value.toString();
    }
}
