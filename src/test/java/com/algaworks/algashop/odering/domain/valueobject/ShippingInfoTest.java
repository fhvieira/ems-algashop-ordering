package com.algaworks.algashop.odering.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class ShippingInfoTest {

	@Test
	void shouldGenerateWithValidValues() {
		FullName fullName = new FullName("Jane", "Doe");
		Document document = new Document("98765432100");
		Phone phone = new Phone("987654321");
		Zipcode zipcode = new Zipcode("12345");
		Address address = Address.builder()
				.street("Second St")
				.additionalInfo("Suite 200")
				.neighborhood("Uptown")
				.city("Gotham")
				.state("GT")
				.zipcode(zipcode)
				.build();

		ShippingInfo shipping = new ShippingInfo(fullName, document, phone, address);
		assertThat(shipping.fullName()).isEqualTo(fullName);
		assertThat(shipping.document()).isEqualTo(document);
		assertThat(shipping.phone()).isEqualTo(phone);
		assertThat(shipping.address()).isEqualTo(address);
	}

	@Test
	void shouldNotAcceptNullFullName() {
		Document document = new Document("98765432100");
		Phone phone = new Phone("987654321");
		Address address = Address.builder()
				.street("Second St")
				.neighborhood("Uptown")
				.city("Gotham")
				.state("GT")
				.zipcode(new Zipcode("12345"))
				.build();
		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> new ShippingInfo(null, document, phone, address));
	}

	@Test
	void shouldNotAcceptNullDocument() {
		FullName fullName = new FullName("Jane", "Doe");
		Phone phone = new Phone("987654321");
		Address address = Address.builder()
				.street("Second St")
				.neighborhood("Uptown")
				.city("Gotham")
				.state("GT")
				.zipcode(new Zipcode("12345"))
				.build();
		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> new ShippingInfo(fullName, null, phone, address));
	}

	@Test
	void shouldNotAcceptNullPhone() {
		FullName fullName = new FullName("Jane", "Doe");
		Document document = new Document("98765432100");
		Address address = Address.builder()
				.street("Second St")
				.neighborhood("Uptown")
				.city("Gotham")
				.state("GT")
				.zipcode(new Zipcode("12345"))
				.build();
		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> new ShippingInfo(fullName, document, null, address));
	}

	@Test
	void shouldNotAcceptNullAddress() {
		FullName fullName = new FullName("Jane", "Doe");
		Document document = new Document("98765432100");
		Phone phone = new Phone("987654321");
		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> new ShippingInfo(fullName, document, phone, null));
	}
}
