package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerJpaEntity;

public class CustomerJpaAssembler {

    public CustomerJpaEntity fromDomain(Customer customer) {
        CustomerJpaEntity entity = new CustomerJpaEntity();

        entity.setId(customer.id().value());
        entity.setFirstName(customer.fullName().firstName());
        entity.setLastName(customer.fullName().lastName());
        entity.setBirthDate(customer.birthDate().value());
        entity.setEmail(customer.email().value());
        entity.setPhone(customer.phone().value());
        entity.setDocument(customer.document().value());
        entity.setPromotionNotificationsAllowed(customer.isPromotionNotificationsAllowed());
        entity.setArchived(customer.isArchived());
        entity.setRegisteredAt(customer.registeredAt());
        entity.setArchivedAt(customer.archivedAt());
        entity.setLoyaltyPoints(customer.loyaltyPoints().value());
        entity.setAddress(AddressEmbeddable.builder()
                .street(customer.address().street())
                .additionalInfo(customer.address().additionalInfo())
                .neighborhood(customer.address().neighborhood())
                .city(customer.address().city())
                .state(customer.address().state())
                .zipcode(customer.address().zipcode().value())
                .build());
        entity.setVersion(customer.version());

        return entity;
    }

    public CustomerJpaEntity fromDomain(Customer customer, CustomerJpaEntity entity) {
        entity.setId(customer.id().value());
        entity.setFirstName(customer.fullName().firstName());
        entity.setLastName(customer.fullName().lastName());
        entity.setBirthDate(customer.birthDate().value());
        entity.setEmail(customer.email().value());
        entity.setPhone(customer.phone().value());
        entity.setDocument(customer.document().value());
        entity.setPromotionNotificationsAllowed(customer.isPromotionNotificationsAllowed());
        entity.setArchived(customer.isArchived());
        entity.setRegisteredAt(customer.registeredAt());
        entity.setArchivedAt(customer.archivedAt());
        entity.setLoyaltyPoints(customer.loyaltyPoints().value());
        entity.setAddress(AddressEmbeddable.builder()
                .street(customer.address().street())
                .additionalInfo(customer.address().additionalInfo())
                .neighborhood(customer.address().neighborhood())
                .city(customer.address().city())
                .state(customer.address().state())
                .zipcode(customer.address().zipcode().value())
                .build());
        entity.setVersion(customer.version());

        return entity;
    }
}
