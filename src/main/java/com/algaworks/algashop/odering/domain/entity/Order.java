package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.valueobject.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public class Order {
    private OrderId id;
    private Money totalAmount;
    private Money shippingCost;
    private Integer quantity;
    private OffsetDateTime placedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime readyAt;
    private LocalDate expectedDeliveryDate;
    private CustomerId customerId;
    private PaymentMethod paymentMethod;
    private OrderStatus orderStatus;
    private List<OrderItem> items;

    private void markAsPaid() {

    }

    private void markAsReady() {

    }

    public void place() {

    }

    public Boolean isPaid() {
        return false;
    }

    public Boolean isCanceled() {
        return false;
    }

    public Boolean isReady() {
        return false;
    }

    public Boolean isDraft() {
        return false;
    }

    public Boolean isPlaced() {
        return false;
    }

    public void addItem(Integer productId, ProductName ProductName, Money price, Integer quantity) {

    }

    public void changeItemQuantity(OrderItemId orderItemId, Integer quantity) {

    }

    public void removeItem(OrderItemId orderItemId) {

    }

    public void changePaymentMethod(PaymentMethod paymentMethod) {

    }
}
