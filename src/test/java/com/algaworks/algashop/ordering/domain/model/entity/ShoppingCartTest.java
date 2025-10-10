package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exceptions.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.model.exceptions.ShoppingCartItemDoesNotExistException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class ShoppingCartTest {

    @Test
    void shouldStartShoppingCartWithEmptyCart() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        assertThat(cart.items()).isEmpty();
    }

    @Test
    void shouldThrowExceptionTryingToAddOutOfStockProduct() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        Product outOfStockProduct = ProductTestDataBuilder.aProduct().inStock(false).build();
        assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> cart.addItem(outOfStockProduct, new Quantity(1)));
    }

    @Test
    void shouldAddUpQuantityAndCalculateTotalsWhenAddingTheSameProductTwice() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        Product product = ProductTestDataBuilder.aProduct().build();

        Quantity expectedTotalItems = new Quantity(2);
        Money expectedTotalAmount = product.price().multiply(expectedTotalItems);

        cart.addItem(product, new Quantity(1));
        cart.addItem(product, new Quantity(1));

        assertThat(cart.items().size()).isEqualTo(1);
        assertThat(cart.totalItems()).isEqualTo(expectedTotalItems);
        assertThat(cart.totalAmount()).isEqualTo(expectedTotalAmount);
    }

    @Test
    void shouldAddTwoDifferentProductsAndCalculateTotals() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        Product product = ProductTestDataBuilder.aProduct().build();
        Product altProduct = ProductTestDataBuilder.anAltProduct().build();

        Money expectedTotalAmount = product.price().add(altProduct.price());

        cart.addItem(product, new Quantity(1));
        cart.addItem(altProduct, new Quantity(1));

        assertThat(cart.items().size()).isEqualTo(2);
        assertThat(cart.totalItems()).isEqualTo(new Quantity(2));
        assertThat(cart.totalAmount()).isEqualTo(expectedTotalAmount);
    }

    @Test
    void shouldThrowExceptionTryingToRemoveNonExistingItem() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());

        assertThatExceptionOfType(ShoppingCartItemDoesNotExistException.class)
                .isThrownBy(() -> cart.removeItem(new ShoppingCartItemId()));
    }

    @Test
    void shouldEmptyCartAndCalculateTotals() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        Product product = ProductTestDataBuilder.aProduct().build();

        cart.addItem(product, new Quantity(1));
        cart.empty();

        assertThat(cart.items().size()).isEqualTo(0);
        assertThat(cart.totalItems()).isEqualTo(Quantity.ZERO);
        assertThat(cart.totalAmount()).isEqualTo(Money.ZERO);
    }

    @Test
    void shouldThrowExceptionTryingToUpdateNonExistingProduct() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());

        assertThatExceptionOfType(ShoppingCartItemDoesNotExistException.class)
                .isThrownBy(() -> cart.refreshItem(ProductTestDataBuilder.aProduct().build()));
    }

    @Test
    void shouldBeEqualById() {
        ShoppingCartId shoppingCartId = new ShoppingCartId();
        ShoppingCart cart1 = new ShoppingCart(
                shoppingCartId,
                new CustomerId(),
                Money.ZERO,
                Quantity.ZERO,
                new HashSet<>());

        ShoppingCart cart2 = new ShoppingCart(
                shoppingCartId,
                new CustomerId(),
                Money.ZERO,
                Quantity.ZERO,
                new HashSet<>());

        assertThat(cart1).isEqualTo(cart2);
    }

}