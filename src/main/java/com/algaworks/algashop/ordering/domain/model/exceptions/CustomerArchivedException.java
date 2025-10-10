package com.algaworks.algashop.ordering.domain.model.exceptions;

import static com.algaworks.algashop.ordering.domain.model.exceptions.ErrorMessages.CUSTOMER_ARCHIVED;

public class CustomerArchivedException extends DomainException {

    public CustomerArchivedException() {
        super(CUSTOMER_ARCHIVED);
    }

    public CustomerArchivedException(Throwable cause) {
        super(CUSTOMER_ARCHIVED, cause);
    }
}
