package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntityTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

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

    @Test
    void shouldPersistEmbeddedFields() {
        OrderJpaEntity entity = OrderJpaEntityTestDataBuilder.existingBuilder().build();
        entity = orderJpaEntityRepository.saveAndFlush(entity);

        // Verify billing embedded fields
        assertThat(entity.getBilling()).isNotNull();
        assertThat(entity.getBilling().getFullName()).isEqualTo("John");
        assertThat(entity.getBilling().getLastName()).isEqualTo("Doe");
        assertThat(entity.getBilling().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(entity.getBilling().getAddress()).isNotNull();
        assertThat(entity.getBilling().getAddress().getStreet()).isEqualTo("123 Main St");

        // Verify shipping embedded fields
        assertThat(entity.getShipping()).isNotNull();
        assertThat(entity.getShipping().getCost()).isEqualTo(new BigDecimal("15.50"));
        assertThat(entity.getShipping().getRecipient()).isNotNull();
        assertThat(entity.getShipping().getRecipient().getFirstName()).isEqualTo("Jane");
        assertThat(entity.getShipping().getAddress()).isNotNull();
        assertThat(entity.getShipping().getAddress().getCity()).isEqualTo("New York");
    }

    @Test
    void shouldPersistEmbeddedFieldsWithCustomColumnNames() {
        OrderJpaEntity entity = OrderJpaEntityTestDataBuilder.existingBuilder().build();
        entity = orderJpaEntityRepository.saveAndFlush(entity);

        // Verify that embedded fields are persisted with custom column names
        // This test ensures that @AttributeOverride annotations are working correctly
        
        // Retrieve the entity from database to verify persistence
        OrderJpaEntity retrievedEntity = orderJpaEntityRepository.findById(entity.getId()).orElseThrow();
        
        // Verify billing fields with custom column names
        assertThat(retrievedEntity.getBilling()).isNotNull();
        assertThat(retrievedEntity.getBilling().getFullName()).isEqualTo("John");
        assertThat(retrievedEntity.getBilling().getLastName()).isEqualTo("Doe");
        assertThat(retrievedEntity.getBilling().getDocument()).isEqualTo("123456789");
        assertThat(retrievedEntity.getBilling().getPhone()).isEqualTo("123-456-7890");
        assertThat(retrievedEntity.getBilling().getEmail()).isEqualTo("john.doe@example.com");
        
        // Verify billing address fields
        assertThat(retrievedEntity.getBilling().getAddress()).isNotNull();
        assertThat(retrievedEntity.getBilling().getAddress().getStreet()).isEqualTo("123 Main St");
        assertThat(retrievedEntity.getBilling().getAddress().getAdditionalInfo()).isEqualTo("Apt 4B");
        assertThat(retrievedEntity.getBilling().getAddress().getNeighborhood()).isEqualTo("Downtown");
        assertThat(retrievedEntity.getBilling().getAddress().getCity()).isEqualTo("New York");
        assertThat(retrievedEntity.getBilling().getAddress().getState()).isEqualTo("NY");
        assertThat(retrievedEntity.getBilling().getAddress().getZipcode()).isEqualTo("10001");
        
        // Verify shipping fields with custom column names
        assertThat(retrievedEntity.getShipping()).isNotNull();
        assertThat(retrievedEntity.getShipping().getCost()).isEqualTo(new BigDecimal("15.50"));
        assertThat(retrievedEntity.getShipping().getExpectedDate()).isNotNull();
        
        // Verify shipping recipient fields
        assertThat(retrievedEntity.getShipping().getRecipient()).isNotNull();
        assertThat(retrievedEntity.getShipping().getRecipient().getFirstName()).isEqualTo("Jane");
        assertThat(retrievedEntity.getShipping().getRecipient().getLastName()).isEqualTo("Smith");
        assertThat(retrievedEntity.getShipping().getRecipient().getDocument()).isEqualTo("987654321");
        assertThat(retrievedEntity.getShipping().getRecipient().getPhone()).isEqualTo("987-654-3210");
        
        // Verify shipping address fields
        assertThat(retrievedEntity.getShipping().getAddress()).isNotNull();
        assertThat(retrievedEntity.getShipping().getAddress().getStreet()).isEqualTo("123 Main St");
        assertThat(retrievedEntity.getShipping().getAddress().getCity()).isEqualTo("New York");
        assertThat(retrievedEntity.getShipping().getAddress().getState()).isEqualTo("NY");
        assertThat(retrievedEntity.getShipping().getAddress().getZipcode()).isEqualTo("10001");
    }
}