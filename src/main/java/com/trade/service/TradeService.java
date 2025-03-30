package com.trade.service;

import com.trade.exception.TradeMatchEngineServiceException;
import com.trade.model.FinancialInstrument;
import com.trade.model.Trade;

import java.util.List;

public interface TradeService {

    /**
     * Adds a new financial instrument to the system
     * @param financialInstrument the instrument that is to be added
     * @throws TradeMatchEngineServiceException throws exception if adding of the instrument fails
     */
    void addFinancialInstrument(FinancialInstrument financialInstrument) throws TradeMatchEngineServiceException;

    /**
     * Returns all the processed trades across all instruments
     * @return List<Trade> a list of trades
     */
    List<Trade> getAllTrades();

    /**
     * Returns a all trades processed by an instrument
     * @param instrumentId id of the instrument for which all trades need to be seen
     * @return List<Trade> a list of trades
     */
    List<Trade> getTradeByInstrument(String instrumentId);
}
