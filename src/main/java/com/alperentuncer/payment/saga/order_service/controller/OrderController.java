package com.alperentuncer.payment.saga.order_service.controller;

import com.alperentuncer.payment.saga.order_service.api.NotFoundException;
import com.alperentuncer.payment.saga.order_service.api.dto.CreateOrderRequest;
import com.alperentuncer.payment.saga.order_service.api.dto.OrderResponse;
import com.alperentuncer.payment.saga.order_service.api.mapper.OrderApiMapper;
import com.alperentuncer.payment.saga.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        var command = OrderApiMapper.toCommand(request);
        var created = orderService.createOrder(command);
        var body = OrderApiMapper.toResponse(created);
        return ResponseEntity
                .created(URI.create("/api/v1/orders/" + created.getId()))
                .body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable("id") UUID id) {
        var order = orderService.getOrder(id);
        return ResponseEntity.ok(OrderApiMapper.toResponse(order));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable("id") UUID id) {
        var order = orderService.cancelOrder(id);
        return ResponseEntity.ok(OrderApiMapper.toResponse(order));
    }

    // (İsteğe bağlı) 404'ü daha netleştirmek için service'te IllegalArgument yerine NotFoundException kullan
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalToNotFound(IllegalArgumentException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("Sipariş bulunamadı")) {
            throw new NotFoundException(ex.getMessage());
        }
        throw ex;
    }
}