package com.trade.controller;

import com.trade.exception.TradeMatchEngineServiceException;
import com.trade.model.FinancialInstrument;
import com.trade.model.Order;
import com.trade.service.OrderService;
import com.trade.service.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TradeMatchEngineControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TradeService tradeService;

    @InjectMocks
    private TradeMatchEngineController tradeMatchEngineController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tradeMatchEngineController).build();
    }

    @Test
    public void testCreateFinancialInstrument_Success() throws Exception {
        String instrumentId = "AAPL";
        String symbol = "Apple Inc.";
        double price = 150.00;

        doNothing().when(tradeService).addFinancialInstrument(ArgumentMatchers.any(FinancialInstrument.class));


        mockMvc.perform(post("/trade-engine/trade/instrument")
                        .param("instrumentId", instrumentId)
                        .param("symbol", symbol)
                        .param("price", String.valueOf(price)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Instrument created successfully."));
    }

    @Test
    public void testCreateFinancialInstrument_BadRequest() throws Exception {
        String instrumentId = "AAPL";
        String symbol = "Apple Inc.";
        double price = 150.00;


        doThrow(new TradeMatchEngineServiceException("Invalid instrument details."))
                .when(tradeService).addFinancialInstrument(ArgumentMatchers.any(FinancialInstrument.class));



        mockMvc.perform(post("/trade-engine/trade/instrument")
                        .param("instrumentId", instrumentId)
                        .param("symbol", symbol)
                        .param("price", String.valueOf(price)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value("Invalid instrument details."));
    }

}
