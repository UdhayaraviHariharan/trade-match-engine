package com.trade.controller;

import com.trade.exception.TradeMatchEngineApplicationException;
import com.trade.exception.TradeMatchEngineServiceException;
import com.trade.model.FinancialInstrument;
import com.trade.model.api.ApiResponse;
import com.trade.service.TradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;


@RestController
@RequestMapping("/trade-engine/trade")
public class TradeMatchEngineController {

    private final TradeService tradeService;

    public TradeMatchEngineController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    /**
     *  Api to create financial instrument
     * @param instrumentId instrumentId for the financial instrument
     * @param symbol symbol  for the financial instrument
     * @param price price  for the financial instrument
     * @return ApiResponse with success or failure
     * @throws TradeMatchEngineApplicationException throws exception if any error in creation of financial instrument
     */

    @PostMapping("/instrument")
    public ResponseEntity<ApiResponse> createFinancialInstrument(@RequestParam("instrumentId") String instrumentId, @RequestParam("symbol") String symbol, @RequestParam("price") double price ) throws TradeMatchEngineApplicationException {
        try{
            FinancialInstrument financialInstrument = new FinancialInstrument(instrumentId,symbol, price);
            tradeService.addFinancialInstrument(financialInstrument);
            return new ResponseEntity<>(new ApiResponse("Instrument created successfully."), HttpStatus.CREATED);

        }
        catch (TradeMatchEngineServiceException e){
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            String errorMessage = format("Exception [%s] occurred while adding financialInstrument", e.getMessage());
            return new ResponseEntity<>(new ApiResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
