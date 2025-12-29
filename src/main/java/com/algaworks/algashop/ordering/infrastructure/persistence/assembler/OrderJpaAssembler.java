package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderItemJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderJpaAssembler {
    private final CustomerJpaRepository customerJpaRepository;

    public OrderJpaEntity fromDomain(Order order) {
        return merge(new OrderJpaEntity(), order);
    }

    public OrderJpaEntity merge(OrderJpaEntity entity, Order order) {
        entity.setId(order.id().value().toLong());
        entity.setCustomer(customerJpaRepository.getReferenceById(order.customerId().value()));
        entity.setTotalAmount(order.totalAmount().value());
        entity.setTotalItems(order.totalItems().value());
        entity.setStatus(order.status().name());
        entity.setPaymentMethod(order.paymentMethod().name());
        entity.setPlacedAt(order.placedAt());
        entity.setPaidAt(order.paidAt());
        entity.setCanceledAt(order.canceledAt());
        entity.setReadyAt(order.readyAt());
        entity.setVersion(order.version());
        entity.setBilling(getBilling(order));
        entity.setShipping(getShipping(order));
        Set<OrderItemJpaEntity> mergedItems = mergeItems(order, entity);
        entity.replaceItems(mergedItems);
        return entity;
    }

    private Set<OrderItemJpaEntity> mergeItems(Order order, OrderJpaEntity entity) {
        Set<OrderItem> currentItems = order.items();

        if (currentItems == null || currentItems.isEmpty()) {
            return new HashSet<>();
        }

        Set<OrderItemJpaEntity> existingEntityItems = entity.getItems();
        if (existingEntityItems == null || existingEntityItems.isEmpty()) {
            return currentItems.stream()
                            .map(this::fromDomain)
                            .collect(Collectors.toSet());
        }

        Map<Long, OrderItemJpaEntity> existingItemsMap = existingEntityItems.stream()
                .collect(Collectors.toMap(OrderItemJpaEntity::getId, item -> item));

        return currentItems.stream()
                .map(orderItem -> {
                    OrderItemJpaEntity entityItem = existingItemsMap.getOrDefault(
                            orderItem.id().value().toLong(), new OrderItemJpaEntity());
                    return merge(entityItem, orderItem);
                })
                .collect(Collectors.toSet());
    }

    public OrderItemJpaEntity fromDomain(OrderItem orderItem) {
        return merge(new OrderItemJpaEntity(), orderItem);
    }

    private OrderItemJpaEntity merge(OrderItemJpaEntity orderItemJpaEntity, OrderItem orderItem) {
        orderItemJpaEntity.setId(orderItem.id().value().toLong());
        orderItemJpaEntity.setProductId(orderItem.productId().value());
        orderItemJpaEntity.setProductName(orderItem.productName().value());
        orderItemJpaEntity.setProductPrice(orderItem.price().value());
        orderItemJpaEntity.setQuantity(orderItem.quantity().value());
        orderItemJpaEntity.setTotalAmount(orderItem.totalAmount().value());
        return orderItemJpaEntity;
    }

    private BillingEmbeddable getBilling(Order order) {
        AddressEmbeddable billingAddress = AddressEmbeddable.builder()
                .street(order.billing().address().street())
                .additionalInfo(order.billing().address().additionalInfo())
                .neighborhood(order.billing().address().neighborhood())
                .city(order.billing().address().city())
                .state(order.billing().address().state())
                .zipcode(order.billing().address().zipcode().value())
                .build();

        return BillingEmbeddable.builder()
                .firstName(order.billing().fullName().firstName())
                .lastName(order.billing().fullName().lastName())
                .document(order.billing().document().value())
                .phone(order.billing().phone().value())
                .email(order.billing().email().value())
                .address(billingAddress)
                .build();
    }

    private ShippingEmbeddable getShipping(Order order) {
        AddressEmbeddable shippingAddress = AddressEmbeddable.builder()
                .street(order.shipping().address().street())
                .additionalInfo(order.shipping().address().additionalInfo())
                .neighborhood(order.shipping().address().neighborhood())
                .city(order.shipping().address().city())
                .state(order.shipping().address().state())
                .zipcode(order.shipping().address().zipcode().value())
                .build();

        RecipientEmbeddable shippingRecipient = RecipientEmbeddable.builder()
                .firstName(order.shipping().recipient().fullName().firstName())
                .lastName(order.shipping().recipient().fullName().lastName())
                .document(order.shipping().recipient().document().value())
                .phone(order.shipping().recipient().phone().value())
                .build();

        return ShippingEmbeddable.builder()
                .cost(order.shipping().cost().value())
                .expectedDate(order.shipping().expectedDate())
                .recipient(shippingRecipient)
                .address(shippingAddress)
                .build();
    }
}
