package com.alperentuncer.payment.saga.order_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * application-test.yml profilini de istersen oluşturup burada aktif edebilirsin.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev") // dev profilindeki H2 ayarlarını kullan
class OrderE2ETest {

    @Autowired
    MockMvc mvc;

    @Test
    void full_flow_post_get_cancel() throws Exception {
        var createJson = """
                {
                  "customerId": "%s",
                  "items": [
                    { "productId": "%s", "quantity": 2, "unitPrice": 15.50 }
                  ]
                }
                """.formatted(UUID.randomUUID(), UUID.randomUUID());

        // create
        var createResult = mvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalAmount").value(31.00))
                .andReturn();

        // id çek
        var body = createResult.getResponse().getContentAsString();
        var id = body.replaceAll(".*\"id\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        // get
        mvc.perform(get("/api/v1/orders/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CREATED"));

        // cancel
        mvc.perform(post("/api/v1/orders/" + id + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
