package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

public class CustomerJpaEntityTestDataBuilder {
    private CustomerJpaEntityTestDataBuilder() {

    }

    public static CustomerJpaEntity.CustomerJpaEntityBuilder aCustomer() {
        return CustomerJpaEntity.builder()
                .id(DEFAULT_CUSTOMER_ID.value())
                .firstName("john")
                .lastName("doe")
                .birthDate(LocalDate.of(1991, 7, 5))
                .email("john.doe@meail.com")
                .phone("478-222-1234")
                .document("123-746-7890")
                .promotionNotificationsAllowed(true)
                .archived(false)
                .registeredAt(OffsetDateTime.now())
                .archivedAt(null)
                .loyaltyPoints(0)
                .address(createAddressEmbeddable());
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

}
