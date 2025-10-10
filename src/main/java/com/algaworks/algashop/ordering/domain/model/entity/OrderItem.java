package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import lombok.Builder;

import java.util.Objects;

public class OrderItem {
    private OrderItemId id;
    private OrderId orderId;
    private ProductId productId;
    private ProductName productName;
    private Money productPrice;
    private Quantity quantity;
    private Money totalAmount;

    @Builder(builderClassName = "ExistingOrderItemBuilder", builderMethodName = "existingBuilder")
    public OrderItem(OrderItemId id, OrderId orderId, ProductId productId, ProductName productName,
                     Money productPrice, Quantity quantity, Money totalAmount) {
        setId(id);
        setOrderId(orderId);
        setProductId(productId);
        setProductName(productName);
        setProductPrice(productPrice);
        setQuantity(quantity);
        setTotalAmount(totalAmount);
    }

    @Builder(builderClassName = "BrandNewOrderItem", builderMethodName = "brandNewBuilder")
    private static OrderItem brandNew(OrderId orderId, Product product, Quantity quantity) {
        OrderItem orderItem = new OrderItem(
                new OrderItemId(),
                orderId,
                product.id(),
                product.name(),
                product.price(),
                quantity,
                Money.ZERO);
        orderItem.calculateTotal();
        return orderItem;
    }

    public OrderItemId id() {
        return id;
    }

    public OrderId orderId() {
        return orderId;
    }

    public ProductId productId() {
        return productId;
    }

    public ProductName productName() {
        return productName;
    }

    void changeQuantity(Quantity quantity) {
        this.setQuantity(quantity);
        this.calculateTotal();
    }

    public Money price() {
        return productPrice;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    private void setId(OrderItemId id) {
        this.id = id;
    }

    private void setOrderId(OrderId orderId) {
        Objects.requireNonNull(orderId);
        this.orderId = orderId;
    }

    private void setProductId(ProductId productId) {
        Objects.requireNonNull(productId);
        this.productId = productId;
    }

    private void setProductName(ProductName productName) {
        Objects.requireNonNull(productName);
        this.productName = productName;
    }

    private void setProductPrice(Money productPrice) {
        Objects.requireNonNull(productPrice);
        this.productPrice = productPrice;
    }

    private void setQuantity(Quantity quantity) {
        Objects.requireNonNull(quantity);
        this.quantity = quantity;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void calculateTotal() {
        this.setTotalAmount(this.price().multiply(quantity()));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
