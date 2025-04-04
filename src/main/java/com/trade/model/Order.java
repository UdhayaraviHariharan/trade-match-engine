package com.trade.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private static final AtomicInteger orderIdGenerator = new AtomicInteger(0);
    private final int orderId;
    private final String traderId;
    private final OrderType orderType;
    private final String instrumentId;
    private final double price;
    //This is not set to final because we can alter the quantity of an order based on match up between buy and sell orders.
    private int quantity;

    public Order(String traderId, String instrumentId, OrderType type, Double price, int quantity) {
        this.orderId = orderIdGenerator.incrementAndGet();
        this.traderId = traderId;
        this.instrumentId = instrumentId;
        this.orderType = type;
        this.price = price;
        this.quantity = quantity;
    }

    public int getOrderId() {
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
