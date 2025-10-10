package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;

class OrderItemTest {

    @Test
    public void shouldGenerate() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Quantity quantity = new Quantity(1);
        OrderId orderId = new OrderId();

        OrderItem orderItem = OrderItem.brandNewBuilder()
                .orderId(orderId)
                .product(product)
                .quantity(quantity)
                .build();

        assertWith(orderItem,
                oi -> assertThat(oi.id()).isNotNull(),
                oi -> assertThat(oi.orderId()).isEqualTo(orderId),
                oi -> assertThat(oi.productId()).isEqualTo(product.id()),
                oi -> assertThat(oi.productName()).isEqualTo(product.name()),
                oi -> assertThat(oi.price()).isEqualTo(product.price()),
                oi -> assertThat(oi.quantity()).isEqualTo(quantity)
        );
    }

}