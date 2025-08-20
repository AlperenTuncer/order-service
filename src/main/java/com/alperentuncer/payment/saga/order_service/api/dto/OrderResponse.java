package com.alperentuncer.payment.saga.order_service.api.dto;

import com.alperentuncer.payment.saga.order_service.domain.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID customerId,
        OrderStatus status,
        Instant createdAt,
        BigDecimal totalAmount,
        List<OrderItemResponse> items
) {
    public record OrderItemResponse(
            UUID productId,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal
    ) {}
}