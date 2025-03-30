package com.trade.model;

public class Order {
    private final String orderId;
    private final String traderId;
    private final OrderType orderType;
    private final String instrumentId;
    private final double price;
    //This is not set to final because we can alter the quantity of an order based on match up between buy and sell orders.
    private int quantity;

    public Order(String orderId, String traderId, String instrumentId, OrderType type, Double price, int quantity) {
        this.orderId = orderId;
        this.traderId = traderId;
        this.instrumentId = instrumentId;
        this.orderType = type;
        this.price = price;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTraderId() {
        return traderId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
