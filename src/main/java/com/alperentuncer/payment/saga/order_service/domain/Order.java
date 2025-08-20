package com.alperentuncer.payment.saga.order_service.domain;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final UUID customerId;
    private OrderStatus status;
    private final Instant createdAt;
    private final List<OrderItem> items;

    private Order(UUID id, UUID customerId, List<OrderItem> items, OrderStatus status, Instant createdAt) {
        if (customerId == null) throw new IllegalArgumentException("customerId zorunlu");
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("en az 1 satır olmalı");
        this.id = (id != null) ? id : UUID.randomUUID();
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.status = (status != null) ? status : OrderStatus.CREATED;
        this.createdAt = (createdAt != null) ? createdAt : Instant.now();
    }

    /** Sipariş oluşturma factory metodu */
    public static Order create(UUID customerId, List<OrderItem> items) {
        return new Order(null, customerId, items, OrderStatus.CREATED, null);
    }

    /** Yükleme/yeniden oluşturma için */
    public static Order rehydrate(UUID id, UUID customerId, List<OrderItem> items, OrderStatus status, Instant createdAt) {
        return new Order(id, customerId, items, status, createdAt);
    }

    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }

    public BigDecimal totalAmount() {
        return items.stream()
                .map(OrderItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Durum geçişini domain’de tut: controller/service değiştirmez */
    public void cancel() {
        if (status == OrderStatus.APPROVED) {
            throw new IllegalStateException("Onaylanmış sipariş iptal edilemez");
        }
        if (status == OrderStatus.CANCELLED) return;
        this.status = OrderStatus.CANCELLED;
    }

    public void approve() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Sadece CREATED → APPROVED geçişine izin var");
        }
        this.status = OrderStatus.APPROVED;
    }

    public void reject() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Sadece CREATED → REJECTED geçişine izin var");
        }
        this.status = OrderStatus.REJECTED;
    }
}