package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.assertj.core.data.Offset;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTestDataBuilder {
    private CustomerTestDataBuilder() {

    }

    public static Customer.BrandNewCustomerBuilder brandNewCustomerBuilder() {
        return Customer.brandNewBuilder()
                .fullName(new FullName("john", "doe"))
                .birthDate(new BirthDate(LocalDate.of(2000, 1, 1)))
                .email(new Email("john.doe@email.com"))
                .phone(new Phone("478-256-25024"))
                .document(new Document("255-08-0578"))
                .promotionNotificationsAllowed(true)
                .address(anValidNewAddress());
    }

    public static Customer.ExistingCustomerBuilder existingCustomerBuilder() {
        return Customer.existingBuilder()
                .id(new CustomerId())
                .fullName(new FullName("john", "doe"))
                .birthDate(new BirthDate(LocalDate.of(2000, 1, 1)))
                .email(new Email("john.doe@email.com"))
                .phone(new Phone("123-456-7890"))
                .document(new Document("123-45-6789"))
                .promotionNotificationsAllowed(true)
                .archived(false)
                .registeredAt(OffsetDateTime.now())
                .archivedAt(OffsetDateTime.now())
                .loyaltyPoints(new LoyaltyPoints(10))
                .address(anValidNewAddress());
    }

    public static Customer.ExistingCustomerBuilder existingAnonymizedCustomerBuilder() {
        return Customer.existingBuilder()
                .id(new CustomerId())
                .fullName(new FullName("anonymous", "anonymous"))
                .birthDate(new BirthDate(LocalDate.of(2000, 1, 1)))
                .email(new Email("anonymous@email.com"))
                .phone(new Phone("000-000-0000"))
                .document(new Document("000-00-0000"))
                .promotionNotificationsAllowed(false)
                .archived(true)
                .registeredAt(OffsetDateTime.now())
                .archivedAt(OffsetDateTime.now())
                .loyaltyPoints(LoyaltyPoints.ZERO)
                .address(anValidNewAddress());
    }

    private static Address anValidNewAddress() {
        return Address.builder()
                .street("street")
                .additionalInfo("additionalInfo")
                .neighborhood("neighborhood")
                .city("city")
                .state("state")
                .zipcode(Zipcode.builder().value("12345").build())
                .build();
    }

}
