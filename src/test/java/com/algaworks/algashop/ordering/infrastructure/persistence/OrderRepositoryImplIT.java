package com.algaworks.algashop.ordering.infrastructure.persistence;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.OrderStatus;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderJpaEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderJpaEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderJpaEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        OrderRepositoryImpl.class,
        OrderJpaEntityAssembler.class,
        OrderJpaEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class OrderRepositoryImplIT {
    private OrderRepositoryImpl orderRepository;
    private OrderJpaEntityRepository entityRepository;

    @Autowired
    public OrderRepositoryImplIT(OrderRepositoryImpl orderRepository, OrderJpaEntityRepository entityRepository) {
        this.orderRepository = orderRepository;
        this.entityRepository = entityRepository;
    }

    @Test
    public void shouldUpdateAndKeepEntityState() {
        Order order = OrderTestDataBuilder.brandNewBuilder()
                .status(OrderStatus.PLACED)
                .build();
        long orderId = order.id().value().toLong();
        orderRepository.add(order);

        OrderJpaEntity entity = entityRepository.findById(orderId).orElseThrow();

        assertThat(entity.getStatus()).isEqualTo(OrderStatus.PLACED.name());
        assertThat(entity.getCreatedByUserId()).isNotNull();
        assertThat(entity.getLastModifiedAt()).isNotNull();
        assertThat(entity.getLastModifiedByUserId()).isNotNull();

        order = orderRepository.ofId(order.id()).orElseThrow();
        order.markAsPaid();
        orderRepository.add(order);

        entity = entityRepository.findById(orderId).orElseThrow();

        assertThat(entity.getStatus()).isEqualTo(OrderStatus.PAID.name());
        assertThat(entity.getCreatedByUserId()).isNotNull();
        assertThat(entity.getLastModifiedAt()).isNotNull();
        assertThat(entity.getLastModifiedByUserId()).isNotNull();
    }
}