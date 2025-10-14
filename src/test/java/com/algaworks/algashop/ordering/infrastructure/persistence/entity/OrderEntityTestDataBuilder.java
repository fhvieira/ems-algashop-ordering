package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderEntity.OrderEntityBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class OrderEntityTestDataBuilder {
    private OrderEntityTestDataBuilder() {

    }

    public static OrderEntityBuilder existingBuilder() {
        return OrderEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customerId(IdGenerator.generateTimeBasedUUID())
                .totalItems(2)
                .totalAmount(new BigDecimal(10))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now());
    }
}
