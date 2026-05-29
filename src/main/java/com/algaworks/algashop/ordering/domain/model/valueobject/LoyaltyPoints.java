package com.algaworks.algashop.ordering.domain.model.valueobject;

import java.math.RoundingMode;
import java.util.Objects;

public record LoyaltyPoints(Integer value) implements Comparable<LoyaltyPoints>{
    public static final LoyaltyPoints ZERO = new LoyaltyPoints(0);
    public static final Money AMOUNT_PER_POINT = new Money("1000");

    public LoyaltyPoints() {
        this(0);
    }

    public LoyaltyPoints(Integer value) {
        Objects.requireNonNull(value);
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public LoyaltyPoints add(Integer value) {
        return this.add(new LoyaltyPoints(value));
    }

    public LoyaltyPoints add(LoyaltyPoints loyaltyPoints) {
        if (loyaltyPoints.value() <= 0) {
            throw new IllegalArgumentException();
        }
        return new LoyaltyPoints(this.value() + loyaltyPoints.value());
    }

    public static LoyaltyPoints basedOn(Money amount) {
        Objects.requireNonNull(amount);

        return new LoyaltyPoints(
                amount.value()
                .divide(AMOUNT_PER_POINT.value(), RoundingMode.DOWN)
                .intValue()
        );
    }

    @Override
    public int compareTo(LoyaltyPoints o) {
        return this.value().compareTo(o.value());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
