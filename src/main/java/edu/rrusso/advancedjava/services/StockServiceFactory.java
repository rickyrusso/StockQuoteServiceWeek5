package edu.rrusso.advancedjava.services;


import edu.rrusso.advancedjava.services.StockService;

import javax.validation.constraints.NotNull;

/**
 * This class looks up and returns a StockQuote
 */
public class StockServiceFactory {
    private StockService stockService;

    /**
     * Create a new <CODE>StockTicker</CODE> instance
     *
     * @param stockService implementation used to get actual stock data
     */
    public StockServiceFactory(@NotNull StockService stockService){
        this.stockService = stockService;
    }

    /**
     * Gets the <CODE>StockService</CODE> implementation
     */
    public StockService getStockService(){
        return stockService;
    }
}
