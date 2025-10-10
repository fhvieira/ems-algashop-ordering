package com.algaworks.algashop.ordering.domain.model.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class PhoneTest {

    @Test
    void shouldGenerateWithValidPhone() {
        Phone phone = new Phone("123456789");
        assertThat(phone.value()).isEqualTo("123456789");
    }

    @Test
    void shouldCleanPhoneNumber() {
        Phone phone = new Phone("(12) 3456-7890");
        assertThat(phone.value()).isEqualTo("1234567890");
    }

    @Test
    void shouldNotAcceptNullValue() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Phone(null));
    }

    @Test
    void shouldNotAcceptBlankPhone() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Phone(""));
    }

    @Test
    void shouldNotAcceptEmptyPhone() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Phone("   "));
    }

    @Test
    void shouldNotAcceptPhoneWithLessThan8Digits() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Phone("1234567"));
    }

    @Test
    void shouldAcceptPhoneWithExactly8Digits() {
        Phone phone = new Phone("12345678");
        assertThat(phone.value()).isEqualTo("12345678");
    }

    @Test
    void shouldAcceptPhoneWithMoreThan8Digits() {
        Phone phone = new Phone("123456789012");
        assertThat(phone.value()).isEqualTo("123456789012");
    }

    @Test
    void shouldAcceptPhoneWithPlusSymbol() {
        Phone phone = new Phone("+123456789");
        assertThat(phone.value()).isEqualTo("+123456789");
    }

    @Test
    void shouldReturnCorrectToString() {
        Phone phone = new Phone("123456789");
        assertThat(phone.toString()).isEqualTo("123456789");
    }
}
