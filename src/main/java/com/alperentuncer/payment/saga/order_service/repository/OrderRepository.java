package com.alperentuncer.payment.saga.order_service.repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.alperentuncer.payment.saga.order_service.domain.Order;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(UUID id);
    List<Order> findByCustomerId(UUID customerId);
}