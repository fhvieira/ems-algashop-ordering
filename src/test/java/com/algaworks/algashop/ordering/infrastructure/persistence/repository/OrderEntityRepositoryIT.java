package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderEntity;
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
        Long orderId = IdGenerator.generateTSID().toLong();
        OrderEntity entity = OrderEntity.builder()
                .id(orderId)
                .build();

        orderEntityRepository.saveAndFlush(entity);
        assertThat(orderEntityRepository.existsById(orderId)).isTrue();
    }

    @Test
    void shouldCount() {
        assertThat(orderEntityRepository.count()).isZero();
    }
}