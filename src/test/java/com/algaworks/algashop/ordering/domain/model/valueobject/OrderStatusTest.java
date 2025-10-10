package com.algaworks.algashop.ordering.domain.model.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderStatusTest {

    @Test
    void shouldAllowValidTransitions() {
        assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.PLACED)).isTrue();
        assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.PAID)).isTrue();
        assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.READY)).isTrue();

        assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.CANCELED)).isTrue();
        assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.CANCELED)).isTrue();
        assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.CANCELED)).isTrue();
        assertThat(OrderStatus.READY.canChangeTo(OrderStatus.CANCELED)).isTrue();
    }

    @Test
    void shouldNotAllowInvalidTransitions() {
        assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.PAID)).isFalse();
        assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.READY)).isFalse();

        assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.READY)).isFalse();
        assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.DRAFT)).isFalse();

        assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.DRAFT)).isFalse();
        assertThat(OrderStatus.PAID.canChangeTo(OrderStatus.PLACED)).isFalse();

        assertThat(OrderStatus.READY.canChangeTo(OrderStatus.DRAFT)).isFalse();
        assertThat(OrderStatus.READY.canChangeTo(OrderStatus.PLACED)).isFalse();
        assertThat(OrderStatus.READY.canChangeTo(OrderStatus.PAID)).isFalse();
    }

    @Test
    void shouldNotAllowAnyTransitionFromCanceled() {
        assertThat(OrderStatus.CANCELED.canChangeTo(OrderStatus.DRAFT)).isFalse();
        assertThat(OrderStatus.CANCELED.canChangeTo(OrderStatus.PLACED)).isFalse();
        assertThat(OrderStatus.CANCELED.canChangeTo(OrderStatus.PAID)).isFalse();
        assertThat(OrderStatus.CANCELED.canChangeTo(OrderStatus.READY)).isFalse();
    }

    @Test
    void shouldMirrorCanNotChangeTo() {
        assertThat(OrderStatus.DRAFT.canNotChangeTo(OrderStatus.PAID))
                .isEqualTo(!OrderStatus.DRAFT.canChangeTo(OrderStatus.PAID));
        assertThat(OrderStatus.PLACED.canNotChangeTo(OrderStatus.PAID))
                .isEqualTo(!OrderStatus.PLACED.canChangeTo(OrderStatus.PAID));
        assertThat(OrderStatus.PAID.canNotChangeTo(OrderStatus.READY))
                .isEqualTo(!OrderStatus.PAID.canChangeTo(OrderStatus.READY));
        assertThat(OrderStatus.READY.canNotChangeTo(OrderStatus.CANCELED))
                .isEqualTo(!OrderStatus.READY.canChangeTo(OrderStatus.CANCELED));
    }
}


