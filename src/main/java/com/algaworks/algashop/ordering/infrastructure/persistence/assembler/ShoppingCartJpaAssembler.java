package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShoppingCartJpaAssembler {

    private final CustomerJpaRepository customerJpaRepository;

    public ShoppingCartJpaEntity fromDomain(ShoppingCart shoppingCart) {
        return merge(new ShoppingCartJpaEntity(), shoppingCart);
    }

    public ShoppingCartJpaEntity merge(ShoppingCartJpaEntity entity, ShoppingCart shoppingCart) {
        entity.setId(shoppingCart.id().value());
        entity.setTotalAmount(shoppingCart.totalAmount().value());
        entity.setTotalItems(shoppingCart.totalItems().value());
        entity.setCustomer(customerJpaRepository.getReferenceById(shoppingCart.customerId().value()));
        entity.replaceItems(mergeItems(shoppingCart.items()));
        return entity;
    }

    private Set<ShoppingCartItemJpaEntity> mergeItems(Set<ShoppingCartItem> cartItems) {
        return cartItems.stream().map(this::mergeItem)
                .collect(Collectors.toSet());
    }

    public ShoppingCartItemJpaEntity mergeItem(ShoppingCartItem cartItem) {
        ShoppingCartItemJpaEntity entityItem =  new ShoppingCartItemJpaEntity();
        entityItem.setId(cartItem.id().value());
        entityItem.setProductId(cartItem.productId().value());
        entityItem.setName(cartItem.name().value());
        entityItem.setPrice(cartItem.price().value());
        entityItem.setQuantity(cartItem.quantity().value());
        entityItem.setTotalAmount(cartItem.totalAmount().value());
        entityItem.setAvailable(cartItem.isAvailable());
        return entityItem;
    }

}
