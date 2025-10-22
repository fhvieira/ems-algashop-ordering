package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderJpaEntityAssembler {
    public OrderJpaEntity fromDomain(Order order) {
        return merge(new OrderJpaEntity(), order);
    }

    public OrderJpaEntity merge(OrderJpaEntity entity, Order order) {
        entity.setId(order.id().value().toLong());
        entity.setCustomerId(order.customerId().value());
        entity.setTotalAmount(order.totalAmount().value());
        entity.setTotalItems(order.totalItems().value());
        entity.setStatus(order.status().name());
        entity.setPaymentMethod(order.paymentMethod().name());
        entity.setPlacedAt(order.placedAt());
        entity.setPaidAt(order.paidAt());
        entity.setCanceledAt(order.canceledAt());
        entity.setReadyAt(order.readyAt());
        entity.setVersion(order.version());
        return entity;
    }
}
