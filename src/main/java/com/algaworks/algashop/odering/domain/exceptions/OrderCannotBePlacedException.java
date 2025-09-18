package com.algaworks.algashop.odering.domain.exceptions;

import com.algaworks.algashop.odering.domain.valueobject.id.OrderId;

import static com.algaworks.algashop.odering.domain.exceptions.ErrorMessages.*;

public class OrderCannotBePlacedException extends DomainException {
    private OrderCannotBePlacedException(String message) {
        super(message);
    }

    public static OrderCannotBePlacedException noShippingCost(OrderId id) {
        return new OrderCannotBePlacedException(ORDER_CANNOT_BE_PLACED_HAS_NO_SHIPPING);
    }

    public static OrderCannotBePlacedException noBillingInfo(OrderId id) {
        return new OrderCannotBePlacedException(ORDER_CANNOT_BE_PLACED_HAS_NO_BILLING_INFO);
    }

    public static OrderCannotBePlacedException noExpectedDeliveryDate(OrderId id) {
        return new OrderCannotBePlacedException(ORDER_CANNOT_BE_PLACED_HAS_NO_DELIVERY_DATE);
    }

    public static OrderCannotBePlacedException noPaymentMethod(OrderId id) {
        return new OrderCannotBePlacedException(ORDER_CANNOT_BE_PLACED_HAS_NO_PAYMENT_METHOD);
    }

    public static OrderCannotBePlacedException noItems(OrderId id) {
        return new OrderCannotBePlacedException(String.format(ORDER_CANNOT_BE_PLACED_HAS_NO_ITEMS, id));
    }
}
