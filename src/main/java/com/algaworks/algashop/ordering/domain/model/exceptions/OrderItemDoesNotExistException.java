package com.algaworks.algashop.ordering.domain.model.exceptions;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;

import static com.algaworks.algashop.ordering.domain.model.exceptions.ErrorMessages.ORDER_ITEM_DOES_NOT_EXIST;

public class OrderItemDoesNotExistException extends DomainException {

    public OrderItemDoesNotExistException(OrderId orderId, OrderItemId itemId) {
        super(String.format(ORDER_ITEM_DOES_NOT_EXIST, orderId, itemId));
    }
}
