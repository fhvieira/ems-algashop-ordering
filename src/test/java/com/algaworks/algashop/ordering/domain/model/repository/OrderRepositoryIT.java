package com.algaworks.algashop.ordering.domain.model.repository;

import com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.JpaCustomerRepository;
import com.algaworks.algashop.ordering.infrastructure.persistence.JpaOrderRepository;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerJpaAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderJpaAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerJpaDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderJpaDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@Import({
        JpaOrderRepository.class,
        OrderJpaAssembler.class,
        OrderJpaDisassembler.class,
        JpaCustomerRepository.class,
        CustomerJpaAssembler.class,
        CustomerJpaDisassembler.class
})
class OrderRepositoryIT {
    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public OrderRepositoryIT(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @BeforeEach
    public void setup() {
        if (!customerRepository.exists(DEFAULT_CUSTOMER_ID)) {
            customerRepository.add(CustomerTestDataBuilder.existingCustomerBuilder().build());
        }
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

    @Test
    public void shouldCountExistingOrders() {
        assertThat(orderRepository.count()).isZero();

        Order order1 = OrderTestDataBuilder.brandNewBuilder().build();
        Order order2 = OrderTestDataBuilder.brandNewBuilder().build();

        orderRepository.add(order1);
        orderRepository.add(order2);

        assertThat(orderRepository.count()).isEqualTo(2L);
    }

    @Test
    public void shouldCheckIfOrderExists() {
        Order order = OrderTestDataBuilder.brandNewBuilder().build();
        orderRepository.add(order);
        assertThat(orderRepository.exists(order.id())).isTrue();
        assertThat(orderRepository.exists(new OrderId())).isFalse();
    }
}