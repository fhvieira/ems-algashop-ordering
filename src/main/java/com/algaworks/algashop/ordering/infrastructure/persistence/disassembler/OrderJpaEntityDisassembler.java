package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderItem;
import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.BillingEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.RecipientEmbeddable;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderJpaEntityDisassembler {
    public Order toDomain(OrderJpaEntity entity) {
        return Order.existingBuilder()
                .id(new OrderId(entity.getId()))
                .customerId(new CustomerId(entity.getCustomerId()))
                .totalAmount(new Money(entity.getTotalAmount()))
                .totalItems(new Quantity(entity.getTotalItems()))
                .status(OrderStatus.valueOf(entity.getStatus()))
                .paymentMethod(PaymentMethod.valueOf(entity.getPaymentMethod()))
                .placedAt(entity.getPlacedAt())
                .paidAt(entity.getPaidAt())
                .canceledAt(entity.getCanceledAt())
                .readyAt(entity.getReadyAt())
                .items(new HashSet<>())
                .shipping(getShipping(entity))
                .billing(getBilling(entity))
                .version(entity.getVersion())
                .items(getItems(entity))
                .build();
    }

    private Set<OrderItem> getItems(OrderJpaEntity entity) {
        return entity.getItems().stream()
                .map(entityItem -> OrderItem.existingBuilder()
                        .id(new OrderItemId(entityItem.getId()))
                        .orderId(new OrderId(entityItem.orderId()))
                        .productId(new ProductId(entityItem.getProductId()))
                        .productName(new ProductName(entityItem.getProductName()))
                        .productPrice(new Money(entityItem.getProductPrice()))
                        .quantity(new Quantity(entityItem.getQuantity()))
                        .totalAmount(new Money(entityItem.getTotalAmount()))
                        .build())
                .collect(Collectors.toSet());
    }

    private Shipping getShipping(OrderJpaEntity entity) {
        RecipientEmbeddable recipientEntity = entity.getShipping().getRecipient();
        AddressEmbeddable addressEntity = entity.getShipping().getAddress();

        Recipient recipient = Recipient.builder()
                .fullName(new FullName(recipientEntity.getFirstName(), recipientEntity.getLastName()))
                .document(new Document(recipientEntity.getDocument()))
                .phone(new Phone(recipientEntity.getPhone()))
                .build();

        Address address = Address.builder()
                .street(addressEntity.getStreet())
                .additionalInfo(addressEntity.getAdditionalInfo())
                .neighborhood(addressEntity.getNeighborhood())
                .city(addressEntity.getCity())
                .state(addressEntity.getState())
                .zipcode(new Zipcode(addressEntity.getZipcode()))
                .build();

        return Shipping.builder()
                .cost(new Money(entity.getShipping().getCost()))
                .expectedDate(entity.getShipping().getExpectedDate())
                .recipient(recipient)
                .address(address)
                .build();
    }

    private Billing getBilling(OrderJpaEntity entity) {
        BillingEmbeddable billingEntity = entity.getBilling();
        Address billingAddress = Address.builder()
                .street(billingEntity.getAddress().getStreet())
                .additionalInfo(billingEntity.getAddress().getAdditionalInfo())
                .neighborhood(billingEntity.getAddress().getNeighborhood())
                .city(billingEntity.getAddress().getCity())
                .state(billingEntity.getAddress().getState())
                .zipcode(new Zipcode(billingEntity.getAddress().getZipcode()))
                .build();

        return Billing.builder()
                .fullName(new FullName(billingEntity.getFirstName(), billingEntity.getLastName()))
                .document(new Document(billingEntity.getDocument()))
                .phone(new Phone(billingEntity.getPhone()))
                .email(new Email(billingEntity.getEmail()))
                .address(billingAddress)
                .build();
    }
}
