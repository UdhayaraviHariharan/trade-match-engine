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


    /**
     *  API to create an order and match with a trade if matches
     * @param orderId orderId of the order to be created
     * @param traderId traderId of the order to be created
     * @param orderType orderType of the order to be created
     * @param instrumentId instrumentId of the order to be created
     * @param price price of the order to be created
     * @param quantity quantity of the order to be created
     * @return ApiResponse with success or failure
     * @throws TradeMatchEngineApplicationException throws exception if any error in creation of financial instrument
     */
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

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@RequestParam("orderId") String orderId, @RequestParam("instrumentId") String instrumentId) throws TradeMatchEngineApplicationException {
        try{
            orderService.cancelOrder(orderId, instrumentId);

            return new ResponseEntity<>(new ApiResponse("Order cancelled successfully."), HttpStatus.CREATED);
        }
        catch (TradeMatchEngineServiceException e){
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            String errorMessage = format("Exception [%s] occurred while cancelling order", e.getMessage());
            return  new ResponseEntity<>(new ApiResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
