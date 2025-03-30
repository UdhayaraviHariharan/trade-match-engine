package com.trade.controller;

import com.trade.exception.TradeMatchEngineApplicationException;
import com.trade.exception.TradeMatchEngineServiceException;
import com.trade.model.FinancialInstrument;
import com.trade.model.Order;
import com.trade.model.OrderType;
import com.trade.model.Trade;
import com.trade.model.api.ApiResponse;
import com.trade.service.OrderService;
import com.trade.service.TradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * Test controller for interview review sake.
 */
@RestController
@RequestMapping("/trade-engine/test")
public class TestController {
    private final Logger logger = Logger.getLogger(TestController.class.getName());

    private final OrderService orderService;
    private final TradeService tradeService;


    public TestController(OrderService orderService, TradeService tradeService) {
        this.orderService = orderService;
        this.tradeService = tradeService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> createOrder() throws TradeMatchEngineApplicationException {
        try{
            logger.info("Adding financial Instrument with Inst ID = INST001 & Symbol : VEGA & MarkerPrice : 100");
            FinancialInstrument financialInstrument = new FinancialInstrument("INST001", "VEGA", 100.0);
            tradeService.addFinancialInstrument(financialInstrument);

            logger.info("FinancialInstrument added Successfully");

            logger.info("\n Adding Buy Order 1 - Type : BUY , Price : 150, Quantity : 10");
            Order buyOrder1 = new Order("orderId1", "traderId1", financialInstrument.getInstrumentId(), OrderType.BUY, 150.0, 10);
            orderService.addOrder(buyOrder1);

            logger.info("\n Adding Buy Order 2 - Type : BUY , Price : 170, Quantity : 10");
            Order buyOrder2 = new Order("orderId2", "traderId1", financialInstrument.getInstrumentId(), OrderType.BUY, 170.0, 10);
            orderService.addOrder(buyOrder2);

            logger.info("\n Adding Buy Order 3 - Type : BUY , Price : 130, Quantity : 10");
            Order buyOrder3 = new Order("orderId3", "traderId1", financialInstrument.getInstrumentId(), OrderType.BUY, 130.0, 10);
            orderService.addOrder(buyOrder3);

            logger.info("\n Adding SELL Order 1- Type : SELL , Price : 120, Quantity : 10");
            Order sellOrder1 = new Order("orderId4", "traderId1",  financialInstrument.getInstrumentId(), OrderType.SELL, 120.0, 10);
            orderService.addOrder(sellOrder1);

            logger.info("Trade has Executed Successfully");
            List<Trade> trades = tradeService.getTradeByInstrument(financialInstrument.getInstrumentId());
            logger.info("Trade 1 \n Instrument ID: " + trades.get(0).getInstrumentId() + "\n Quantity: " + trades.get(0).getQuantity() +  "\n Price : " + trades.get(0).getPrice());


            logger.info("\n Adding SELL Order 1- Type : SELL , Price : 150, Quantity : 10");
            Order sellOrder2= new Order("orderId5", "traderId1",  financialInstrument.getInstrumentId(), OrderType.SELL, 150.0, 20);
            orderService.addOrder(sellOrder2);

            trades = tradeService.getTradeByInstrument(financialInstrument.getInstrumentId());
            logger.info("Trade 2 \n Instrument ID: " + trades.get(1).getInstrumentId() + "\n Quantity: " + trades.get(1).getQuantity() +  "\n Price : " + trades.get(1).getPrice());

            return new ResponseEntity<>(new ApiResponse("Order created successfully."), HttpStatus.CREATED);

        }
        catch (TradeMatchEngineServiceException e){
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            String errorMessage = format("Exception [%s] occurred while adding order", e.getMessage());
            return  new ResponseEntity<>(new ApiResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
