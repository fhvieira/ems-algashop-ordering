package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class OrderJpaEntityDisassembler {
    public Order toDomainEntity(OrderJpaEntity entity) {
        return Order.existingBuilder()
                .id(new OrderId(entity.getId()))
                .customerId(new CustomerId(entity.getCustomerId()))
                .totalAmount(new Money(entity.getTotalAmount()))
                .totalItems(new Quantity(entity.getTotalItems()))
                .status(OrderStatus.valueOf(entity.getStatus()))
                .paymentMethod(PaymentMethod.valueOf(entity.getPaymentMethod()))
                .placedAt(entity.getPlacedAt())
                .paidAt(entity.getPaidAt())
                .canceledAt(entity.getCanceledAt())
                .readyAt(entity.getReadyAt())
                .items(new HashSet<>())
                .version(entity.getVersion())
                .build();
    }
}
