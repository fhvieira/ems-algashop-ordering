package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;

import java.time.LocalDate;

import static com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

public class OrderTestDataBuilder {
    private CustomerId customerId = DEFAULT_CUSTOMER_ID;
    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    private Billing billing = OrderTestDataBuilder.aBilling();
    private Shipping shipping = OrderTestDataBuilder.aShipping();
    private boolean withItems = true;
    private OrderStatus status = OrderStatus.DRAFT;

    private OrderTestDataBuilder() {

    }

    public static OrderTestDataBuilder brandNewBuilder() {
        return new OrderTestDataBuilder();
    }

    public OrderTestDataBuilder customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestDataBuilder paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderTestDataBuilder billing(Billing billing) {
        this.billing = billing;
        return this;
    }

    public OrderTestDataBuilder shipping(Shipping shipping) {
        this.shipping = shipping;
        return this;
    }

    public OrderTestDataBuilder withItems(boolean withItems) {
        this.withItems = withItems;
        return this;
    }

    public OrderTestDataBuilder status(OrderStatus status) {
        this.status = status;
        return this;
    }

    public static Address anAddress() {
        return Address.builder()
                .street("street name")
                .state("state name")
                .city("city name")
                .zipcode(new Zipcode("12345"))
                .neighborhood("neighborhood")
                .build();
    }

    public static Billing aBilling() {
        return Billing.builder()
                .fullName(new FullName("john", "doe"))
                .phone(new Phone("123-456-7890"))
                .document(new Document("12345678912"))
                .email(new Email("john.doe@email.com"))
                .address(anAddress())
                .build();
    }

    public static Shipping aShipping() {
        Recipient recipient = Recipient.builder()
                .fullName(new FullName("john", "doe"))
                .document(new Document("12345678912"))
                .phone(new Phone("123-456-7890"))
                .build();

        return Shipping.builder()
                .cost(new Money("10"))
                .expectedDate(LocalDate.now().plusDays(2))
                .recipient(recipient)
                .address(anAddress())
                .build();
    }

    public Order build() {
        Order order = Order.draft(customerId);

        order.changePaymentMethod(this.paymentMethod);
        order.changeBilling(this.billing);
        order.changeShipping(this.shipping);

        if (withItems) {
            order.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(1));
            order.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));
        }

        switch (this.status) {
            case DRAFT -> {
            }
            case PLACED -> {
                order.place();
            }
            case PAID -> {
                order.place();
                order.markAsPaid();
            }
            case READY -> {
                order.place();
                order.markAsPaid();
                order.markAsReady();
            }
            case CANCELED -> {
                order.cancel();
            }
        }

        return order;
    }
}
