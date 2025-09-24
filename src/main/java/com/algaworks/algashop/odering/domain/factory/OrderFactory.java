package com.algaworks.algashop.odering.domain.factory;

import com.algaworks.algashop.odering.domain.entity.Order;
import com.algaworks.algashop.odering.domain.valueobject.*;
import com.algaworks.algashop.odering.domain.valueobject.id.CustomerId;
import java.util.Objects;

public class OrderFactory {
    private OrderFactory() {

    }

    public static Order readyToPlace(CustomerId customerId, Billing billing, Shipping shipping,
                                     PaymentMethod paymentMethod, Product product, Quantity productQuantity) {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(billing);
        Objects.requireNonNull(shipping);
        Objects.requireNonNull(paymentMethod);
        Objects.requireNonNull(product);
        Objects.requireNonNull(productQuantity);

        Order order = Order.draft(customerId);
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(paymentMethod);
        order.addItem(product, productQuantity);

        return order;
    }
}
