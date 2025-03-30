package com.trade.controller;
import com.trade.exception.TradeMatchEngineServiceException;
import com.trade.model.Order;
import com.trade.model.OrderType;
import com.trade.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class OrderControllerTest {
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testCreateOrder_Success() throws Exception {
        String orderId = "12345";
        String traderId = "trader1";
        OrderType orderType = OrderType.BUY;
        String instrumentId = "AAPL";
        String price = "150.00";
        String quantity = "10";


        doNothing().when(orderService).addOrder(any(Order.class));


        mockMvc.perform(post("/trade-engine/order")
                        .param("orderId", orderId)
                        .param("traderId", traderId)
                        .param("orderType", orderType.toString())
                        .param("instrumentId", instrumentId)
                        .param("price", price)
                        .param("quantity", quantity))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Order created successfully."));
    }

    @Test
    public void testCreateOrder_BadRequest() throws Exception {
        // Similar setup as before
        String orderId = "12345";
        String traderId = "trader1";
        OrderType orderType = OrderType.BUY;
        String instrumentId = "AAPL";
        String price = "150.00";
        String quantity = "10";

        doThrow(new TradeMatchEngineServiceException("Invalid order details."))
                .when(orderService).addOrder(any(Order.class));

        mockMvc.perform(post("/trade-engine/order")
                        .param("orderId", orderId)
                        .param("traderId", traderId)
                        .param("orderType", orderType.toString())
                        .param("instrumentId", instrumentId)
                        .param("price", price)
                        .param("quantity", quantity))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Invalid order details."));
    }


}
