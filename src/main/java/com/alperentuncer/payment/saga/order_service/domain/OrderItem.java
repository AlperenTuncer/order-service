package com.alperentuncer.payment.saga.order_service.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public final class OrderItem {
    private final UUID productId;
    private final int quantity;
    private final BigDecimal unitPrice;

    private OrderItem(UUID productId, int quantity, BigDecimal unitPrice) {
        if (productId == null) throw new IllegalArgumentException("productId zorunlu");
        if (quantity < 1) throw new IllegalArgumentException("quantity >= 1 olmalı");
        if (unitPrice == null || unitPrice.signum() < 0) throw new IllegalArgumentException("unitPrice negatif olamaz");
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public static OrderItem of(UUID productId, int quantity, BigDecimal unitPrice) {
        return new OrderItem(productId, quantity, unitPrice);
    }

    public UUID getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Value Object eşitlik
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem that)) return false;
        return quantity == that.quantity &&
               Objects.equals(productId, that.productId) &&
               Objects.equals(unitPrice, that.unitPrice);
    }
    @Override public int hashCode() { return Objects.hash(productId, quantity, unitPrice); }
}