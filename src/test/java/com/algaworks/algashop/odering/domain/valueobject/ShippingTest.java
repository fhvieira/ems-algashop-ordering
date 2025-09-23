package com.algaworks.algashop.odering.domain.valueobject;

import com.algaworks.algashop.odering.domain.entity.OrderTestDataBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class ShippingTest {

	@Test
	void shouldGenerateWithValidValues() {
		FullName fullName = new FullName("Jane", "Doe");
		Document document = new Document("98765432100");
		Phone phone = new Phone("987654321");

		Recipient recipient = Recipient.builder()
				.fullName(fullName)
				.phone(phone)
				.document(document)
				.build();

		Address address = Address.builder()
				.street("Second St")
				.additionalInfo("Suite 200")
				.neighborhood("Uptown")
				.city("Gotham")
				.state("GT")
				.zipcode(new Zipcode("12345"))
				.build();

		Shipping shipping = Shipping.builder()
				.recipient(recipient)
				.cost(new Money("10"))
				.expectedDate(LocalDate.now().plusDays(3))
				.address(address)
				.build();

		assertThat(shipping.recipient().fullName()).isEqualTo(fullName);
		assertThat(shipping.recipient().document()).isEqualTo(document);
		assertThat(shipping.recipient().phone()).isEqualTo(phone);
		assertThat(shipping.address()).isEqualTo(address);
	}

	@Test
	void shouldNotAcceptNullFullName() {
		Recipient recipient = Recipient.builder()
				.fullName(new FullName("john", "doe"))
				.document(new Document("98765432100"))
				.phone(new Phone("123-123-1234"))
				.build();
		Address address = Address.builder()
				.street("Second St")
				.neighborhood("Uptown")
				.city("Gotham")
				.state("GT")
				.zipcode(new Zipcode("12345"))
				.build();

		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> Shipping.builder()
						.recipient(recipient)
						.address(address)
						.cost(new Money("10"))
						.build());
	}
}
