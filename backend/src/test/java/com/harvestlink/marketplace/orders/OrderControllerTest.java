package com.harvestlink.marketplace.orders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.harvestlink.marketplace.config.SecurityConfiguration;
import com.harvestlink.marketplace.auth.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
@Import(SecurityConfiguration.class)
class OrderControllerTest {

    @MockBean
    private UserAccountRepository userAccountRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void orderEndpointCreatesOrder() throws Exception {
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "buyerName": "Tesfaye",
                                  "buyerEmail": "tesfaye@example.com",
                                  "shippingAddress": "Addis Ababa, Ethiopia",
                                  "notes": "Handle carefully",
                                  "items": [
                                    {
                                      "productId": 1,
                                      "name": "Teff",
                                      "slug": "teff",
                                      "unit": "kg",
                                      "price": 4200,
                                      "currency": "ETB",
                                      "quantity": 2,
                                      "sellerName": "Aster Cooperative"
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buyerName").value("Tesfaye"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.paymentStatus").value("PENDING"))
                .andExpect(jsonPath("$.totalAmount").value(8400));
    }

    @Test
    void orderStatusEndpointUpdatesStatus() throws Exception {
        Order order = new Order("Tesfaye", "tesfaye@example.com", "Addis Ababa, Ethiopia", "Handle carefully");
        order.setTotalAmount(new java.math.BigDecimal("8400"));
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(patch("/api/v1/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_TRANSIT\",\"paymentStatus\":\"PAID\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_TRANSIT"))
                .andExpect(jsonPath("$.paymentStatus").value("PAID"));
    }
}
