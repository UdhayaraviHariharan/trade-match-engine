package com.trade.model;

import java.util.Date;

public class Trade {
    private final int buyOrderId;
    private final int sellOrderId;
    private final double price;
    private final int quantity;
    private final String instrumentId;
    private String timestamp ;


    public Trade(int buyOrderId, int sellOrderId, double price, int quantity, String instrumentId) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.price = price;
        this.quantity = quantity;
        this.instrumentId = instrumentId;
        this.timestamp = new Date().toString();
    }

    public String getInstrumentId() {
        return instrumentId;
    }
    public int getBuyOrderId() {
        return buyOrderId;
    }
    public int getSellOrderId() {
        return sellOrderId;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getTimestamp() {
        return timestamp;
    }
}
