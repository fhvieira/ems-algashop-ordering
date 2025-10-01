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
    void shouldGenerateDraftOrder() {
        Order draft = Order.draft(new CustomerId());

        assertWith(draft,
                o -> assertThat(o.id()).isNotNull(),
                o -> assertThat(o.customerId()).isNotNull(),
                o -> assertThat(o.totalAmount()).isEqualTo(Money.ZERO),
                o -> assertThat(o.totalItems()).isEqualTo(Quantity.ZERO),
                o -> assertThat(o.isDraft()).isTrue(),
                o -> assertThat(o.items()).isEmpty(),
                o -> assertThat(o.placedAt()).isNull(),
                o -> assertThat(o.paidAt()).isNull(),
                o -> assertThat(o.canceledAt()).isNull(),
                o -> assertThat(o.readyAt()).isNull(),
                o -> assertThat(o.billing()).isNull(),
                o -> assertThat(o.shipping()).isNull(),
                o -> assertThat(o.paymentMethod()).isNull()
        );
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
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
        assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    void shouldFailToChangePaymentMethodToNull() {
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> order.changePaymentMethod(null));

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
    void shouldChangeToReady() {
        Order order = OrderTestDataBuilder.brandNewBuilder().status(OrderStatus.PAID).build();
        order.markAsReady();
        assertThat(order.isReady()).isTrue();
        assertThat(order.readyAt()).isNotNull();
    }

    @Test
    void shouldFailToChangeToReady() {
        Order order = OrderTestDataBuilder.brandNewBuilder().status(OrderStatus.PLACED).build();

        assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::markAsReady);

        assertThat(order.isPaid()).isFalse();
        assertThat(order.paidAt()).isNull();
    }

    @Test
    void shouldCancelDraft() {
        Order order = Order.draft(new CustomerId());
        order.cancel();
        assertThat(order.status()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    void shouldCancelPlaced() {
        Order order = OrderTestDataBuilder.brandNewBuilder().status(OrderStatus.PLACED).build();
        order.cancel();
        assertThat(order.status()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    void shouldCancelPaid() {
        Order order = OrderTestDataBuilder.brandNewBuilder().status(OrderStatus.PAID).build();
        order.cancel();
        assertThat(order.status()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    void shouldCancelReady() {
        Order order = OrderTestDataBuilder.brandNewBuilder().status(OrderStatus.READY).build();
        order.cancel();
        assertThat(order.status()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    void shouldFailToCancel() {
        Order order = OrderTestDataBuilder.brandNewBuilder().status(OrderStatus.CANCELED).build();
        assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::cancel);
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