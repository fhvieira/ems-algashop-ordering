package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.utility.IdGenerator;
import com.algaworks.algashop.odering.domain.valueobject.*;
import com.algaworks.algashop.odering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.odering.domain.valueobject.id.ProductId;

import java.time.LocalDate;

public class OrderTestDataBuilder {
    private CustomerId customerId = new CustomerId();
    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    private BillingInfo billingInfo = getBillingInfo();
    private ShippingInfo shippingInfo = getShippingInfo();
    private Money shippingCost = new Money("100");
    private LocalDate expectedDeliveryDate = LocalDate.now().plusWeeks(1);
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

    public OrderTestDataBuilder billingInfo(BillingInfo billingInfo) {
        this.billingInfo = billingInfo;
        return this;
    }

    public OrderTestDataBuilder shippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
        return this;
    }

    public OrderTestDataBuilder shippingCost(Money shippingCost) {
        this.shippingCost = shippingCost;
        return this;
    }

    public OrderTestDataBuilder expectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
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

    public static Address getAddres() {
        return Address.builder()
                .street("street name")
                .state("state name")
                .city("city name")
                .zipcode(new Zipcode("12345"))
                .neighborhood("neighborhood")
                .build();
    }

    public static BillingInfo getBillingInfo() {
        return BillingInfo.builder()
                .fullName(new FullName("john", "doe"))
                .phone(new Phone("123-456-7890"))
                .document(new Document("12345678912"))
                .address(getAddres())
                .build();
    }

    public static ShippingInfo getShippingInfo() {
        return ShippingInfo.builder()
                .fullName(new FullName("john", "doe"))
                .document(new Document("12345678912"))
                .phone(new Phone("123-456-7890"))
                .address(getAddres())
                .build();
    }

    public Order build() {
        Order order = Order.draft(customerId);
        order.changePaymentMothod(this.paymentMethod);
        order.changeBillingInfo(this.billingInfo);
        order.changeShippingInfo(this.shippingInfo, shippingCost, expectedDeliveryDate);

        if (withItems) {
            order.addItem(new ProductId(IdGenerator.generateTimeBasedUUID()), new ProductName("product name"),
                    new Money("100"), new Quantity(1));

            order.addItem(new ProductId(IdGenerator.generateTimeBasedUUID()), new ProductName("new product name"),
                    new Money("200"), new Quantity(2));
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
            }
            case CANCELED -> {
            }
        }

        return order;
    }
}
