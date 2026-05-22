package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.repository.ShoppingCartRepository;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartJpaAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartJpaDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartJpaEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartRepositoryAdapter implements ShoppingCartRepository {

    private final ShoppingCartJpaRepository repository;
    private final ShoppingCartJpaAssembler assembler;
    private final ShoppingCartJpaDisassembler shoppingCartJpaDd;
    private final EntityManager entityManager;

    @Override
    public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
        return repository.findByCustomerId(customerId.value())
                .map(shoppingCartJpaDd::toDomain);
    }

    @Override
    public Optional<ShoppingCart> ofId(ShoppingCartId shoppingCartId) {
        return repository.findById(shoppingCartId.value())
                .map(shoppingCartJpaDd::toDomain);
    }

    @Override
    public boolean exists(ShoppingCartId shoppingCartId) {
        return repository.existsById(shoppingCartId.value());
    }

    @Override
    @Transactional(readOnly = false)
    public void add(ShoppingCart aggregateRoot) {
        repository.findById(aggregateRoot.id().value())
                .ifPresentOrElse(
                        (entity) -> update(aggregateRoot, entity),
                        () -> insert(aggregateRoot)
                );
    }

    private void update(ShoppingCart aggregateRoot, ShoppingCartJpaEntity entity) {
        entity = assembler.merge(entity, aggregateRoot);
        entityManager.detach(entity);
        entity = repository.saveAndFlush(entity);
        updateVersion(aggregateRoot, entity);
    }

    private void insert(ShoppingCart aggregateRoot) {
        ShoppingCartJpaEntity entity = assembler.fromDomain(aggregateRoot);
        entity = repository.saveAndFlush(entity);
        updateVersion(aggregateRoot, entity);
    }

    @SneakyThrows
    private void updateVersion(ShoppingCart aggregateRoot, ShoppingCartJpaEntity entity) {
        Field version = aggregateRoot.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version, aggregateRoot, entity.getVersion());
        version.setAccessible(false);
    }

    @Override
    public void remove(ShoppingCart shoppingCart) {
        repository.deleteById(shoppingCart.id().value());
    }

    @Override
    public void remove(ShoppingCartId shoppingCartId) {
        repository.deleteById(shoppingCartId.value());
    }

    @Override
    public long count() {
        return repository.count();
    }
}
