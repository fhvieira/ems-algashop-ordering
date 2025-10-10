package com.algaworks.algashop.ordering.domain.model.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class FullNameTest {

    @Test
    void shouldGenerateWithValidNames() {
        FullName fullName = new FullName("John", "Doe");
        assertThat(fullName.firstName()).isEqualTo("John");
        assertThat(fullName.lastName()).isEqualTo("Doe");
    }

    @Test
    void shouldTrimNames() {
        FullName fullName = new FullName("  John  ", "  Doe  ");
        assertThat(fullName.firstName()).isEqualTo("John");
        assertThat(fullName.lastName()).isEqualTo("Doe");
    }

    @Test
    void shouldNotAcceptNullFirstName() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new FullName(null, "Doe"));
    }

    @Test
    void shouldNotAcceptNullLastName() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new FullName("John", null));
    }

    @Test
    void shouldNotAcceptBlankFirstName() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FullName("", "Doe"));
    }

    @Test
    void shouldNotAcceptBlankLastName() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FullName("John", ""));
    }

    @Test
    void shouldNotAcceptEmptyFirstName() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FullName("   ", "Doe"));
    }

    @Test
    void shouldNotAcceptEmptyLastName() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new FullName("John", "   "));
    }

    @Test
    void shouldAcceptNamesWithSpecialCharacters() {
        FullName fullName = new FullName("João", "Silva");
        assertThat(fullName.firstName()).isEqualTo("João");
        assertThat(fullName.lastName()).isEqualTo("Silva");
    }

    @Test
    void shouldAcceptNamesWithNumbers() {
        FullName fullName = new FullName("John2", "Doe3");
        assertThat(fullName.firstName()).isEqualTo("John2");
        assertThat(fullName.lastName()).isEqualTo("Doe3");
    }

    @Test
    void shouldReturnCorrectToString() {
        FullName fullName = new FullName("John", "Doe");
        assertThat(fullName.toString()).isEqualTo("John Doe");
    }
}
