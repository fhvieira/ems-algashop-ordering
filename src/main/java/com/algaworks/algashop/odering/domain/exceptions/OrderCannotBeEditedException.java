package com.algaworks.algashop.odering.domain.exceptions;

import com.algaworks.algashop.odering.domain.valueobject.OrderStatus;
import com.algaworks.algashop.odering.domain.valueobject.id.OrderId;

import static com.algaworks.algashop.odering.domain.exceptions.ErrorMessages.ORDER_CANNOT_BE_EDITED;

public class OrderCannotBeEditedException extends DomainException {
    public OrderCannotBeEditedException(OrderId id, OrderStatus status) {
        super(String.format(ORDER_CANNOT_BE_EDITED, id, status));
    }
}
