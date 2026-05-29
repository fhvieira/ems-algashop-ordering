package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.exceptions.LoyaltyPointsCannotBeAddedForUnpaidOrderException;
import com.algaworks.algashop.ordering.domain.model.exceptions.OrderDoesNotBelongToCustomerException;
import com.algaworks.algashop.ordering.domain.model.valueobject.LoyaltyPoints;

import java.util.Objects;

public class CustomerLoyaltyPointsService {
    public void addPoints(Customer customer, Order order) {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(order);

        if (!customer.id().equals(order.customerId())) {
            throw new OrderDoesNotBelongToCustomerException();
        }

        if (!order.isPaid()) {
            throw new LoyaltyPointsCannotBeAddedForUnpaidOrderException();
        }

        LoyaltyPoints points = LoyaltyPoints.basedOn(order.totalAmount());
        if (points.compareTo(LoyaltyPoints.ZERO) <= 0) {
            return;
        }

        customer.addLoyaltyPoints(points);
    }
}
