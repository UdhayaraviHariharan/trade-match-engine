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
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class TradeMatchEngineServiceImpl implements OrderService, TradeService {
    private Map<String, FinancialInstrument> financialInstruments = new HashMap<>();
    private List<Trade> processedTrades = new ArrayList<>();
    private PriorityQueue<Order> buyOrders = new PriorityQueue<>((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
    private PriorityQueue<Order> sellOrders = new PriorityQueue<>(Comparator.comparingDouble(Order::getPrice));


    /**
     * @see OrderService#addOrder(Order)
     */
    @Override
    public void addOrder(Order order) throws TradeMatchEngineApplicationException {
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
            String errorMessage = format("Exception [%s] occured while adding order [%s]", e.getMessage(), order);
            throw new TradeMatchEngineApplicationException(errorMessage);
        }
    }

    private void executeTrade(String instrumentId) {
        FinancialInstrument financialInstrument = financialInstruments.get(instrumentId);

        while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
            Order bestBuy = buyOrders.peek();
            Order bestSell = sellOrders.peek();

            if (bestBuy.getPrice() >= bestSell.getPrice()) {
                double tradePrice = bestSell.getPrice();
                int tradeQuantity = Math.min(bestBuy.getQuantity(), bestSell.getQuantity());

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

    private void updateInstrumentMarketPrice(FinancialInstrument financialInstrument, PriorityQueue<Order> buyOrders, PriorityQueue<Order> sellOrders) {
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
    public void cancelOrder(int orderId, String instrumentId) throws TradeMatchEngineApplicationException {
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
