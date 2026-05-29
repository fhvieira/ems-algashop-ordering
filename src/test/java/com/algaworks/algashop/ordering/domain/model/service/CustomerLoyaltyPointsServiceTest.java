package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.OrderStatus;
import org.junit.jupiter.api.Test;

import static com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder.existingCustomerBuilder;
import static com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder.brandNewBuilder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerLoyaltyPointsServiceTest {

    @Test
    void shouldAddLoyaltyPointsForPaidOrder() {
        Customer customer = existingCustomerBuilder()
                .loyaltyPoints(LoyaltyPoints.ZERO)
                .build();

        Order order = brandNewBuilder()
                .customerId(customer.id())
                .totalAmount(new Money("5000"))
                .status(OrderStatus.PAID)
                .build();

        CustomerLoyaltyPointsService service = new CustomerLoyaltyPointsService();

        service.addPoints(customer, order);

        assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(5));
    }

    @Test
    void shouldNotChangeCustomerLoyaltyPointsWhenCalculatedPointsIsZero() {
        Customer customer = existingCustomerBuilder()
                .loyaltyPoints(LoyaltyPoints.ZERO)
                .build();

        Order order = brandNewBuilder()
                .customerId(customer.id())
                .totalAmount(new Money("999"))
                .status(OrderStatus.PAID)
                .build();

        CustomerLoyaltyPointsService service = new CustomerLoyaltyPointsService();

        service.addPoints(customer, order);

        assertThat(customer.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
    }
}
