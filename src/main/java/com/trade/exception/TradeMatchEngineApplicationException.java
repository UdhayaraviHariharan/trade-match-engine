package com.trade.exception;

public class TradeMatchEngineApplicationException extends Exception {
    public TradeMatchEngineApplicationException(String message) {
        super(message);
    }

    public TradeMatchEngineApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
