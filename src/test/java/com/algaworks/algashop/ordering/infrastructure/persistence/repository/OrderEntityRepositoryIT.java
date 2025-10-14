package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderEntityTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderEntityRepositoryIT {
    private final OrderEntityRepository orderEntityRepository;

    @Autowired
    public OrderEntityRepositoryIT(OrderEntityRepository orderEntityRepository) {
        this.orderEntityRepository = orderEntityRepository;
    }

    @Test
    void shouldPersist() {
        OrderEntity entity = OrderEntityTestDataBuilder.existingBuilder().build();

        orderEntityRepository.saveAndFlush(entity);
        assertThat(orderEntityRepository.existsById(entity.getId())).isTrue();
    }

    @Test
    void shouldCount() {
        assertThat(orderEntityRepository.count()).isZero();
    }
}