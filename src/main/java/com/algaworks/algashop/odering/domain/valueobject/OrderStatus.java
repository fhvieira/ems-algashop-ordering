package com.algaworks.algashop.odering.domain.valueobject;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    DRAFT,
    PLACED(DRAFT),
    PAID(PLACED),
    READY(PAID),
    CANCELED(DRAFT, PLACED, PAID, READY);

    private final List<OrderStatus> allowedTransitions;

    OrderStatus(OrderStatus... allowedTransitions) {
        this.allowedTransitions = Arrays.asList(allowedTransitions);
    }

    public boolean canChangeTo(OrderStatus newStatus) {
        OrderStatus currentStatus = this;
        return newStatus.allowedTransitions.contains(currentStatus);
    }

    public boolean canNotChangeTo(OrderStatus newStatus) {
        return !canChangeTo(newStatus);
    }

}