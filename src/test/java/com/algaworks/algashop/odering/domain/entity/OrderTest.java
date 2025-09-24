package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.exceptions.InvalidOrderDeliveryDateException;
import com.algaworks.algashop.odering.domain.exceptions.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.odering.domain.exceptions.ProductOutOfStockException;
import com.algaworks.algashop.odering.domain.valueobject.*;
import com.algaworks.algashop.odering.domain.valueobject.id.CustomerId;
import org.assertj.core.api.ThrowableAssert;
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
        Product product = ProductTestDataBuilder.aProduct().build();
        Quantity quantity = new Quantity(3);

        Order order = Order.draft(new CustomerId());
        order.addItem(product, quantity);

        assertThat(order.items().size()).isEqualTo(1);
        assertWith(order.items().iterator().next(),
                (i) -> assertThat(i.id()).isNotNull(),
                (i) -> assertThat(i.productName()).isEqualTo(product.name()),
                (i) -> assertThat(i.productId()).isEqualTo(product.id()),
                (i) -> assertThat(i.price()).isEqualTo(product.price()),
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
        Product product = ProductTestDataBuilder.aProduct().build();

        Order order = Order.draft(new CustomerId());
        order.addItem(product, new Quantity(3));
        order.addItem(product, new Quantity(3));

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
    void shouldChangeBilling() {
        Billing billing = OrderTestDataBuilder.aBilling();
        Order order = OrderTestDataBuilder.brandNewBuilder()
                .billing(billing)
                .build();
        assertThat(order.billing()).isEqualTo(billing);
    }

    @Test
    void shouldChangeShipping() {
        Order order = Order.draft(new CustomerId());
        Shipping shipping = OrderTestDataBuilder.aShipping();

        order.changeShipping(shipping);

        assertThat(order.shipping()).isEqualTo(shipping);
    }

    @Test
    void shouldFailToChangeDeliveryDateInThePast() {
        Order order = Order.draft(new CustomerId());

        Shipping newShipping = OrderTestDataBuilder.aShipping().toBuilder()
                .expectedDate(LocalDate.now().minusDays(3))
                .build();

        assertThatExceptionOfType(InvalidOrderDeliveryDateException.class)
                .isThrownBy(() -> order.changeShipping(newShipping));
    }

    @Test
    void shouldChangeOrderItemQuantityAndRecalculate() {
        Order order = Order.draft(new CustomerId());
        Product product = ProductTestDataBuilder.aProduct()
                .price(new Money("10"))
                .build();

        order.addItem(product, new Quantity(3));

        OrderItem item = order.items().iterator().next();
        order.changeItemQuantity(item.id(), new Quantity(5));

        assertWith(order,
                (it) -> assertThat(it.totalItems()).isEqualTo(new Quantity(5)),
                (it) -> assertThat(it.totalAmount()).isEqualTo(new Money("50"))
        );
    }

    @Test
    void slhuldNotAddItemWithOutOfStockProduct() {
        Order order = Order.draft(new CustomerId());

        ThrowableAssert.ThrowingCallable addItemTask = () -> order.addItem(ProductTestDataBuilder.aProduct()
                        .inStock(false).build(), new Quantity(1));

        assertThatExceptionOfType(ProductOutOfStockException.class).isThrownBy(addItemTask);
    }
}