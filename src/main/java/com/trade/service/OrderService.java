package com.trade.service;

import com.trade.exception.TradeMatchEngineApplicationException;
import com.trade.model.Order;
import com.trade.model.OrderType;

import java.util.List;

public interface OrderService {

    /**
     * Adds a new BUY or SELL order to the trade engine
     * @param order the buy/sell order received from the user via controller
     * @throws TradeMatchEngineApplicationException throws exception if order creation failed
     */
    void addOrder(Order order) throws TradeMatchEngineApplicationException;

    /**
     * Cancels an order in the system
     * @param orderId id of the order to be removed
     * @param instrumentId instrumentId from which the order needs to be removed
     * @throws TradeMatchEngineApplicationException  throws exception if order cancellation failed
     */
    void cancelOrder(int orderId, String instrumentId) throws TradeMatchEngineApplicationException;

    /**
     * Gets all orders by an order type
     * @param orderType Order type BUY/SELL for which orders are needed
     * @return List<Order> all orders for a given order type
     * @throws TradeMatchEngineApplicationException throws exception if any failure in fetching orders
     *
     */
    List<Order> getOrders(OrderType orderType) throws TradeMatchEngineApplicationException;
}
