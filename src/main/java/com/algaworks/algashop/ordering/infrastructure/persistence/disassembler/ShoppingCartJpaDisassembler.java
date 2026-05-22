package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShoppingCartJpaDisassembler {

    public ShoppingCart toDomain(ShoppingCartJpaEntity entity) {
        return ShoppingCart.existing()
                .id(new ShoppingCartId(entity.getId()))
                .version(entity.getVersion())
                .customerId(new CustomerId(entity.getCustomer().getId()))
                .totalAmount(new Money(entity.getTotalAmount()))
                .totalItems(new Quantity(entity.getTotalItems()))
                .items(getItems(entity))
                .build();
    }

    private Set<ShoppingCartItem> getItems(ShoppingCartJpaEntity entity) {
        return entity.getItems().stream()
                .map(item -> ShoppingCartItem.existingBuilder()
                        .id(new ShoppingCartItemId(item.getId()))
                        .shoppingCartId(new ShoppingCartId(entity.getId()))
                        .productId(new ProductId(item.getProductId()))
                        .name(new ProductName(item.getName()))
                        .price(new Money(item.getPrice()))
                        .quantity(new Quantity(item.getQuantity()))
                        .totalAmount(new Money(item.getTotalAmount()))
                        .available(item.getAvailable())
                        .build())
                .collect(Collectors.toSet());
    }

}
