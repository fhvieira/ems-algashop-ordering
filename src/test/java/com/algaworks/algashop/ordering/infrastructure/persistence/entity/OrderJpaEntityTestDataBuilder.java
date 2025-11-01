package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity.OrderJpaEntityBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.ShippingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.util.Set;

public class OrderJpaEntityTestDataBuilder {
    private OrderJpaEntityTestDataBuilder() {

    }

    public static OrderJpaEntityBuilder existingBuilder() {
        return OrderJpaEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customerId(IdGenerator.generateTimeBasedUUID())
                .totalItems(5)
                .totalAmount(new BigDecimal(700))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now())
                .billing(createBillingEmbeddable())
                .shipping(createShippingEmbeddable())
                .items(Set.of(existingItem().build(), existingItemAlt().build()));
    }

    public static OrderItemJpaEntity.OrderItemJpaEntityBuilder existingItem() {
        return OrderItemJpaEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .productPrice(new BigDecimal(200))
                .quantity(2)
                .totalAmount(new BigDecimal(400))
                .productName("product")
                .productId(IdGenerator.generateTimeBasedUUID());
    }

    public static OrderItemJpaEntity.OrderItemJpaEntityBuilder existingItemAlt() {
        return OrderItemJpaEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .productPrice(new BigDecimal(100))
                .quantity(3)
                .totalAmount(new BigDecimal(300))
                .productName("other product")
                .productId(IdGenerator.generateTimeBasedUUID());
    }

    private static BillingEmbeddable createBillingEmbeddable() {
        return BillingEmbeddable.builder()
                .firstName("John")
                .lastName("Doe")
                .document("123456789")
                .phone("123-456-7890")
                .email("john.doe@example.com")
                .address(createAddressEmbeddable())
                .build();
    }

    private static ShippingEmbeddable createShippingEmbeddable() {
        return ShippingEmbeddable.builder()
                .cost(new BigDecimal("15.50"))
                .expectedDate(LocalDate.now().plusDays(3))
                .recipient(createRecipientEmbeddable())
                .address(createAddressEmbeddable())
                .build();
    }

    private static AddressEmbeddable createAddressEmbeddable() {
        return AddressEmbeddable.builder()
                .street("123 Main St")
                .additionalInfo("Apt 4B")
                .neighborhood("Downtown")
                .city("New York")
                .state("NY")
                .zipcode("10001")
                .build();
    }

    private static RecipientEmbeddable createRecipientEmbeddable() {
        return RecipientEmbeddable.builder()
                .firstName("Jane")
                .lastName("Smith")
                .document("987654321")
                .phone("987-654-3210")
                .build();
    }
}
