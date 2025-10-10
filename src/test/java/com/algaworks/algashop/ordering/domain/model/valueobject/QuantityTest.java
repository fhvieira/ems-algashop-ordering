package com.algaworks.algashop.ordering.domain.model.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class QuantityTest {

	@Test
	void shouldGenerateWithValidValue() {
		Quantity quantity = new Quantity(5);
		assertThat(quantity.value()).isEqualTo(5);
	}

	@Test
	void shouldNotAcceptNullValue() {
		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> new Quantity(null));
	}

	@Test
	void shouldNotAcceptNegativeValue() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> new Quantity(-1));
	}

	@Test
	void shouldAddTwoQuantities() {
		Quantity a = new Quantity(2);
		Quantity b = new Quantity(3);
		assertThat(a.add(b).value()).isEqualTo(5);
	}

	@Test
	void shouldBeComparable() {
		Quantity a = new Quantity(1);
		Quantity b = new Quantity(2);
		assertThat(a.compareTo(b)).isLessThan(0);
		assertThat(b.compareTo(a)).isGreaterThan(0);
		assertThat(a.compareTo(a)).isEqualTo(0);
	}

	@Test
	void shouldReturnCorrectToString() {
		Quantity quantity = new Quantity(10);
		assertThat(quantity.toString()).isEqualTo("10");
	}
}
