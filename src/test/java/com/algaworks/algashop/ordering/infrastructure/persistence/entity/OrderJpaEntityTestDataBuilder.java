package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity.OrderJpaEntityBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderJpaEntityTestDataBuilder {
    private OrderJpaEntityTestDataBuilder() {

    }

    public static OrderJpaEntityBuilder existingBuilder() {
        return OrderJpaEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customerId(IdGenerator.generateTimeBasedUUID())
                .totalItems(2)
                .totalAmount(new BigDecimal(10))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now());
    }
}
