package com.trade.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Order implements Comparable<Order>{
    private static final AtomicInteger orderIdGenerator = new AtomicInteger(0);
    private final int orderId;
    private final String traderId;
    private final OrderType orderType;
    private final String instrumentId;
    private final double price;
    //This is not set to final because we can alter the quantity of an order based on match up between buy and sell orders.
    private long quantity;

    public Order(String traderId, String instrumentId, OrderType type, Double price, long quantity) {
        //EdgeCase 1
        if(price <= 0 || quantity <= 0) {
            throw new IllegalArgumentException("Price and Quantity Must be positive");
        }
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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(Order otherOrder) {
        if(this.getOrderType().equals(OrderType.BUY)){
            return Double.compare(otherOrder.getPrice(), this.getPrice());
        }
        else {
            return Double.compare(this.getPrice(), otherOrder.getPrice());
        }
    }
}
