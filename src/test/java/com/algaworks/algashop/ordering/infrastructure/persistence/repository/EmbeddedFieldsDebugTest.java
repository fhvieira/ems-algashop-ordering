package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntityTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SpringDataAuditingConfig.class)
@ActiveProfiles("test")
class EmbeddedFieldsDebugTest {
    
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Test
    void debugEmbeddedFields() {
        System.out.println("=== DEBUGGING EMBEDDED FIELDS ===");
        
        // Create entity with embedded fields
        OrderJpaEntity entity = OrderJpaEntityTestDataBuilder.existingBuilder().build();
        
        System.out.println("Entity created:");
        System.out.println("  - ID: " + entity.getId());
        System.out.println("  - Billing: " + entity.getBilling());
        System.out.println("  - Shipping: " + entity.getShipping());
        
        if (entity.getBilling() != null) {
            System.out.println("  - Billing Full Name: " + entity.getBilling().getFirstName());
            System.out.println("  - Billing Email: " + entity.getBilling().getEmail());
            System.out.println("  - Billing Address: " + entity.getBilling().getAddress());
        }
        
        if (entity.getShipping() != null) {
            System.out.println("  - Shipping Cost: " + entity.getShipping().getCost());
            System.out.println("  - Shipping Recipient: " + entity.getShipping().getRecipient());
            System.out.println("  - Shipping Address: " + entity.getShipping().getAddress());
        }
        
        // Save entity
        System.out.println("\nSaving entity...");
        entity = orderJpaRepository.saveAndFlush(entity);
        System.out.println("Entity saved with ID: " + entity.getId());
        
        // Retrieve entity
        System.out.println("\nRetrieving entity...");
        OrderJpaEntity retrievedEntity = orderJpaRepository.findById(entity.getId()).orElseThrow();
        
        System.out.println("Retrieved entity:");
        System.out.println("  - ID: " + retrievedEntity.getId());
        System.out.println("  - Billing: " + retrievedEntity.getBilling());
        System.out.println("  - Shipping: " + retrievedEntity.getShipping());
        
        if (retrievedEntity.getBilling() != null) {
            System.out.println("  - Billing Full Name: " + retrievedEntity.getBilling().getFirstName());
            System.out.println("  - Billing Email: " + retrievedEntity.getBilling().getEmail());
        }
        
        if (retrievedEntity.getShipping() != null) {
            System.out.println("  - Shipping Cost: " + retrievedEntity.getShipping().getCost());
        }
        
        // Basic assertions
        assertThat(retrievedEntity.getId()).isEqualTo(entity.getId());
        assertThat(retrievedEntity.getBilling()).isNotNull();
        assertThat(retrievedEntity.getShipping()).isNotNull();
        
        System.out.println("\n=== DEBUG COMPLETE ===");
    }
}
