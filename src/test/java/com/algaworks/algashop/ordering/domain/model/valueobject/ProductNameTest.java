package com.algaworks.algashop.ordering.domain.model.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class ProductNameTest {

	@Test
	void shouldGenerateWithValidName() {
		ProductName name = new ProductName("Widget");
		assertThat(name.value()).isEqualTo("Widget");
	}

	@Test
	void shouldNotAcceptNullValue() {
		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> new ProductName(null));
	}

	@Test
	void shouldNotAcceptBlankName() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> new ProductName(""));
	}

	@Test
	void shouldNotAcceptEmptyName() {
		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> new ProductName("   "));
	}

	@Test
	void shouldReturnCorrectToString() {
		ProductName name = new ProductName("Gadget");
		assertThat(name.toString()).isEqualTo("Gadget");
	}
}
