package com.algaworks.algashop.odering.domain.exceptions;

import com.algaworks.algashop.odering.domain.valueobject.id.ProductId;

import static com.algaworks.algashop.odering.domain.exceptions.ErrorMessages.PRODUCT_OUT_OF_STOCK;

public class ProductOutOfStockException extends DomainException {

    public ProductOutOfStockException(ProductId productId) {
        super(String.format(PRODUCT_OUT_OF_STOCK, productId));
    }
}
