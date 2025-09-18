package com.algaworks.algashop.odering.domain.entity;

import com.algaworks.algashop.odering.domain.exceptions.CustomerArchivedException;
import com.algaworks.algashop.odering.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertWith;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    @Test
    void givenAnInvalidEmail_whenTryingToCreateCustomer_shouldGenerateException() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CustomerTestDataBuilder.brandNewCustomerBuilder()
                        .email(new Email("john.doe_email.com"))
                        .build());
    }

    @Test
    void givenAnInvalidEmail_whenTryingToChangeCustomerEmail_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuilder().build();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("john.doe_email.com")));
    }

    @Test
    void givenValidCustomer_whenAddingNonPositiveLoyaltyPoints_shouldGenerateException() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuilder().build();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(0)));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));
    }

    @Test
    void givenValidCustomer_whenAddingLoyaltyPoints_shouldOnlyAdd() {
        LoyaltyPoints pointsToBeAdded = new LoyaltyPoints(10);
        Customer customer = CustomerTestDataBuilder.brandNewCustomerBuilder().build();
        customer.addLoyaltyPoints(pointsToBeAdded);
        assertEquals(pointsToBeAdded, customer.loyaltyPoints());
    }

    @Test
    void givenNonArchivedCustomer_whenArchiving_shouldAnonymize() {
        Customer customer = CustomerTestDataBuilder.existingCustomerBuilder().build();
        customer.archive();
        assertWith(customer,
                c -> assertEquals(new FullName("anonymous", "anonymous"), c.fullName()),
                c -> assertEquals(new Email("anonymous@email.com"), c.email()),
                c -> assertEquals(new Phone("000-000-0000"), c.phone()),
                c -> assertEquals(new Document("000-00-0000"), c.document()),
                c -> assertNull(c.birthDate()),
                c -> assertFalse(c.isPromotionNotificationsAllowed()),
                c -> assertEquals("anonymous", c.address().additionalInfo())
        );
    }

    @Test
    void givenArchivedCustomer_whenTryingToUpdate_shouldThrowException() {
        Customer customer = CustomerTestDataBuilder.existingAnonymizedCustomerBuilder().build();

        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeName(new FullName("new", "name")));
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("newemail@email.com")));
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone(new Phone("000-000-0000")));
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(10)));
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::enablePromotionNotification);
        assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::disablePromotionNotification);
    }

}