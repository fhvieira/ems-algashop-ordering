package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.exceptions.ShoppingCartItemDoesNotExistException;
import com.algaworks.algashop.odering.domain.valueobject.Money;
import com.algaworks.algashop.odering.domain.valueobject.Product;
import com.algaworks.algashop.odering.domain.valueobject.Quantity;
import com.algaworks.algashop.odering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.odering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.odering.domain.valueobject.id.ShoppingCartItemId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ShoppingCartItemTest {

    @Test
    void shouldCalculateTotalAmountForNewItem() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        Product product = ProductTestDataBuilder.aProduct().build();
        Quantity expectedQuantity = new Quantity(2);
        Money expectedTotalAmount = product.price().multiply(expectedQuantity);

        cart.addItem(product, expectedQuantity);

        assertThat(cart.totalAmount()).isEqualTo(expectedTotalAmount);
    }

    @Test
    void shouldThrowExceptionTryingToChangeQuantityToZero() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        Product product = ProductTestDataBuilder.aProduct().build();
        Quantity expectedQuantity = new Quantity(2);

        cart.addItem(product, expectedQuantity);
        ShoppingCartItemId itemId = cart.findItem(product.id()).id();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> cart.changeItemQuantity(itemId, Quantity.ZERO));
    }

    @Test
    void shouldThrowExceptionTryingToChangeIncompatibleProduct() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        Product product = ProductTestDataBuilder.aProduct().build();
        Quantity expectedQuantity = new Quantity(2);

        cart.addItem(product, expectedQuantity);

        assertThatExceptionOfType(ShoppingCartItemDoesNotExistException.class)
                .isThrownBy(() -> cart.changeItemQuantity(new ShoppingCartItemId(), new Quantity(1)));
    }

    @Test
    void shouldBeEqualById() {
        ShoppingCartItemId itemId = new ShoppingCartItemId();
        ShoppingCartId cartId = new ShoppingCartId();
        Product product = ProductTestDataBuilder.aProduct().build();

        ShoppingCartItem item1 = ShoppingCartItem.existingBuilder()
                .id(itemId)
                .shoppingCartId(cartId)
                .productId(product.id())
                .name(product.name())
                .price(product.price())
                .quantity(new Quantity(1))
                .totalAmount(new Money("10"))
                .available(product.inStock())
                .build();

        ShoppingCartItem item2 = ShoppingCartItem.existingBuilder()
                .id(itemId)
                .shoppingCartId(cartId)
                .productId(product.id())
                .name(product.name())
                .price(product.price())
                .quantity(new Quantity(1))
                .totalAmount(new Money("10"))
                .available(product.inStock())
                .build();

        assertThat(item1).isEqualTo(item2);

    }

}
