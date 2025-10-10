package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exceptions.ShoppingCartItemDoesNotExistException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ShoppingCart implements AggregateRoot<ShoppingCartId> {
    private ShoppingCartId id;
    private CustomerId customerId;
    private Money totalAmount;
    private Quantity totalItems;
    private Set<ShoppingCartItem> items;

    public ShoppingCart(ShoppingCartId id, CustomerId customerId, Money totalAmount, Quantity totalItems,
                        Set<ShoppingCartItem> items) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setTotalAmount(totalAmount);
        this.setTotalItems(totalItems);
        this.setItems(items);
    }

    public static ShoppingCart startShopping(CustomerId customerId) {
        return new ShoppingCart(
                new ShoppingCartId(),
                customerId,
                Money.ZERO,
                Quantity.ZERO,
                new HashSet<>()
        );
    }

    public void empty() {
        this.items().forEach(i -> this.removeItem(i.id()));
    }

    public void addItem(Product product, Quantity quantity) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);
        product.checkOutOfStock();

        this.items.stream()
                .filter(it -> it.productId().equals(product.id()))
                .findFirst()
                .ifPresentOrElse(it -> {
                            it.changeQuantity(it.quantity().add(quantity));
                            it.changePrice(product.price());
                        }, () -> this.items.add(ShoppingCartItem.brandNewBuilder()
                                .shoppingCartId(this.id())
                                .product(product)
                                .quantity(quantity)
                                .build()
                        )
                );
        this.calculateTotals();
    }

    public void removeItem(ShoppingCartItemId itemId) {
        Objects.requireNonNull(itemId);
        ShoppingCartItem item = findItem(itemId);
        this.items.remove(item);
        calculateTotals();
    }

    public void refreshItem(Product product) {
        Objects.requireNonNull(product);
        ShoppingCartItem item = this.findItem(product.id());
        item.refresh(product);
        this.calculateTotals();
    }

    public void changeItemQuantity(ShoppingCartItemId itemId, Quantity quantity) {
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(quantity);
        ShoppingCartItem item = this.findItem(itemId);
        item.changeQuantity(quantity);
        calculateTotals();
    }

    public ShoppingCartItem findItem(ShoppingCartItemId itemId) {
        return this.items().stream()
                .filter(it -> it.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartItemDoesNotExistException(this.id(), itemId));
    }

    public ShoppingCartItem findItem(ProductId productId) {
        return this.items().stream()
                .filter(it -> it.productId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartItemDoesNotExistException(this.id(), productId));
    }

    public void calculateTotals() {
        this.setTotalAmount(Money.ZERO);
        this.setTotalItems(Quantity.ZERO);

        this.items().forEach(item -> {
            this.setTotalAmount(this.totalAmount().add(item.totalAmount()));
            this.setTotalItems(this.totalItems().add(item.quantity()));
        });
    }

    public Boolean containsUnavailableItems() {
        return this.items().stream().anyMatch(ShoppingCartItem::isAvailable);
    }

    public Boolean isEmpty() {
        return items.isEmpty();
    }

    public ShoppingCartId id() {
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

    public Set<ShoppingCartItem> items() {
        if (items == null) {
            items = new HashSet<>();
        }
        return Collections.unmodifiableSet(this.items);
    }

    private void setId(ShoppingCartId id) {
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

    private void setItems(Set<ShoppingCartItem> items) {
        Objects.requireNonNull(items);
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
