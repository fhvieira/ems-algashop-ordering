package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderItemJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntityTestDataBuilder;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class OrderJpaEntityAssemblerTest {
    private final OrderJpaEntityAssembler assembler = new OrderJpaEntityAssembler();

    @Test
    void shouldConvertFromDomain() {
        Order order = OrderTestDataBuilder.brandNewBuilder().build();
        OrderJpaEntity entity = assembler.fromDomain(order);
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

    @Test
    void givenOrderWithNoItems_shouldRemovePersistenceEntityItems() {
        Order order = OrderTestDataBuilder.brandNewBuilder().withItems(false).build();
        OrderJpaEntity entity = OrderJpaEntityTestDataBuilder.existingBuilder().build();

        assertThat(order.items()).isEmpty();
        assertThat(entity.getItems()).isNotEmpty();

        assembler.merge(entity, order);
        assertThat(entity.getItems()).isEmpty();
    }

    @Test
    void givenOrderWithNewItems_shouldAddToPersistenceEntity() {
        Order order = OrderTestDataBuilder.brandNewBuilder().withItems(true).build();
        OrderJpaEntity entity = OrderJpaEntityTestDataBuilder.existingBuilder().items(new HashSet<>()).build();

        assertThat(order.items()).isNotEmpty();
        assertThat(entity.getItems()).isEmpty();

        assembler.merge(entity, order);
        assertThat(entity.getItems()).isNotEmpty();
    }

    @Test
    void givenOrderWithItems_whenMerge_shouldMergeIntoEntityItems() {
        Order order = OrderTestDataBuilder.brandNewBuilder().withItems(true).build();

        Set<OrderItemJpaEntity> entityOrderItems = order.items().stream()
                .map(assembler::fromDomain)
                .collect(Collectors.toSet());

        OrderJpaEntity orderEntity = OrderJpaEntityTestDataBuilder.existingBuilder()
                .items(entityOrderItems)
                .build();

        OrderItem orderItem = order.items().iterator().next();
        order.removeItem(orderItem.id());

        assembler.merge(orderEntity, order);

        assertThat(orderEntity.getItems()).isNotEmpty();
        assertThat(orderEntity.getItems().size()).isEqualTo(order.items().size());
    }

}