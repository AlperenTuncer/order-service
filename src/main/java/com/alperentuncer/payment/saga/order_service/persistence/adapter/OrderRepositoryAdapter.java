package com.alperentuncer.payment.saga.order_service.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.alperentuncer.payment.saga.order_service.domain.Order;
import com.alperentuncer.payment.saga.order_service.persistence.jpa.OrderJpaRepository;
import com.alperentuncer.payment.saga.order_service.persistence.mapper.OrderMapper;
import com.alperentuncer.payment.saga.order_service.repository.OrderRepository;

@Repository
@Transactional
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository jpa;

    public OrderRepositoryAdapter(OrderJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Order save(Order order) {
        var saved = jpa.save(OrderMapper.toEntity(order));
        return OrderMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(UUID id) {
        return jpa.findById(id).map(OrderMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomerId(UUID customerId) {
        return jpa.findByCustomerId(customerId).stream()
                .map(OrderMapper::toDomain)
                .toList();
    }
}