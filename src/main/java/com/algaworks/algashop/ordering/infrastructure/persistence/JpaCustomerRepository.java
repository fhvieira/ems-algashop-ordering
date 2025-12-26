package com.algaworks.algashop.ordering.infrastructure.persistence;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.repository.CustomerRepository;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerJpaAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerJpaDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerJpaRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaCustomerRepository implements CustomerRepository {
    private final CustomerJpaRepository jpaRepository;
    private final CustomerJpaAssembler assembler;
    private final CustomerJpaDisassembler disassember;
    private final EntityManager entityManager;

    @Override
    public Optional<Customer> ofId(CustomerId customerId) {
        Optional<CustomerJpaEntity> possibleCustomer = jpaRepository.findById(customerId.value());
        return possibleCustomer.map(disassember::toDomain);
    }

    @Override
    public boolean exists(CustomerId customerId) {
        return jpaRepository.existsById(customerId.value());
    }

    @Override
    @Transactional
    public void add(Customer customer) {
        jpaRepository.findById(customer.id().value())
                .ifPresentOrElse(
                        (entity) -> {
                            try {
                                update(customer, entity);
                            } catch (NoSuchFieldException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> insert(customer)
                );
    }

    private void insert(Customer customer) {
        CustomerJpaEntity entity = assembler.fromDomain(customer);
        jpaRepository.saveAndFlush(entity);
    }

    private void update(Customer customer, CustomerJpaEntity entity) throws NoSuchFieldException {
        entity = assembler.fromDomain(customer, entity);
        entityManager.detach(entity);
        entity = jpaRepository.saveAndFlush(entity);

        Field version = customer.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version, customer, entity.getVersion());
        version.setAccessible(false);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
