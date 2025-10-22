package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntityTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SpringDataAuditingConfig.class)
class OrderJpaEntityRepositoryIT {
    private final OrderJpaEntityRepository orderJpaEntityRepository;

    @Autowired
    public OrderJpaEntityRepositoryIT(OrderJpaEntityRepository orderJpaEntityRepository) {
        this.orderJpaEntityRepository = orderJpaEntityRepository;
    }

    @Test
    void shouldPersist() {
        OrderJpaEntity entity = OrderJpaEntityTestDataBuilder.existingBuilder().build();

        orderJpaEntityRepository.saveAndFlush(entity);
        assertThat(orderJpaEntityRepository.existsById(entity.getId())).isTrue();
    }

    @Test
    void shouldCount() {
        assertThat(orderJpaEntityRepository.count()).isZero();
    }

    @Test
    void shouldSetAuditingValues() {
        OrderJpaEntity entity = OrderJpaEntityTestDataBuilder.existingBuilder().build();
        entity = orderJpaEntityRepository.saveAndFlush(entity);

        assertThat(entity.getCreatedByUserId()).isNotNull();
        assertThat(entity.getLastModifiedAt()).isNotNull();
        assertThat(entity.getLastModifiedByUserId()).isNotNull();
    }
}