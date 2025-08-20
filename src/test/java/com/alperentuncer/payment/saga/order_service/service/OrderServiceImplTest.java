package com.alperentuncer.payment.saga.order_service.service;

import com.alperentuncer.payment.saga.order_service.api.NotFoundException;
import com.alperentuncer.payment.saga.order_service.domain.Order;
import com.alperentuncer.payment.saga.order_service.domain.OrderItem;
import com.alperentuncer.payment.saga.order_service.repository.OrderRepository;
import com.alperentuncer.payment.saga.order_service.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderServiceImpl(orderRepository);
    }

    @Test
    void createOrder_should_persist_and_return_domain() {
        var cmd = new CreateOrderCommand(
                UUID.randomUUID(),
                List.of(new CreateOrderCommand.CreateOrderItem(UUID.randomUUID(), 2, new BigDecimal("10.00")))
        );

        // save edilen domain'i, (id vs.) geri döndürüyoruz gibi davranalım
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        var created = orderService.createOrder(cmd);

        assertThat(created.getId()).isNotNull();
        assertThat(created.totalAmount()).isEqualByComparingTo("20.00");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrder_should_throw_not_found_when_absent() {
        var id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrder(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Sipariş bulunamadı");
    }

    @Test
    void cancelOrder_should_change_status_and_save() {
        var id = UUID.randomUUID();
        var order = Order.create(UUID.randomUUID(),
                List.of(OrderItem.of(UUID.randomUUID(), 1, new BigDecimal("5.00"))));
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        var cancelled = orderService.cancelOrder(id);

        assertThat(cancelled.getStatus().name()).isEqualTo("CANCELLED");
        verify(orderRepository).save(any(Order.class));
    }
}
