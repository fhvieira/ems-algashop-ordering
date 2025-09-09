package com.algaworks.algashop.odering.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money>{
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal value) {
        Objects.requireNonNull(value);
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        this.value = value.setScale(2, ROUNDING_MODE);
    }

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money add(Money other) {
        return new Money(this.value().add(other.value()));
    }

    public Money multiply(Quantity quantity) {
        if (quantity.value() < 1) {
            throw new IllegalArgumentException();
        }
        return new Money(this.value().multiply(BigDecimal.valueOf(quantity.value())));
    }

    public Money divide(Money divisor) {
        return new Money(this.value().divide(divisor.value(), 2, ROUNDING_MODE));
    }

    @Override
    public int compareTo(Money o) {
        return this.value.compareTo(o.value());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
