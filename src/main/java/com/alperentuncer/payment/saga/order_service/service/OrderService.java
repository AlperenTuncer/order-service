package com.alperentuncer.payment.saga.order_service.service;

import com.alperentuncer.payment.saga.order_service.domain.Order;

import java.util.UUID;

public interface OrderService {
    /** Use-case: Sipariş oluşturma */
    Order createOrder(CreateOrderCommand command);

    /** Use-case: Siparişi görüntüleme */
    Order getOrder(UUID orderId);

    /** Use-case: Sipariş iptali */
    Order cancelOrder(UUID orderId);
}