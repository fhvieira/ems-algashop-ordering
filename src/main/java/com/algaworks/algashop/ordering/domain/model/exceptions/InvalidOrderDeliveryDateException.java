package com.algaworks.algashop.ordering.domain.model.exceptions;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;

import static com.algaworks.algashop.ordering.domain.model.exceptions.ErrorMessages.ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST;

public class InvalidOrderDeliveryDateException extends DomainException {
    public InvalidOrderDeliveryDateException(OrderId id) {
        super(String.format(ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST));
    }
}
