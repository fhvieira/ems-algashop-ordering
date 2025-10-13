package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class OrderEntityAssemblerTest {
    @Autowired
    private final OrderEntityAssembler assembler;

    OrderEntityAssemblerTest(OrderEntityAssembler assembler) {
        this.assembler = assembler;
    }

    @Test
    void shouldConvertFromDomain() {
        Order order = OrderTestDataBuilder.brandNewBuilder().build();
        OrderEntity entity = assembler.fromDomain(order);
        assertThat(entity).satisfies(
            e -> assertThat(e.getId()).isEqualTo(order.id().value().toLong()),
            e -> assertThat(e.getTotalAmount()).isEqualTo(order.totalAmount().value()),
            e -> assertThat(e.getTotalItems()).isEqualTo(order.totalItems().value()),
            e -> assertThat(e.getStatus()).isEqualTo(order.status().name()),
            e -> assertThat(e.getPaymentMethod()).isEqualTo(order.paymentMethod().name()),
            e -> assertThat(e.getPlacedAt()).isEqualTo(order.placedAt()),
            e -> assertThat(e.getPaidAt()).isEqualTo(order.paidAt()),
            e -> assertThat(e.getCanceledAt()).isEqualTo(order.canceledAt()),
            e -> assertThat(e.getReadyAt()).isEqualTo(order.readyAt())
        );
    }




}