package com.algaworks.algashop.odering.domain.valueobject;

import com.algaworks.algashop.odering.domain.entity.OrderTestDataBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertWith;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class BillingTest {

	@Test
	void shouldGenerateWithValidValues() {
		Billing.BillingBuilder builder = OrderTestDataBuilder.aBilling().toBuilder();
		Billing billing = builder.build();

		assertThat(billing.fullName()).isNotNull();
		assertThat(billing.document()).isNotNull();
		assertThat(billing.phone()).isNotNull();
		assertThat(billing.email()).isNotNull();
		assertThat(billing.address()).isNotNull();
	}

	@Test
	void shouldNotAcceptNullFullName() {
		Billing.BillingBuilder builder = OrderTestDataBuilder.aBilling().toBuilder()
				.fullName(null);

		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(builder::build);
	}

	@Test
	void shouldNotAcceptNullDocument() {
		Billing.BillingBuilder builder = OrderTestDataBuilder.aBilling().toBuilder()
				.document(null);

		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(builder::build);
	}

	@Test
	void shouldNotAcceptNullPhone() {
		Billing.BillingBuilder builder = OrderTestDataBuilder.aBilling().toBuilder()
				.phone(null);

		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(builder::build);

	}

	@Test
	void shouldNotAcceptNullEmail() {
		Billing.BillingBuilder builder = OrderTestDataBuilder.aBilling().toBuilder()
				.email(null);

		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(builder::build);
	}

	@Test
	void shouldNotAcceptNullAddress() {
		Billing.BillingBuilder builder = OrderTestDataBuilder.aBilling().toBuilder()
				.address(null);

		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(builder::build);
	}
}
