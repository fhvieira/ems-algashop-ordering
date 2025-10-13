package com.algaworks.algashop.ordering.infrastructure.persistence;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.repository.OrderRepository;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderEntityRepository orderEntityRepository;
    private final OrderEntityAssembler orderEntityAssembler;

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        OrderEntity entity = orderEntityRepository.findById(orderId.value().toLong()).get();
        return Optional.empty();
    }

    @Override
    public boolean exists(OrderId orderId) {
        return false;
    }

    @Override
    public void add(Order order) {
        OrderEntity entity = orderEntityAssembler.fromDomain(order);
        orderEntityRepository.saveAndFlush(entity);
    }

    @Override
    public int count() {
        return 0;
    }
}