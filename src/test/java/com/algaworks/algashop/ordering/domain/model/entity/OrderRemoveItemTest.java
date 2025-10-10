package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exceptions.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.model.exceptions.OrderItemDoesNotExistException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class OrderRemoveItemTest {
    @Test
    void shouldRemoveOrderItemAndRecalculate() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Product altProduct = ProductTestDataBuilder.anAltProduct().build();

        Order order = Order.draft(new CustomerId());

        order.addItem(product, new Quantity(1));
        order.addItem(altProduct, new Quantity(2));

        OrderItemId itemId = order.items().stream()
                .filter(i -> i.productName().equals(altProduct.name()))
                .map(OrderItem::id)
                .findFirst().get();

        order.removeItem(itemId);

        assertWith(order,
                (it) -> assertThat(it.totalItems()).isEqualTo(new Quantity(1)),
                (it) -> assertThat(it.totalAmount()).isEqualTo(new Money("100"))
        );
    }

    @Test
    void shouldFailToRemoveNonExistingItem() {
        Order order = Order.draft(new CustomerId());

        assertThatExceptionOfType(OrderItemDoesNotExistException.class)
                .isThrownBy(() -> order.removeItem(new OrderItemId()));
    }

    @Test
    void shouldFailToRemoveItemFromNonDraftOrder() {
        Order order = OrderTestDataBuilder.brandNewBuilder()
                .status(OrderStatus.PLACED)
                .withItems(true)
                .build();

        OrderItem item = order.items().iterator().next();

        assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.removeItem(item.id()));
    }

}
