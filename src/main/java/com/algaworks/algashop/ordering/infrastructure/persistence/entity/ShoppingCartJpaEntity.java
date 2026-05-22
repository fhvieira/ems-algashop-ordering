package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "\"shopping_cart\"")
@EntityListeners(AuditingEntityListener.class)
public class ShoppingCartJpaEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private BigDecimal totalAmount;
    private Integer totalItems;

    @CreatedBy
    private UUID createdByUserId;

    @CreatedDate
    private OffsetDateTime createdAt;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @Version
    private Long version;

    @JoinColumn
    @ManyToOne(optional = false)
    private CustomerJpaEntity customer;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShoppingCartItemJpaEntity> items = new HashSet<>();

    public void replaceItems(Set<ShoppingCartItemJpaEntity> newItems) {
        if (newItems == null || newItems.isEmpty()) {
            return;
        }

        newItems.forEach(i -> i.setShoppingCart(this));
        this.items.clear();
        this.items.addAll(newItems);
    }
}
