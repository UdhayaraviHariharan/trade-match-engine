package com.trade.service.impl;

import com.trade.exception.TradeMatchEngineApplicationException;
import com.trade.exception.TradeMatchEngineServiceException;
import com.trade.model.FinancialInstrument;
import com.trade.model.Order;
import com.trade.model.OrderType;
import com.trade.model.Trade;
import com.trade.service.OrderService;
import com.trade.service.TradeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class TradeMatchEngineServiceImpl implements OrderService, TradeService {
    private final Map<String, FinancialInstrument> financialInstruments = new ConcurrentHashMap<>();
    private final PriorityBlockingQueue<Order> buyOrders = new PriorityBlockingQueue<>();
    private final PriorityBlockingQueue<Order> sellOrders = new PriorityBlockingQueue<>();
    private final List<Trade> processedTrades = new ArrayList<>();

    // For simplicity purposes, defaulted to a static value. We can have this set when creating a trade match engine
    private static final int ORDER_BOOK_LIMIT = 100;

    /**
     * @see OrderService#addOrder(Order)
     */
    @Override
    public synchronized void addOrder(Order order) throws TradeMatchEngineApplicationException {
        // To help prevent orders overflow
        if(buyOrders.size() + sellOrders.size() >= ORDER_BOOK_LIMIT) {
            throw new TradeMatchEngineApplicationException("Order Limit Exceeded");
        }
        try {
            if (!financialInstruments.containsKey(order.getInstrumentId())) {
                // Assumption
                // that we need to throw exception if financial instrument doesnt exist.
                // Alternate : Can create a new financial instruction if it doesnt exist
                throw new TradeMatchEngineServiceException("Financial instrument not found");
            }

            if (order.getOrderType().equals(OrderType.BUY)) {
                buyOrders.add(order);
            } else {
                sellOrders.add(order);
            }

            this.executeTrade(order.getInstrumentId());

        } catch (TradeMatchEngineServiceException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = format("Exception [%s] occurred while adding order [%s]", e.getMessage(), order);
            throw new TradeMatchEngineApplicationException(errorMessage);
        }
    }

    private synchronized void executeTrade(String instrumentId) {
        FinancialInstrument financialInstrument = financialInstruments.get(instrumentId);

        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            Order bestBuy = buyOrders.peek();
            Order bestSell = sellOrders.peek();

            if (bestBuy.getPrice() >= bestSell.getPrice()) {
                double tradePrice = bestSell.getPrice();
                // This can solve the case where the order quantities dont match
                // so we can do partial order filling and
                long tradeQuantity = Math.min(bestBuy.getQuantity(), bestSell.getQuantity());

                Trade trade = new Trade(bestBuy.getOrderId(), bestSell.getOrderId(), tradePrice, tradeQuantity, instrumentId);
                processedTrades.add(trade);

                bestBuy.setQuantity(bestBuy.getQuantity() - tradeQuantity);
                bestSell.setQuantity(bestSell.getQuantity() - tradeQuantity);

                if (bestBuy.getQuantity() == 0) {
                    buyOrders.poll();
                }
                if (bestSell.getQuantity() == 0) {
                    sellOrders.poll();
                }

                this.updateInstrumentMarketPrice(financialInstrument, buyOrders, sellOrders);

            } else {
                break;
            }
        }
    }

    private void updateInstrumentMarketPrice(FinancialInstrument financialInstrument, PriorityBlockingQueue<Order> buyOrders, PriorityBlockingQueue<Order> sellOrders) {
        if (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            financialInstrument.setMarketPrice((buyOrders.peek().getPrice() + sellOrders.peek().getPrice()) / 2.0);
        } else if (!buyOrders.isEmpty()) {
            financialInstrument.setMarketPrice(buyOrders.peek().getPrice());
        } else if (!sellOrders.isEmpty()) {
            financialInstrument.setMarketPrice(sellOrders.peek().getPrice());
        } else {
            financialInstrument.setMarketPrice(0);
        }
    }


    @Override
    public synchronized void cancelOrder(int orderId, String instrumentId) throws TradeMatchEngineApplicationException {
        try {
            buyOrders.removeIf(order -> order.getOrderId() == orderId && order.getInstrumentId().equals(instrumentId));
            sellOrders.removeIf(order -> order.getOrderId() == orderId && order.getInstrumentId().equals(instrumentId));
        } catch (Exception e) {
            String errorMessage = format("Exception [%s] occurred while canceling order [%s]", e.getMessage(), orderId);
            throw new TradeMatchEngineApplicationException(errorMessage);
        }

    }

    @Override
    public List<Order> getOrders(OrderType orderType) throws TradeMatchEngineApplicationException {
        return orderType.equals(OrderType.BUY) ? new ArrayList<>(this.buyOrders) : new ArrayList<>(this.sellOrders) ;
    }

    @Override
    public void addFinancialInstrument(FinancialInstrument financialInstrument) throws TradeMatchEngineServiceException {
        if(financialInstruments.containsKey(financialInstrument.getInstrumentId())) {
            String errorMessage = format("Instrument already exists [%s]", financialInstrument.getInstrumentId());
            throw new TradeMatchEngineServiceException(errorMessage);
        }
        financialInstruments.put(financialInstrument.getInstrumentId(), financialInstrument);
    }

    @Override
    public List<Trade> getAllTrades() {
        return processedTrades;
    }

    @Override
    public List<Trade> getTradeByInstrument(String instrumentId) {
        if (processedTrades == null || instrumentId == null) {
            return new ArrayList<>(); // Or throw an exception
        }

        return processedTrades.stream()
                .filter(trade -> trade.getInstrumentId().equals(instrumentId))
                .collect(Collectors.toList());
    }
}
