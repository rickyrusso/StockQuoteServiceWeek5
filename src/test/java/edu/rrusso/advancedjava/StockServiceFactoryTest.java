package edu.rrusso.advancedjava;

import edu.rrusso.advancedjava.services.DatabaseStockService;
import edu.rrusso.advancedjava.services.StockService;
import edu.rrusso.advancedjava.services.StockServiceFactory;
import org.junit.Test;

import static org.mockito.Matchers.any;

public class StockServiceFactoryTest {
    private StockServiceFactory stockTicker;

    //  test that no exception is thrown
    @Test
    public void getStockService() {
        StockServiceFactory stockServiceFactory = new StockServiceFactory(new DatabaseStockService());
        StockService stockService = stockServiceFactory.getStockService();
        if(stockService == null){
            throw new NullPointerException();
        }
    }


    //  test that a NullPointerException is thrown
    @Test(expected = NullPointerException.class)
    public void getNullStockService() {
        StockServiceFactory stockServiceFactory = new StockServiceFactory(null);
        StockService stockService = stockServiceFactory.getStockService();
        if(stockService == null){
            throw new NullPointerException();
        }
    }

}
