package com.algaworks.algashop.odering.domain.factory;

import com.algaworks.algashop.odering.domain.entity.Order;
import com.algaworks.algashop.odering.domain.entity.OrderTestDataBuilder;
import com.algaworks.algashop.odering.domain.entity.ProductTestDataBuilder;
import com.algaworks.algashop.odering.domain.valueobject.*;
import com.algaworks.algashop.odering.domain.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;

class OrderFactoryTest {

    @Test
    void shouldPlaceOrder() {
        Order order = OrderFactory.readyToPlace(new CustomerId(), OrderTestDataBuilder.aBilling(),
                OrderTestDataBuilder.aShipping(), PaymentMethod.GATEWAY_BALANCE,
                ProductTestDataBuilder.aProduct().build(), new Quantity(2));

        assertWith(order,
                o -> assertThat(o.customerId()).isNotNull(),
                o -> assertThat(o.billing()).isNotNull(),
                o -> assertThat(o.shipping()).isNotNull(),
                o -> assertThat(o.paymentMethod()).isNotNull(),
                o -> assertThat(o.items().size()).isGreaterThan(0),
                o -> assertThat(o.totalItems()).isGreaterThan(Quantity.ZERO),
                o -> assertThat(o.totalAmount()).isGreaterThan(Money.ZERO)
        );
        order.place();

        assertThat(order.isPlaced()).isTrue();
    }

}