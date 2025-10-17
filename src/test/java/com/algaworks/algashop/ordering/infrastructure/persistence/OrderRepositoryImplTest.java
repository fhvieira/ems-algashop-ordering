package com.algaworks.algashop.ordering.infrastructure.persistence;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.OrderStatus;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
        OrderRepositoryImpl.class,
        OrderEntityAssembler.class,
        OrderEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class OrderRepositoryImplIT {
    private OrderRepositoryImpl orderRepository;
    private OrderEntityRepository entityRepository;

    @Autowired
    public OrderRepositoryImplIT(OrderRepositoryImpl orderRepository, OrderEntityRepository entityRepository) {
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

        OrderEntity entity = entityRepository.findById(orderId).orElseThrow();

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