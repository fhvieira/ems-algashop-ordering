package com.algaworks.algashop.odering.domain.exceptions;

import static com.algaworks.algashop.odering.domain.exceptions.ErrorMessages.CUSTOMER_ARCHIVED;

public class CustomerArchivedException extends DomainException {

    public CustomerArchivedException() {
        super(CUSTOMER_ARCHIVED);
    }

    public CustomerArchivedException(Throwable cause) {
        super(CUSTOMER_ARCHIVED, cause);
    }
}
