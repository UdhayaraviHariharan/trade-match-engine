package com.trade.exception;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradeMatchEngineApplicationExceptionTest {

    @Test
    public void testTradeMatchEngineApplicationException()  {
        TradeMatchEngineApplicationException exception = new TradeMatchEngineApplicationException("TradeMatchEngineApplicationException");

        assertEquals("TradeMatchEngineApplicationException", exception.getMessage());

        Exception cause = new Exception("Trade Failed Exception");
        exception = new TradeMatchEngineApplicationException("TradeMatchEngineApplicationException", cause);

        assertEquals("TradeMatchEngineApplicationException", exception.getMessage());
        assertEquals(exception.getCause(), cause);
    }
}
