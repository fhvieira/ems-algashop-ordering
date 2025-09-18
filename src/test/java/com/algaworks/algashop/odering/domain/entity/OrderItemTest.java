package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.valueobject.Money;
import com.algaworks.algashop.odering.domain.valueobject.ProductName;
import com.algaworks.algashop.odering.domain.valueobject.Quantity;
import com.algaworks.algashop.odering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.odering.domain.valueobject.id.ProductId;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    public void shouldGenerate() {
        OrderItem orderItem = OrderItem.brandNewBuilder()
                .orderId(new OrderId())
                .productId(new ProductId())
                .productName(new ProductName("new product"))
                .price(Money.ZERO)
                .quantity(new Quantity(1))
                .build();
    }

}