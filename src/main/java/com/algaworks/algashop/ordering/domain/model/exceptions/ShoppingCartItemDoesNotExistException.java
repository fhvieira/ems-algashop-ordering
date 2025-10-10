package com.algaworks.algashop.ordering.domain.model.exceptions;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;

import static com.algaworks.algashop.ordering.domain.model.exceptions.ErrorMessages.SHOPPING_CART_ITEM_DOES_NOT_EXIST;
import static com.algaworks.algashop.ordering.domain.model.exceptions.ErrorMessages.SHOPPING_CART_ITEM_DOES_NOT_EXIST_FOR_PRODUCT_ID;

public class ShoppingCartItemDoesNotExistException extends DomainException {

    public ShoppingCartItemDoesNotExistException(ShoppingCartId cartId, ShoppingCartItemId itemId) {
        super(String.format(SHOPPING_CART_ITEM_DOES_NOT_EXIST, cartId, itemId));
    }

    public ShoppingCartItemDoesNotExistException(ShoppingCartId cartId, ProductId productId) {
        super(String.format(SHOPPING_CART_ITEM_DOES_NOT_EXIST_FOR_PRODUCT_ID, cartId, productId));
    }
}
