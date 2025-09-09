package com.algaworks.algashop.odering.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class MoneyTest {

	@Test
	void shouldGenerateWithValidValue() {
		Money money = new Money(new BigDecimal("10.00"));
		assertThat(money.value()).isEqualTo(new BigDecimal("10.00"));
	}

	@Test
	void shouldScaleToTwoDecimalPlaces() {
		Money money = new Money(new BigDecimal("10.1"));
		assertThat(money.value()).isEqualTo(new BigDecimal("10.10"));
	}

	@Test
	void shouldCreateFromString() {
		Money money = new Money("12.34");
		assertThat(money.value()).isEqualTo(new BigDecimal("12.34"));
	}

	@Test
	void shouldNotAcceptNullValue() {
		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> new Money((BigDecimal) null));
	}

	@Test
	void shouldNotAcceptNegativeValue() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> new Money(new BigDecimal("-1.00")));
	}

	@Test
	void shouldAddTwoMoneyValues() {
		Money a = new Money("10.00");
		Money b = new Money("2.50");
		assertThat(a.add(b).value()).isEqualTo(new BigDecimal("12.50"));
	}

	@Test
	void shouldMultiplyByQuantity() {
		Money price = new Money("3.33");
		Quantity qty = new Quantity(3);
		assertThat(price.multiply(qty).value()).isEqualTo(new BigDecimal("9.99"));
	}

	@Test
	void shouldNotMultiplyByQuantityLessThanOne() {
		Money price = new Money("3.33");
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> price.multiply(new Quantity(0)));
	}

	@Test
	void shouldDivideMoney() {
		Money total = new Money("10.00");
		Money divisor = new Money("4.00");
		assertThat(total.divide(divisor).value()).isEqualTo(new BigDecimal("2.50"));
	}

	@Test
	void shouldBeComparable() {
		Money a = new Money("1.00");
		Money b = new Money("2.00");
		assertThat(a.compareTo(b)).isLessThan(0);
		assertThat(b.compareTo(a)).isGreaterThan(0);
		assertThat(a.compareTo(a)).isEqualTo(0);
	}

	@Test
	void shouldReturnCorrectToString() {
		Money money = new Money("10.00");
		assertThat(money.toString()).isEqualTo("10.00");
	}
}