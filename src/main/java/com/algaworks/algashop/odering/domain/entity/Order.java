package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.exceptions.InvalidOrderDeliveryDateException;
import com.algaworks.algashop.odering.domain.exceptions.OrderCannotBePlacedException;
import com.algaworks.algashop.odering.domain.exceptions.OrderItemDoesNotExistException;
import com.algaworks.algashop.odering.domain.exceptions.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.odering.domain.valueobject.*;
import com.algaworks.algashop.odering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.odering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.odering.domain.valueobject.id.OrderItemId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order {
    private OrderId id;
    private CustomerId customerId;
    private Money totalAmount;
    private Quantity totalItems;
    private OffsetDateTime placedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime readyAt;
    private Billing billing;
    private Shipping shipping;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private Set<OrderItem> items;

    @Builder(builderClassName = "ExistingOrderBuilder", builderMethodName = "existingBuilder")
    public Order(OrderId id, CustomerId customerId, Money totalAmount, Quantity totalItems, OffsetDateTime placedAt,
                 OffsetDateTime paidAt, OffsetDateTime canceledAt, OffsetDateTime readyAt, Billing billing,
                 Shipping shipping, OrderStatus status, PaymentMethod paymentMethod, Set<OrderItem> items) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setTotalAmount(totalAmount);
        this.setTotalItems(totalItems);
        this.setPlacedAt(placedAt);
        this.setPaidAt(paidAt);
        this.setCanceledAt(canceledAt);
        this.setReadyAt(readyAt);
        this.setBilling(billing);
        this.setShipping(shipping);
        this.setStatus(status);
        this.setPaymentMethod(paymentMethod);
        this.setItems(items);
    }

    public static Order draft(CustomerId customerId) {
        return new Order(
                new OrderId(),
                customerId,
                Money.ZERO,
                Quantity.ZERO,
                null,
                null,
                null,
                null,
                null,
                null,
                OrderStatus.DRAFT,
                null,
                new HashSet<>()
        );
    }

    public void addItem(Product product, Quantity quantity) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);
        product.checkOutOfStock();

        this.items.add(OrderItem.brandNewBuilder()
                .orderId(this.id())
                .product(product)
                .quantity(quantity)
                .build()
        );
        calculateTotals();
    }

    public void changeItemQuantity(OrderItemId itemId, Quantity quantity) {
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(quantity);

        OrderItem item = items().stream()
                .filter(it -> it.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new OrderItemDoesNotExistException(this.id(), itemId));

        item.changeQuantity(quantity);
        this.calculateTotals();
    }

    public void changePaymentMothod(PaymentMethod newPaymentMethod) {
        Objects.requireNonNull(newPaymentMethod);
        setPaymentMethod(newPaymentMethod);
    }

    public void changeBilling(Billing newBilling) {
        Objects.requireNonNull(newBilling);
        setBilling(newBilling);
    }

    public void changeShipping(Shipping newShipping) {
        Objects.requireNonNull(newShipping);

        if (newShipping.expectedDate().isBefore(LocalDate.now())) {
            throw new InvalidOrderDeliveryDateException(this.id());
        }

        this.setShipping(newShipping);
        this.calculateTotals();
    }

    public void place() {
        this.canChangeToPlaced();
        this.changeStatus(OrderStatus.PLACED);
        setPlacedAt(OffsetDateTime.now());
    }

    public void markAsPaid() {
        this.setPaidAt(OffsetDateTime.now());
        this.changeStatus(OrderStatus.PAID);
    }

    private void changeStatus(OrderStatus newStatus) {
        Objects.requireNonNull(newStatus);
        if (status.canNotChangeTo(newStatus)) {
            throw new OrderStatusCannotBeChangedException(this.id(), this.status(), newStatus);
        }
        this.setStatus(newStatus);
    }

    private void calculateTotals() {
         this.items().stream()
                .map(OrderItem::totalAmount)
                .reduce(Money::add)
                .ifPresent(this::setTotalAmount);
        this.items().stream()
                .map(OrderItem::quantity)
                .reduce(Quantity::add)
                .ifPresent(this::setTotalItems);

        BigDecimal shippingCost;
        if(this.shipping() == null) {
            shippingCost = BigDecimal.ZERO;
        } else {
            shippingCost = this.shipping().cost().value();
        }

        this.totalAmount.add(new Money(shippingCost));
    }

    public Boolean isPaid() {
        return OrderStatus.PAID.equals(this.status());
    }

    public Boolean isCanceled() {
        return OrderStatus.CANCELED.equals(this.status());
    }

    public Boolean isReady() {
        return OrderStatus.READY.equals(this.status());
    }

    public Boolean isDraft() {
        return OrderStatus.READY.equals(this.status());
    }

    public Boolean isPlaced() {
        return OrderStatus.PLACED.equals(this.status());
    }

    public OrderId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    public OffsetDateTime placedAt() {
        return placedAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    public OffsetDateTime canceledAt() {
        return canceledAt;
    }

    public OffsetDateTime readyAt() {
        return readyAt;
    }

    public Billing billing() {
        return billing;
    }

    public Shipping shipping() {
        return shipping;
    }

    public OrderStatus status() {
        return status;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    public Set<OrderItem> items() {
        if (items == null) {
            items = new HashSet<>();
        }
        return Collections.unmodifiableSet(this.items);
    }

    private void setId(OrderId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setCustomerId(CustomerId customerId) {
        Objects.requireNonNull(customerId);
        this.customerId = customerId;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setTotalItems(Quantity totalItems) {
        Objects.requireNonNull(totalItems);
        this.totalItems = totalItems;
    }

    private void setPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
    }

    private void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    private void setCanceledAt(OffsetDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    private void setReadyAt(OffsetDateTime readyAt) {
        this.readyAt = readyAt;
    }

    private void setBilling(Billing billing) {
        this.billing = billing;
    }

    private void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    private void setStatus(OrderStatus status) {
        this.status = status;
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private void setItems(Set<OrderItem> items) {
        Objects.requireNonNull(items);
        this.items = items;
    }

    private void canChangeToPlaced() {
        if (this.billing() == null) {
            throw OrderCannotBePlacedException.noBillingInfo(this.id());
        }
        if (this.paymentMethod() == null) {
            throw OrderCannotBePlacedException.noPaymentMethod(this.id());
        }
        if (items == null || items().isEmpty()) {
            throw OrderCannotBePlacedException.noItems(this.id());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
