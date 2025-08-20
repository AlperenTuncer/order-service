package com.alperentuncer.payment.saga.order_service.api.mapper;


import com.alperentuncer.payment.saga.order_service.api.dto.CreateOrderRequest;
import com.alperentuncer.payment.saga.order_service.api.dto.OrderResponse;
import com.alperentuncer.payment.saga.order_service.domain.Order;
import com.alperentuncer.payment.saga.order_service.service.CreateOrderCommand;

import java.util.List;

public final class OrderApiMapper {
    private OrderApiMapper() {}

    public static CreateOrderCommand toCommand(CreateOrderRequest req) {
        var items = req.items().stream()
                .map(i -> new CreateOrderCommand.CreateOrderItem(
                        i.productId(), i.quantity(), i.unitPrice()
                ))
                .toList();
        return new CreateOrderCommand(req.customerId(), items);
    }

    public static OrderResponse toResponse(Order order) {
        List<OrderResponse.OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderResponse.OrderItemResponse(
                        i.getProductId(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.lineTotal()
                )).toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getCreatedAt(),
                order.totalAmount(),
                items
        );
    }
}