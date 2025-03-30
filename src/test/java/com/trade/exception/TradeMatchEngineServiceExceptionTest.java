package com.trade.exception;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradeMatchEngineServiceExceptionTest {
    @Test
    public void testTradeMatchEngineServiceExceptionTest() {
        TradeMatchEngineServiceException exception = new TradeMatchEngineServiceException("TradeMatchEngineServiceException");
        assertEquals("TradeMatchEngineServiceException", exception.getMessage());
    }

    @Test
    public void testTradeMatchEngineServiceExceptionWithCauseTest(){
        Exception cause = new RuntimeException("Service Exception");
        TradeMatchEngineServiceException exception = new TradeMatchEngineServiceException("ServiceFailed", cause);
        assertEquals("ServiceFailed", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
