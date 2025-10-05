package com.algaworks.algashop.odering.domain.exceptions;

import com.algaworks.algashop.odering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.odering.domain.valueobject.id.OrderItemId;

import static com.algaworks.algashop.odering.domain.exceptions.ErrorMessages.ORDER_ITEM_DOES_NOT_EXIST;

public class OrderItemDoesNotExistException extends DomainException {

    public OrderItemDoesNotExistException(OrderId orderId, OrderItemId itemId) {
        super(String.format(ORDER_ITEM_DOES_NOT_EXIST, orderId, itemId));
    }
}
