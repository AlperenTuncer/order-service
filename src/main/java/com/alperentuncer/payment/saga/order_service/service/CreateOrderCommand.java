package com.alperentuncer.payment.saga.order_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(
        UUID customerId,
        List<CreateOrderItem> items
) {
    public record CreateOrderItem(
            UUID productId,
            int quantity,
            BigDecimal unitPrice
    ) {}
}