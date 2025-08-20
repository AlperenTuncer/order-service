package com.alperentuncer.payment.saga.order_service.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.alperentuncer.payment.saga.order_service.domain.Order;
import com.alperentuncer.payment.saga.order_service.domain.OrderItem;
import com.alperentuncer.payment.saga.order_service.persistence.entity.OrderEntity;
import com.alperentuncer.payment.saga.order_service.persistence.entity.OrderItemEmbeddable;

public final class OrderMapper {

    private OrderMapper() {}

    public static OrderEntity toEntity(Order order) {
        var itemEmbeddables = order.getItems().stream()
                .map(i -> new OrderItemEmbeddable(i.getProductId(), i.getQuantity(), i.getUnitPrice()))
                .collect(Collectors.toList());

        return new OrderEntity(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getCreatedAt(),
                order.totalAmount(),
                itemEmbeddables
        );
    }

    public static Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(i -> OrderItem.of(i.getProductId(), i.getQuantity(), i.getUnitPrice()))
                .collect(Collectors.toList());

        return Order.rehydrate(
                entity.getId(),
                entity.getCustomerId(),
                items,
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}