package com.algaworks.algashop.odering.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void shouldGenerateWithValidEmail() {
        Email email = new Email("test@example.com");
        assertThat(email.value()).isEqualTo("test@example.com");
    }

    @Test
    void shouldNormalizeEmailToLowerCase() {
        Email email = new Email("TEST@EXAMPLE.COM");
        assertThat(email.value()).isEqualTo("test@example.com");
    }

    @Test
    void shouldTrimEmailWhitespace() {
        Email email = new Email("  test@example.com  ");
        assertThat(email.value()).isEqualTo("test@example.com");
    }

    @Test
    void shouldNotAcceptNullValue() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Email(null));
    }

    @Test
    void shouldNotAcceptBlankEmail() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email(""));
    }

    @Test
    void shouldNotAcceptEmptyEmail() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email("   "));
    }

    @Test
    void shouldNotAcceptInvalidEmailFormat() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email("invalid-email"));
    }

    @Test
    void shouldNotAcceptEmailWithoutAtSymbol() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email("testexample.com"));
    }

    @Test
    void shouldNotAcceptEmailWithoutDomain() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email("test@"));
    }

    @Test
    void shouldAcceptValidEmailWithSubdomain() {
        Email email = new Email("test@sub.example.com");
        assertThat(email.value()).isEqualTo("test@sub.example.com");
    }

    @Test
    void shouldReturnCorrectToString() {
        Email email = new Email("test@example.com");
        assertThat(email.toString()).isEqualTo("test@example.com");
    }
}
