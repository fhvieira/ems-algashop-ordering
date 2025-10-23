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

public class OrderJpaEntityTestDataBuilder {
    private OrderJpaEntityTestDataBuilder() {

    }

    public static OrderJpaEntityBuilder existingBuilder() {
        return OrderJpaEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customerId(IdGenerator.generateTimeBasedUUID())
                .totalItems(2)
                .totalAmount(new BigDecimal(10))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placedAt(OffsetDateTime.now())
                .billing(createBillingEmbeddable())
                .shipping(createShippingEmbeddable());
    }

    private static BillingEmbeddable createBillingEmbeddable() {
        return BillingEmbeddable.builder()
                .fullName("John")
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
