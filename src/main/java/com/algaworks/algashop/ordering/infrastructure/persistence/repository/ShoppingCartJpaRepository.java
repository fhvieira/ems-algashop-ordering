package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartJpaRepository extends JpaRepository<ShoppingCartJpaEntity, UUID> {

    Optional<ShoppingCartJpaEntity> findByCustomerId(UUID customerId);

    void removeById(UUID id);
}
