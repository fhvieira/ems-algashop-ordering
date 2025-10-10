package com.algaworks.algashop.ordering.domain.model.exceptions;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;

import static com.algaworks.algashop.ordering.domain.model.exceptions.ErrorMessages.*;

public class ShoppingCartItemIncompatibleProductException extends DomainException {

    public ShoppingCartItemIncompatibleProductException(ShoppingCartId cartId, ProductId productId) {
        super(String.format(SHOPPING_CART_ITEM_IMCOPATIBLE_PRODUCT, cartId, productId));
    }
}
