package com.alperentuncer.payment.saga.order_service.service.impl;


import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alperentuncer.payment.saga.order_service.api.NotFoundException;

import com.alperentuncer.payment.saga.order_service.domain.Order;
import com.alperentuncer.payment.saga.order_service.domain.OrderItem;
import com.alperentuncer.payment.saga.order_service.repository.OrderRepository;
import com.alperentuncer.payment.saga.order_service.service.CreateOrderCommand;
import com.alperentuncer.payment.saga.order_service.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(CreateOrderCommand command) {
        // Basit savunmacı kontroller (API katmanında Bean Validation da yapacağız)
        if (command == null) throw new IllegalArgumentException("CreateOrderCommand zorunlu");
        if (command.customerId() == null) throw new IllegalArgumentException("customerId zorunlu");
        if (command.items() == null || command.items().isEmpty()) {
            throw new IllegalArgumentException("en az bir satır gerekli");
        }

        var items = command.items().stream()
                .map(i -> {
                    if (i == null) throw new IllegalArgumentException("item null olamaz");
                    if (i.productId() == null) throw new IllegalArgumentException("productId zorunlu");
                    if (i.quantity() < 1) throw new IllegalArgumentException("quantity >= 1 olmalı");
                    if (i.unitPrice() == null || i.unitPrice().signum() < 0) {
                        throw new IllegalArgumentException("unitPrice negatif olamaz");
                    }
                    return OrderItem.of(i.productId(), i.quantity(), i.unitPrice());
                })
                .toList();

        var order = Order.create(command.customerId(), items);
        return orderRepository.save(order);
    }

    @Override
@Transactional(readOnly = true)
public Order getOrder(UUID orderId) {
    Objects.requireNonNull(orderId, "orderId zorunlu");
    return orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Sipariş bulunamadı: " + orderId));
}


  @Override
public Order cancelOrder(UUID orderId) {
    Objects.requireNonNull(orderId, "orderId zorunlu");
    var order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Sipariş bulunamadı: " + orderId));

    order.cancel();
    return orderRepository.save(order);
}
}