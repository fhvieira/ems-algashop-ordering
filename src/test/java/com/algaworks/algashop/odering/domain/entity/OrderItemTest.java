package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.valueobject.Quantity;
import com.algaworks.algashop.odering.domain.valueobject.id.OrderId;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    public void shouldGenerate() {

        OrderItem orderItem = OrderItem.brandNewBuilder()
                .orderId(new OrderId())
                .product(ProductTestDataBuilder.aProduct().build())
                .quantity(new Quantity(1))
                .build();
    }

}