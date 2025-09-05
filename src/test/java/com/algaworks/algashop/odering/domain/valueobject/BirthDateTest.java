package com.algaworks.algashop.odering.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class BirthDateTest {

    @Test
    void shouldGenerateWithValue() {
        LocalDate date = LocalDate.of(1990, 1, 1);
        BirthDate birthDate = new BirthDate(date);
        assertThat(birthDate.value()).isEqualTo(date);
    }

    @Test
    void shouldCalculateAge() {
        LocalDate date = LocalDate.now().minusYears(25);
        BirthDate birthDate = new BirthDate(date);
        assertThat(birthDate.age()).isEqualTo(25);
    }

    @Test
    void shouldNotAcceptNullValue() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new BirthDate(null));
    }

    @Test
    void shouldNotAcceptFutureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new BirthDate(futureDate));
    }

    @Test
    void shouldAcceptTodayDate() {
        LocalDate today = LocalDate.now();
        BirthDate birthDate = new BirthDate(today);
        assertThat(birthDate.value()).isEqualTo(today);
        assertThat(birthDate.age()).isEqualTo(0);
    }

    @Test
    void shouldReturnCorrectToString() {
        LocalDate date = LocalDate.of(1990, 1, 1);
        BirthDate birthDate = new BirthDate(date);
        assertThat(birthDate.toString()).isEqualTo("1990-01-01");
    }
}
