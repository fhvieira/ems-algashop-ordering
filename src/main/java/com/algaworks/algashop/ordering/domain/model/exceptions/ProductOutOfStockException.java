package com.algaworks.algashop.ordering.domain.model.exceptions;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;

import static com.algaworks.algashop.ordering.domain.model.exceptions.ErrorMessages.PRODUCT_OUT_OF_STOCK;

public class ProductOutOfStockException extends DomainException {

    public ProductOutOfStockException(ProductId productId) {
        super(String.format(PRODUCT_OUT_OF_STOCK, productId));
    }
}
