package com.algaworks.algashop.ordering.infrastructure.persistence;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.repository.OrderRepository;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderEntityRepository orderEntityRepository;
    private final OrderEntityAssembler assembler;
    private final OrderEntityDisassembler disassembler;

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        Optional<OrderEntity> possibleEntity = orderEntityRepository.findById(orderId.value().toLong());
        return possibleEntity.map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(OrderId orderId) {
        return false;
    }

    @Override
    public void add(Order order) {
        OrderEntity entity = assembler.fromDomain(order);
        orderEntityRepository.saveAndFlush(entity);
    }

    @Override
    public int count() {
        return 0;
    }
}