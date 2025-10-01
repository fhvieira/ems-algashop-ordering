package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.valueobject.Money;
import com.algaworks.algashop.odering.domain.valueobject.Product;
import com.algaworks.algashop.odering.domain.valueobject.ProductName;
import com.algaworks.algashop.odering.domain.valueobject.id.ProductId;

public class ProductTestDataBuilder {
    private ProductTestDataBuilder() {
    }

    public static Product.ProductBuilder aProduct() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("product name"))
                .price(new Money("100"))
                .inStock(true);
    }

    public static Product.ProductBuilder anAltProduct() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("alternative product name"))
                .price(new Money("70"))
                .inStock(true);
    }

    public static Product.ProductBuilder anOutOfStockProduct() {
        return Product.builder()
                .id(new ProductId())
                .name(new ProductName("out of stock product name"))
                .price(new Money("10"))
                .inStock(false);
    }
}
