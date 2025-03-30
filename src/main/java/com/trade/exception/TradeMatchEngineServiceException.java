package com.trade.exception;

public class TradeMatchEngineServiceException extends TradeMatchEngineApplicationException{
    public TradeMatchEngineServiceException(String message) {
        super(message);
    }

    public TradeMatchEngineServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}