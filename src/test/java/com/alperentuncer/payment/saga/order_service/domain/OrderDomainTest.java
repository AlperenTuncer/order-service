package com.alperentuncer.payment.saga.order_service.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class OrderDomainTest {

    @Test
    void create_order_with_valid_items_should_calculate_total() {
        var item1 = OrderItem.of(UUID.randomUUID(), 2, new BigDecimal("10.50"));
        var item2 = OrderItem.of(UUID.randomUUID(), 1, new BigDecimal("5.00"));

        var order = Order.create(UUID.randomUUID(), List.of(item1, item2));

        assertThat(order.totalAmount()).isEqualByComparingTo("26.00");
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(order.getItems()).hasSize(2);
    }

    @Test
    void create_order_with_empty_items_should_fail() {
        assertThatThrownBy(() -> Order.create(UUID.randomUUID(), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("en az 1 satır olmalı");
    }

    @Test
    void item_with_negative_price_should_fail() {
        assertThatThrownBy(() -> OrderItem.of(UUID.randomUUID(), 1, new BigDecimal("-1.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("negatif");
    }

    @Test
    void cancel_on_approved_order_should_fail() {
        var item = OrderItem.of(UUID.randomUUID(), 1, new BigDecimal("1.00"));
        var order = Order.create(UUID.randomUUID(), List.of(item));
        order.approve();

        assertThatThrownBy(order::cancel)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Onaylanmış");
    }

    @Test
    void approve_from_created_should_work() {
        var item = OrderItem.of(UUID.randomUUID(), 1, new BigDecimal("1.00"));
        var order = Order.create(UUID.randomUUID(), List.of(item));

        order.approve();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.APPROVED);
    }
}
