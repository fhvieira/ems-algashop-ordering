package com.algaworks.algashop.odering.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class BillingInfoTest {

	@Test
	void shouldGenerateWithValidValues() {
		FullName fullName = new FullName("John", "Doe");
		Document document = new Document("12345678901");
		Phone phone = new Phone("123456789");
		Zipcode zipcode = new Zipcode("12345");
		Address address = Address.builder()
				.street("Main St")
				.additionalInfo("Apt 1")
				.neighborhood("Downtown")
				.city("Metropolis")
				.state("ST")
				.zipcode(zipcode)
				.build();

		BillingInfo billing = new BillingInfo(fullName, document, phone, address);
		assertThat(billing.fullName()).isEqualTo(fullName);
		assertThat(billing.document()).isEqualTo(document);
		assertThat(billing.phone()).isEqualTo(phone);
		assertThat(billing.address()).isEqualTo(address);
	}

	@Test
	void shouldNotAcceptNullFullName() {
		Document document = new Document("12345678901");
		Phone phone = new Phone("123456789");
		Address address = Address.builder()
				.street("Main St")
				.neighborhood("Downtown")
				.city("Metropolis")
				.state("ST")
				.zipcode(new Zipcode("12345"))
				.build();
		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> new BillingInfo(null, document, phone, address));
	}

	@Test
	void shouldNotAcceptNullDocument() {
		FullName fullName = new FullName("John", "Doe");
		Phone phone = new Phone("123456789");
		Address address = Address.builder()
				.street("Main St")
				.neighborhood("Downtown")
				.city("Metropolis")
				.state("ST")
				.zipcode(new Zipcode("12345"))
				.build();
		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> new BillingInfo(fullName, null, phone, address));
	}

	@Test
	void shouldNotAcceptNullPhone() {
		FullName fullName = new FullName("John", "Doe");
		Document document = new Document("12345678901");
		Address address = Address.builder()
				.street("Main St")
				.neighborhood("Downtown")
				.city("Metropolis")
				.state("ST")
				.zipcode(new Zipcode("12345"))
				.build();
		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> new BillingInfo(fullName, document, null, address));
	}

	@Test
	void shouldNotAcceptNullAddress() {
		FullName fullName = new FullName("John", "Doe");
		Document document = new Document("12345678901");
		Phone phone = new Phone("123456789");
		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> new BillingInfo(fullName, document, phone, null));
	}
}
