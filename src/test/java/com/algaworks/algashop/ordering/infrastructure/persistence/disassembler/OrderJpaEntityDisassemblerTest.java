package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderJpaEntityTestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderJpaEntityDisassemblerTest {
    private final OrderJpaEntityDisassembler disassembler = new OrderJpaEntityDisassembler();

    @Test
    void shouldConvertFromPersistenceEntity() {
        OrderJpaEntity entity = OrderJpaEntityTestDataBuilder.existingBuilder().build();
        Order domainEntity = disassembler.toDomain(entity);
        assertThat(domainEntity).satisfies(
                d -> assertThat(d.id()).isEqualTo(new OrderId(entity.getId())),
                d -> assertThat(d.customerId()).isEqualTo(new CustomerId(entity.getCustomerId())),
                d -> assertThat(d.totalItems()).isEqualTo(new Quantity(entity.getTotalItems())),
                d -> assertThat(d.totalAmount()).isEqualTo(new Money(entity.getTotalAmount())),
                d -> assertThat(d.status()).isEqualTo(OrderStatus.valueOf(entity.getStatus())),
                d -> assertThat(d.paymentMethod()).isEqualTo(PaymentMethod.valueOf(entity.getPaymentMethod())),
                d -> assertThat(d.placedAt()).isEqualTo(entity.getPlacedAt()),
                d -> assertThat(d.readyAt()).isEqualTo(entity.getReadyAt()),
                d -> assertThat(d.paidAt()).isEqualTo(entity.getPaidAt()),
                d -> assertThat(d.placedAt()).isEqualTo(entity.getPlacedAt()),
                d -> assertThat(d.items()).isNotEmpty()
        );
    }

}