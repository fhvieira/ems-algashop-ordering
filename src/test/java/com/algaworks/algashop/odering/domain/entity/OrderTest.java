package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.exceptions.InvalidOrderDeliveryDateException;
import com.algaworks.algashop.odering.domain.exceptions.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.odering.domain.valueobject.*;
import com.algaworks.algashop.odering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.odering.domain.valueobject.id.ProductId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;


class OrderTest {

    @Test
    void shouldGenerate() {
        Order draft = Order.draft(new CustomerId());
    }

    @Test
    void shouldAddItem() {
        ProductId productId = new ProductId();
        ProductName productName = new ProductName("product name");
        Money price = new Money("100");
        Quantity quantity = new Quantity(1);

        Order order = Order.draft(new CustomerId());
        order.addItem(productId, productName, price, quantity);

        assertThat(order.items().size()).isEqualTo(1);
        assertWith(order.items().iterator().next(),
                (i) -> assertThat(i.id()).isNotNull(),
                (i) -> assertThat(i.productName()).isEqualTo(productName),
                (i) -> assertThat(i.productId()).isEqualTo(productId),
                (i) -> assertThat(i.price()).isEqualTo(price),
                (i) -> assertThat(i.quantity()).isEqualTo(quantity));
    }

    @Test
    void shouldGenerateExceptionWhenTryingToChangeItemSet() {
        Order order = Order.draft(new CustomerId());

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> order.items().clear());
    }

    @Test
    void shouldCalculateTotals() {
        ProductId productId = new ProductId();
        ProductName productName = new ProductName("product name");
        Money price = new Money("100");
        Quantity quantity = new Quantity(3);

        Order order = Order.draft(new CustomerId());
        order.addItem(productId, productName, price, quantity);
        order.addItem(productId, productName, price, quantity);

        assertThat(order.items().size()).isEqualTo(2);
        assertWith(order.totalAmount()).isEqualTo(new Money("600"));
        assertWith(order.totalItems().value()).isEqualTo(6);
        assertWith(order.items().iterator().next().totalAmount()).isEqualTo(new Money("300"));
    }

    @Test
    void shouldPlaceOrderFromDraft() {
        Order order = OrderTestDataBuilder.brandNewBuilder().build();
        order.place();

        assertThat(order.isPlaced()).isTrue();
        assertThat(order.placedAt()).isNotNull();
    }

    @Test
    void shouldFailToPlaceAlreadyPlacedOrder() {
        Order order = OrderTestDataBuilder.brandNewBuilder().status(OrderStatus.PLACED).build();

        assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::place);

        assertThat(order.isPlaced()).isTrue();
        assertThat(order.placedAt()).isNotNull();
    }

    @Test
    void shouldChangePaymentMethod() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMothod(PaymentMethod.CREDIT_CARD);
        assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    void shouldFailToChangePaymentMethodToNull() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMothod(PaymentMethod.CREDIT_CARD);

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> order.changePaymentMothod(null));

        assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    void shouldChangeToPaid() {
        Order order = OrderTestDataBuilder.brandNewBuilder().status(OrderStatus.PLACED).build();
        order.markAsPaid();
        assertThat(order.isPaid()).isTrue();
        assertThat(order.paidAt()).isNotNull();
    }

    @Test
    void shouldChangeBillingInfo() {
        BillingInfo billingInfo = OrderTestDataBuilder.getBillingInfo();
        Order order = OrderTestDataBuilder.brandNewBuilder()
                .billingInfo(billingInfo)
                .build();
        assertThat(order.billingInfo()).isEqualTo(billingInfo);
    }

    @Test
    void shouldChangeShippingInfo() {
        Order order = Order.draft(new CustomerId());
        ShippingInfo shippingInfo = OrderTestDataBuilder.getShippingInfo();

        order.changeShippingInfo(shippingInfo, new Money("100"), LocalDate.now());

        assertThat(order.shippingInfo()).isEqualTo(shippingInfo);
    }

    @Test
    void shouldFailToChangeDeliveryDateInThePast() {
        Order order = Order.draft(new CustomerId());

        ShippingInfo shippingInfo = OrderTestDataBuilder.getShippingInfo();

        assertThatExceptionOfType(InvalidOrderDeliveryDateException.class)
                .isThrownBy(() -> order.changeShippingInfo(
                        shippingInfo,
                        new Money("100"),
                        LocalDate.now().minusDays(1)));
    }

}