package com.algaworks.algashop.ordering.domain.model.repository;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.OrderRepositoryImpl;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderEntityAssembler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({OrderRepositoryImpl.class, OrderEntityAssembler.class})
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
}