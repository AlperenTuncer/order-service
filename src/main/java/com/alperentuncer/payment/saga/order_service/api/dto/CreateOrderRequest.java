package com.alperentuncer.payment.saga.order_service.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID customerId,
        @NotNull @Size(min = 1, message = "En az bir satır olmalı")
        List<@Valid CreateOrderItemRequest> items
) {
    public record CreateOrderItemRequest(
            @NotNull UUID productId,
            @Min(value = 1, message = "quantity >= 1 olmalı") int quantity,
            @NotNull @DecimalMin(value = "0.0", inclusive = true, message = "unitPrice negatif olamaz")
            BigDecimal unitPrice
    ) {}
}