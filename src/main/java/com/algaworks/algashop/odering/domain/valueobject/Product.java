package com.algaworks.algashop.odering.domain.valueobject;

public record Product(
        ProductId id,
        String name,
        Money price,
        Boolean inStock
) {
}
