package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.exceptions.ShoppingCartItemIncompatibleProductException;
import com.algaworks.algashop.odering.domain.valueobject.Money;
import com.algaworks.algashop.odering.domain.valueobject.Product;
import com.algaworks.algashop.odering.domain.valueobject.ProductName;
import com.algaworks.algashop.odering.domain.valueobject.Quantity;
import com.algaworks.algashop.odering.domain.valueobject.id.ProductId;
import com.algaworks.algashop.odering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.odering.domain.valueobject.id.ShoppingCartItemId;
import lombok.Builder;

import java.util.Objects;

public class ShoppingCartItem {
    private ShoppingCartItemId id;
    private ShoppingCartId shoppingCartId;
    private ProductId productId;
    private ProductName name;
    private Money price;
    private Quantity quantity;
    private Money totalAmount;
    private Boolean available;

    @Builder(builderClassName = "ExistingShoppingCartBuilder", builderMethodName = "existingBuilder")
    public ShoppingCartItem(ShoppingCartItemId id, ShoppingCartId shoppingCartId, ProductId productId,
                            ProductName name, Money price, Quantity quantity, Money totalAmount, Boolean available) {
        this.setId(id);
        this.setShoppingCartId(shoppingCartId);
        this.setProductId(productId);
        this.setName(name);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.setTotalAmount(totalAmount);
        this.setAvailable(available);
    }

    @Builder(builderClassName = "BrandNewShoppingCartItemBuilder", builderMethodName = "brandNewBuilder")
    private static ShoppingCartItem brandNew(ShoppingCartId shoppingCartId, Product product, Quantity quantity) {
        ShoppingCartItem item = new ShoppingCartItem(
                new ShoppingCartItemId(),
                shoppingCartId,
                product.id(),
                product.name(),
                product.price(),
                quantity,
                Money.ZERO,
                product.inStock());
        item.calculateTotals();
        return item;
    }

    public ShoppingCartItemId id() {
        return id;
    }

    public ShoppingCartId shoppingCartId() {
        return shoppingCartId;
    }

    public ProductId productId() {
        return productId;
    }

    public ProductName name() {
        return name;
    }

    public Money price() {
        return price;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void changeName(ProductName name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public void changeQuantity(Quantity quantity) {
        Objects.requireNonNull(quantity);
        if (quantity.value() < 1) {
            throw new IllegalArgumentException();
        }
        setQuantity(quantity);
        calculateTotals();
    }

    public void changePrice(Money price) {
        Objects.requireNonNull(price);
        this.price = price;
        calculateTotals();
    }

    public void changeAvailable(Boolean available) {
        Objects.requireNonNull(available);
        this.available = available;
    }

    void refresh(Product product) {
        if (this.productId.equals(product.id())) {
            throw new ShoppingCartItemIncompatibleProductException(this.shoppingCartId(), product.id());
        }
        this.changeName(product.name());
        this.changePrice(product.price());
        this.changeAvailable(product.inStock());
        calculateTotals();
    }

    void calculateTotals() {
        this.totalAmount = this.price().multiply(this.quantity());
    }

    private void setId(ShoppingCartItemId id) {
        this.id = id;
    }

    private void setShoppingCartId(ShoppingCartId shoppingCartId) {
        Objects.requireNonNull(shoppingCartId);
        this.shoppingCartId = shoppingCartId;
    }

    private void setProductId(ProductId productId) {
        Objects.requireNonNull(productId);
        this.productId = productId;
    }

    private void setName(ProductName name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    private void setPrice(Money price) {
        Objects.requireNonNull(price);
        this.price = price;
    }

    private void setQuantity(Quantity quantity) {
        Objects.requireNonNull(quantity);
        this.quantity = quantity;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setAvailable(Boolean available) {
        Objects.requireNonNull(available);
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartItem that = (ShoppingCartItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
