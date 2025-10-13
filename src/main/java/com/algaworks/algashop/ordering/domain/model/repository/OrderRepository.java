package com.algaworks.algashop.ordering.domain.model.repository;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;
import org.springframework.stereotype.Component;

public interface OrderRepository extends Repository<Order,  OrderId>{
}
