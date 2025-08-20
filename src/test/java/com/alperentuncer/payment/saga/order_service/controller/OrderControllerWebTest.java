package com.alperentuncer.payment.saga.order_service.controller;

import com.alperentuncer.payment.saga.order_service.api.GlobalExceptionHandler;
import com.alperentuncer.payment.saga.order_service.api.NotFoundException;
import com.alperentuncer.payment.saga.order_service.domain.Order;
import com.alperentuncer.payment.saga.order_service.domain.OrderItem;
import com.alperentuncer.payment.saga.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@Import(GlobalExceptionHandler.class)
class OrderControllerWebTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    OrderService orderService;

    @Test
    void create_should_return_201_and_body() throws Exception {
        var order = Order.create(UUID.randomUUID(),
                List.of(OrderItem.of(UUID.randomUUID(), 2, new BigDecimal("10.00"))));

        Mockito.when(orderService.createOrder(any())).thenReturn(order);

        var json = """
                {
                  "customerId": "%s",
                  "items": [
                    { "productId": "%s", "quantity": 2, "unitPrice": 10.00 }
                  ]
                }
                """.formatted(UUID.randomUUID(), UUID.randomUUID());

        mvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.totalAmount").value(20.00));
    }

    @Test
    void create_validation_error_should_return_400() throws Exception {
        // items boş → validasyon hatası
        var json = """
                { "customerId": "%s", "items": [] }
                """.formatted(UUID.randomUUID());

        mvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void get_not_found_should_return_404() throws Exception {
        Mockito.when(orderService.getOrder(any()))
                .thenThrow(new NotFoundException("Sipariş bulunamadı"));

        mvc.perform(get("/api/v1/orders/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}
