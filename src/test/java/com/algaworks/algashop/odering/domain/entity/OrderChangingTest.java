package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.exceptions.OrderCannotBeEditedException;
import com.algaworks.algashop.odering.domain.valueobject.*;
import com.algaworks.algashop.odering.domain.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertWith;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class OrderChangingTest {
    @Test
    void shouldChangeOrderInDraft() {
        Order order = Order.draft(new CustomerId());

        Product product = ProductTestDataBuilder.aProduct().build();
        Billing billing = OrderTestDataBuilder.aBilling();
        Shipping shipping = OrderTestDataBuilder.aShipping();

        order.addItem(product, new Quantity(1));
        order.changeBilling(billing);
        order.changeShipping(shipping);
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        assertWith(order,
                o -> assertThat(o.items()).isNotEmpty(),
                o -> assertThat(o.billing()).isEqualTo(billing),
                o -> assertThat(o.shipping()).isEqualTo(shipping),
                o -> assertThat(o.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD)
        );
    }

    @Test
    void shouldFailToChangeNotInDraftOrder() {
        Order order = OrderTestDataBuilder.brandNewBuilder().status(OrderStatus.PLACED).build();

        assertSoftly(softly -> {
            softly.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                    .isThrownBy(() -> order.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(1)));
            softly.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                    .isThrownBy(() -> order.changeBilling(OrderTestDataBuilder.aBilling()));
            softly.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                    .isThrownBy(() -> order.changeShipping(OrderTestDataBuilder.aShipping()));
            softly.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                    .isThrownBy(() -> order.changePaymentMethod(PaymentMethod.CREDIT_CARD));
        });
    }
    
}
