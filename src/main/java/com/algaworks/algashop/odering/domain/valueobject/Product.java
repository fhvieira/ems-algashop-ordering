package com.algaworks.algashop.odering.domain.valueobject;

import com.algaworks.algashop.odering.domain.valueobject.id.ProductId;

public record Product(
        ProductId id,
        String name,
        Money price,
        Boolean inStock
) {
}
