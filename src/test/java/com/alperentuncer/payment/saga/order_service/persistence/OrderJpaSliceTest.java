package com.alperentuncer.payment.saga.order_service.persistence;

import com.alperentuncer.payment.saga.order_service.domain.Order;
import com.alperentuncer.payment.saga.order_service.domain.OrderItem;
import com.alperentuncer.payment.saga.order_service.persistence.adapter.OrderRepositoryAdapter;
import com.alperentuncer.payment.saga.order_service.persistence.jpa.OrderJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class OrderJpaSliceTest {

    @Autowired
    private OrderJpaRepository jpaRepository;

    @Test
    void save_and_find_by_id_should_work() {
        var adapter = new OrderRepositoryAdapter(jpaRepository);

        var order = Order.create(
                UUID.randomUUID(),
                List.of(OrderItem.of(UUID.randomUUID(), 2, new BigDecimal("7.50")))
        );

        var saved = adapter.save(order);
        var found = adapter.findById(saved.getId()).orElseThrow();

        assertThat(found.totalAmount()).isEqualByComparingTo("15.00");
        assertThat(found.getItems()).hasSize(1);
    }
}
