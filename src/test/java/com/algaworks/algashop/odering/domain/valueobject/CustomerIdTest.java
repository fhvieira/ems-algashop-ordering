package com.algaworks.algashop.odering.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class CustomerIdTest {

    @Test
    void shouldGenerateWithValue() {
        UUID uuid = UUID.randomUUID();
        CustomerId customerId = new CustomerId(uuid);
        assertThat(customerId.value()).isEqualTo(uuid);
    }

    @Test
    void shouldGenerateWithDefaultConstructor() {
        CustomerId customerId = new CustomerId();
        assertThat(customerId.value()).isNotNull();
        assertThat(customerId.value()).isInstanceOf(UUID.class);
    }

    @Test
    void shouldNotAcceptNullValue() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new CustomerId(null));
    }

    @Test
    void shouldReturnCorrectToString() {
        UUID uuid = UUID.randomUUID();
        CustomerId customerId = new CustomerId(uuid);
        assertThat(customerId.toString()).isEqualTo(uuid.toString());
    }

    @Test
    void shouldGenerateDifferentIdsWithDefaultConstructor() {
        CustomerId customerId1 = new CustomerId();
        CustomerId customerId2 = new CustomerId();
        assertThat(customerId1.value()).isNotEqualTo(customerId2.value());
    }
}
