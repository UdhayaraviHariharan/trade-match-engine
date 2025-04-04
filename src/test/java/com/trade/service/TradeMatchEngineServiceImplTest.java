package com.trade.service;

import com.trade.exception.TradeMatchEngineApplicationException;
import com.trade.model.FinancialInstrument;
import com.trade.model.Order;
import com.trade.model.OrderType;
import com.trade.service.impl.TradeMatchEngineServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

class TradeMatchEngineServiceImplTest {
    private TradeMatchEngineServiceImpl tradeMatchEngineService;
    private FinancialInstrument financialInstrument;
    private final static String INSTRUMENT_ID = "instrumentid";
    private final static String SYMBOL = "symbol";


    @BeforeEach
    public void setUp() {
        financialInstrument = new FinancialInstrument(INSTRUMENT_ID, SYMBOL);
    }

    @Test
    void testAddOrderAndExecuteTrade_AddsOrdersSuccessfully() throws TradeMatchEngineApplicationException {
        tradeMatchEngineService = new TradeMatchEngineServiceImpl();
        tradeMatchEngineService.addFinancialInstrument(financialInstrument);

        tradeMatchEngineService.addOrder(createOrders(OrderType.BUY));
        tradeMatchEngineService.addOrder(createOrders(OrderType.BUY));

        assertEquals(2, tradeMatchEngineService.getOrders(OrderType.BUY).size());
        assertEquals(0, tradeMatchEngineService.getTradeByInstrument(INSTRUMENT_ID).size());

        tradeMatchEngineService.addOrder(createOrders(OrderType.SELL));
        assertEquals(1, tradeMatchEngineService.getTradeByInstrument(INSTRUMENT_ID).size());

        assertEquals(1, tradeMatchEngineService.getOrders(OrderType.BUY).size());
        assertEquals(0, tradeMatchEngineService.getOrders(OrderType.SELL).size());

        tradeMatchEngineService.addOrder(createOrders(OrderType.SELL));

        assertEquals(2, tradeMatchEngineService.getTradeByInstrument(INSTRUMENT_ID).size());

        tradeMatchEngineService.addOrder(createOrders(OrderType.SELL));
        assertEquals(1, tradeMatchEngineService.getOrders(OrderType.SELL).size());

        assertEquals(2, tradeMatchEngineService.getTradeByInstrument(INSTRUMENT_ID).size());
    }

    @Test
    void testCancelOrder_RemovesBuyOrderSuccessfully() throws TradeMatchEngineApplicationException {
        tradeMatchEngineService = new TradeMatchEngineServiceImpl();
        tradeMatchEngineService.addFinancialInstrument(financialInstrument);
        tradeMatchEngineService.addOrder(createOrders(OrderType.BUY));
        tradeMatchEngineService.addOrder(createOrders(OrderType.BUY));
        tradeMatchEngineService.addOrder(createOrders(OrderType.BUY));

        Order order = createOrders(OrderType.BUY);
        tradeMatchEngineService.addOrder(order);

        tradeMatchEngineService.cancelOrder(order.getOrderId(), order.getInstrumentId());

        List<Order> orders = tradeMatchEngineService.getOrders(OrderType.BUY);

        assertEquals(3, orders.size());
        assertFalse(orders.contains(order));
    }

    @Test
    void testCancelOrder_RemovesSellOrderSuccessfully() throws TradeMatchEngineApplicationException {
        tradeMatchEngineService = new TradeMatchEngineServiceImpl();
        tradeMatchEngineService.addFinancialInstrument(financialInstrument);
        tradeMatchEngineService.addOrder(createOrders(OrderType.SELL));
        tradeMatchEngineService.addOrder(createOrders(OrderType.SELL));
        tradeMatchEngineService.addOrder(createOrders(OrderType.SELL));

        Order order = createOrders(OrderType.SELL);
        tradeMatchEngineService.addOrder(order);

        tradeMatchEngineService.cancelOrder(order.getOrderId(), order.getInstrumentId());

        List<Order> orders = tradeMatchEngineService.getOrders(OrderType.SELL);

        assertEquals(3, orders.size());
        assertFalse(orders.contains(order));
    }

    @Test
    void testAddOrder_ThrowsExceptionWhenFinancialInstrumentDoesNotExists() throws TradeMatchEngineApplicationException {
        tradeMatchEngineService = new TradeMatchEngineServiceImpl();

        TradeMatchEngineApplicationException  exception = null;
        try{
            tradeMatchEngineService.addOrder(new Order( "traderId", "insId", OrderType.BUY, Math.random(), 22));
        }
        catch(TradeMatchEngineApplicationException e){
            exception = e;
        }
        assertNotNull(exception);
        assertEquals("Financial instrument not found", exception.getMessage());

    }


    private Order createOrders(OrderType orderType)  {
        int randomNumber = (int) (Math.random() * 100);
        return new Order("traderId"+randomNumber, INSTRUMENT_ID, orderType, 100.0, 10);
    }
}

