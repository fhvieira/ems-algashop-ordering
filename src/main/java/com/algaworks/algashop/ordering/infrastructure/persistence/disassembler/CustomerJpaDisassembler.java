package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerJpaDisassembler {
    public Customer toDomain(CustomerJpaEntity entity) {
        return Customer.existingBuilder()
                .id(new CustomerId(entity.getId()))
                .version(entity.getVersion())
                .fullName(new FullName(entity.getFirstName(), entity.getLastName()))
                .birthDate(new BirthDate(entity.getBirthDate()))
                .email(new Email(entity.getEmail()))
                .phone(new Phone(entity.getPhone()))
                .document(new Document(entity.getDocument()))
                .promotionNotificationsAllowed(entity.getPromotionNotificationsAllowed())
                .archived(entity.getArchived())
                .registeredAt(entity.getRegisteredAt())
                .archivedAt(entity.getArchivedAt())
                .loyaltyPoints(new LoyaltyPoints(entity.getLoyaltyPoints()))
                .address(new Address(
                        entity.getAddress().getStreet(),
                        entity.getAddress().getAdditionalInfo(),
                        entity.getAddress().getNeighborhood(),
                        entity.getAddress().getCity(),
                        entity.getAddress().getState(),
                        new Zipcode(entity.getAddress().getZipcode())
                )).build();
    }
}
