package com.algaworks.algashop.odering.domain.exceptions;

import com.algaworks.algashop.odering.domain.valueobject.id.ProductId;
import com.algaworks.algashop.odering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.odering.domain.valueobject.id.ShoppingCartItemId;

import static com.algaworks.algashop.odering.domain.exceptions.ErrorMessages.*;

public class ShoppingCartItemIncompatibleProductException extends DomainException {

    public ShoppingCartItemIncompatibleProductException(ShoppingCartId cartId, ProductId productId) {
        super(String.format(SHOPPING_CART_ITEM_IMCOPATIBLE_PRODUCT, cartId, productId));
    }
}
