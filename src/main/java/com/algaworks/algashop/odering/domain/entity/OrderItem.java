package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.valueobject.*;

public class OrderItem {
    private OrderItemId id;
    private ProductId productId;
    private Money price;
    private Quantity quantity;
    private Money totalAmount;
    private OrderId orderId;
}
