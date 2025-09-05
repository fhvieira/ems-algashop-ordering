package com.algaworks.algashop.odering.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class LoyaltyPointsTest {

    @Test
    void shouldGenerateWithValue() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        assertThat(loyaltyPoints.value()).isEqualTo(10);
    }

    @Test
    void shouldAddWithValue() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        LoyaltyPoints LoyaltyPointsUpdated = loyaltyPoints.add(5);
        assertThat(LoyaltyPointsUpdated.value()).isEqualTo(15);
    }

    @Test
    void shouldNotAddNonPositiveNegativeValue() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> loyaltyPoints.add(-1));

        assertThat(loyaltyPoints.value()).isEqualTo(10);
    }

    @Test
    void shouldNotZeroValue() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> loyaltyPoints.add(0));

        assertThat(loyaltyPoints.value()).isEqualTo(10);
    }

}