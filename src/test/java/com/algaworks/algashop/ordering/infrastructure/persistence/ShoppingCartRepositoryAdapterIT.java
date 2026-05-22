package com.algaworks.algashop.ordering.infrastructure.persistence;

import com.algaworks.algashop.ordering.domain.model.entity.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartJpaAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartJpaDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerJpaEntityTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartJpaEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.CustomerJpaRepository;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.ShoppingCartJpaRepository;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.ShoppingCartRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        ShoppingCartRepositoryAdapter.class,
        ShoppingCartJpaAssembler.class,
        ShoppingCartJpaDisassembler.class,
        SpringDataAuditingConfig.class
})
class ShoppingCartRepositoryAdapterIT {

    private final ShoppingCartRepositoryAdapter shoppingCartRepository;
    private final ShoppingCartJpaRepository entityRepository;
    private final CustomerJpaRepository customerJpaRepository;

    @Autowired
    ShoppingCartRepositoryAdapterIT(ShoppingCartRepositoryAdapter shoppingCartRepository,
                                    ShoppingCartJpaRepository entityRepository,
                                    CustomerJpaRepository customerJpaRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.entityRepository = entityRepository;
        this.customerJpaRepository = customerJpaRepository;
    }

    @BeforeEach
    void setup() {
        if (!customerJpaRepository.existsById(DEFAULT_CUSTOMER_ID.value())) {
            customerJpaRepository.saveAndFlush(CustomerJpaEntityTestDataBuilder.aCustomer().build());
        }
    }

    @Test
    void shouldPersistShoppingCartWithMultipleItems() {
        ShoppingCart cart = shoppingCartWithTwoItems();

        shoppingCartRepository.add(cart);

        ShoppingCartJpaEntity entity = entityRepository.findById(cart.id().value()).orElseThrow();
        assertThat(entity.getItems()).hasSize(2);
        assertThat(entity.getTotalItems()).isEqualTo(3);
        assertThat(entity.getTotalAmount()).isEqualByComparingTo(new BigDecimal("270.00"));
    }

    @Test
    void shouldFillCreatedAtAndLastModifiedAtAfterSave() {
        ShoppingCart cart = shoppingCartWithTwoItems();

        shoppingCartRepository.add(cart);

        ShoppingCartJpaEntity entity = entityRepository.findById(cart.id().value()).orElseThrow();
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getLastModifiedAt()).isNotNull();
    }

    @Test
    void shouldFindShoppingCartByCustomerIdAndKeepItemData() {
        Product product = ProductTestDataBuilder.aProduct().build();
        Product altProduct = ProductTestDataBuilder.anAltProduct().build();
        ShoppingCart cart = ShoppingCart.startShopping(DEFAULT_CUSTOMER_ID);
        cart.addItem(product, new Quantity(2));
        cart.addItem(altProduct, new Quantity(1));
        shoppingCartRepository.add(cart);

        ShoppingCart foundCart = shoppingCartRepository.ofCustomer(DEFAULT_CUSTOMER_ID).orElseThrow();

        assertThat(foundCart.id()).isEqualTo(cart.id());
        assertThat(foundCart.customerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(foundCart.totalItems()).isEqualTo(new Quantity(3));
        assertThat(foundCart.totalAmount().value()).isEqualByComparingTo(new BigDecimal("270.00"));
        assertThat(foundCart.items()).hasSize(2);
        assertThat(foundCart.findItem(product.id()).shoppingCartId()).isEqualTo(cart.id());
        assertThat(foundCart.findItem(product.id()).quantity()).isEqualTo(new Quantity(2));
        assertThat(foundCart.findItem(product.id()).totalAmount().value()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(foundCart.findItem(altProduct.id()).quantity()).isEqualTo(new Quantity(1));
        assertThat(foundCart.findItem(altProduct.id()).totalAmount().value()).isEqualByComparingTo(new BigDecimal("70.00"));
    }

    @Test
    void shouldRemoveShoppingCart() {
        ShoppingCart cart = shoppingCartWithTwoItems();
        shoppingCartRepository.add(cart);

        shoppingCartRepository.remove(cart.id());

        assertThat(shoppingCartRepository.ofId(cart.id())).isEmpty();
        assertThat(entityRepository.existsById(cart.id().value())).isFalse();
    }

    @Test
    void shouldUpdateExistingShoppingCartAndPersistNewVersion() {
        Product product = ProductTestDataBuilder.aProduct().build();
        ShoppingCart cart = ShoppingCart.startShopping(DEFAULT_CUSTOMER_ID);
        cart.addItem(product, new Quantity(1));
        shoppingCartRepository.add(cart);
        Long firstVersion = cart.version();

        cart.changeItemQuantity(cart.findItem(product.id()).id(), new Quantity(3));
        shoppingCartRepository.add(cart);

        ShoppingCartJpaEntity entity = entityRepository.findById(cart.id().value()).orElseThrow();
        ShoppingCart updatedCart = shoppingCartRepository.ofId(cart.id()).orElseThrow();
        assertThat(firstVersion).isNotNull();
        assertThat(cart.version()).isGreaterThan(firstVersion);
        assertThat(entity.getVersion()).isEqualTo(cart.version());
        assertThat(updatedCart.version()).isEqualTo(cart.version());
        assertThat(updatedCart.totalItems()).isEqualTo(new Quantity(3));
        assertThat(updatedCart.totalAmount().value()).isEqualByComparingTo(new BigDecimal("300.00"));
    }

    private ShoppingCart shoppingCartWithTwoItems() {
        ShoppingCart cart = ShoppingCart.startShopping(DEFAULT_CUSTOMER_ID);
        cart.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(2));
        cart.addItem(ProductTestDataBuilder.anAltProduct().build(), new Quantity(1));
        return cart;
    }
}
