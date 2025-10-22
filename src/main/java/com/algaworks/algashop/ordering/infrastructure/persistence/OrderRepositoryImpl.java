package com.algaworks.algashop.ordering.infrastructure.persistence;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.repository.OrderRepository;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderJpaEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderJpaEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrderJpaEntityRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaEntityRepository orderJpaEntityRepository;
    private final OrderJpaEntityAssembler assembler;
    private final OrderJpaEntityDisassembler disassembler;
    private final EntityManager entityManager;

    @Override
    public Optional<Order> ofId(OrderId orderId) {
        Optional<OrderJpaEntity> possibleEntity = orderJpaEntityRepository.findById(orderId.value().toLong());
        return possibleEntity.map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(OrderId orderId) {
        return false;
    }

    @Override
    public void add(Order order) {
        orderJpaEntityRepository.findById(order.id().value().toLong()).ifPresentOrElse(
                (entity) -> {
                    update(order, entity);
                },
                () -> {
                    insert(order);
                }
        );
    }

    private void update(Order order, OrderJpaEntity entity) {
        entity = assembler.merge(entity, order);
        entityManager.detach(entity);
        entity = orderJpaEntityRepository.saveAndFlush(entity);
        updateVersion(order, entity);
    }

    private void insert(Order order) {
        OrderJpaEntity entity = assembler.fromDomain(order);
        orderJpaEntityRepository.saveAndFlush(entity);
        updateVersion(order, entity);
    }

    @SneakyThrows
    private void updateVersion(Order order, OrderJpaEntity entity) {
        Field version = order.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version, order, entity.getVersion());
        version.setAccessible(false);
    }

    @Override
    public int count() {
        return 0;
    }
}