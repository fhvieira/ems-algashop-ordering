package com.algaworks.algashop.ordering.domain.model.repository;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.OrderRepositoryImpl;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderJpaEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderJpaEntityDisassembler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@Import({OrderRepositoryImpl.class, OrderJpaEntityAssembler.class, OrderJpaEntityDisassembler.class})
class OrderRepositoryIT {
    private OrderRepository orderRepository;

    @Autowired
    public OrderRepositoryIT(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Test
    public void shouldPersistAndFind() {
        Order order = OrderTestDataBuilder.brandNewBuilder().build();
        OrderId orderId = order.id();
        orderRepository.add(order);
        Optional<Order> possibleOrder = orderRepository.ofId(orderId);
        assertThat(possibleOrder).isPresent();
    }

    @Test
    public void shouldUpdateExistingOrder() {
        Order order = OrderTestDataBuilder.brandNewBuilder()
                .status(OrderStatus.PLACED)
                .build();
        orderRepository.add(order);
        order = orderRepository.ofId(order.id()).orElseThrow();
        order.markAsPaid();
        orderRepository.add(order);
        order = orderRepository.ofId(order.id()).orElseThrow();
        assertThat(order.isPaid()).isTrue();
    }

    @Test
    public void shouldNotAllowStaleUpdated() {
        Order order = OrderTestDataBuilder.brandNewBuilder()
                .status(OrderStatus.PLACED)
                .build();
        orderRepository.add(order);

        Order orderT1 = orderRepository.ofId(order.id()).orElseThrow();
        Order orderT2 = orderRepository.ofId(order.id()).orElseThrow();

        orderT1.markAsPaid();
        orderRepository.add(orderT1);

        orderT2.cancel();

        assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
                .isThrownBy(() -> orderRepository.add(orderT2));

        Order savedOrder = orderRepository.ofId(order.id()).orElseThrow();

        assertThat(savedOrder.canceledAt()).isNull();
        assertThat(savedOrder.paidAt()).isNotNull();
    }
}