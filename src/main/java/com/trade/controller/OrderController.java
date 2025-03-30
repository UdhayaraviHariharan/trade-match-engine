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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

import static java.lang.String.format;

@RestController
@RequestMapping("/trade-engine/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping()
    public ResponseEntity<ApiResponse> createOrder(@RequestParam("orderId") String orderId, @RequestParam("traderId") String traderId,
                                                   @RequestParam("orderType") OrderType orderType, @RequestParam("instrumentId") String instrumentId,
                                                   @RequestParam("price") String price, @RequestParam("quantity") String quantity) throws TradeMatchEngineApplicationException {
        try{
            Order order = new Order(orderId, traderId, instrumentId, orderType, Double.parseDouble(price), Integer.parseInt(quantity));

            orderService.addOrder(order);

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
