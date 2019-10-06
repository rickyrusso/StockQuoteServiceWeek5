package edu.rrusso.advancedjava.services;


import edu.rrusso.advancedjava.model.StockQuery;
import edu.rrusso.advancedjava.model.StockQuote;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface StockService {
    /**
     * Return the current price for a share of stock for the given symbol
     *
     * @param symbol the stock symbol of the company you want a quote for e.g. APPL for APPLE
     * @return a <CODE>StockQuote</CODE> instance
     */
    StockQuote getQuote(@NotNull String symbol) throws StockServiceException;
    /**
     * Get a historical list of stock quotes for the provide symbol
     * This method will return one StockQuote per 24 hour period.
     * If you wish more or less StockQuotes returned you can specify the
     * Interval yourself using the
     *
     * @param stockQuery the stockQuery object with values to search for
     * @return a list of StockQuote instances. One for each day in the range specified.
     */
    List<StockQuote> getQuote(StockQuery stockQuery) throws StockServiceException;
}