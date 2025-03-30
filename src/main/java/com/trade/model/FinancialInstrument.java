package com.trade.model;

public class FinancialInstrument {
    private final String instrumentId;
    private final String symbol;
    //This is not set to final because we can alter the market price of a financial instrument
    private double marketPrice;

    public FinancialInstrument(String instrumentId, String symbol) {
        this.instrumentId = instrumentId;
        this.symbol = symbol;
        this.marketPrice = 0.0;
    }

    public FinancialInstrument(String instrumentId, String symbol, double marketPrice) {
        this.instrumentId = instrumentId;
        this.symbol = symbol;
        this.marketPrice = marketPrice;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

}
