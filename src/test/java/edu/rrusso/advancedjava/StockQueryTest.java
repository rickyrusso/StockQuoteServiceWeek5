package edu.rrusso.advancedjava;

import edu.rrusso.advancedjava.model.StockQuery;
import edu.rrusso.advancedjava.model.StockQuote;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class StockQueryTest {
    private StockQuery stockQuery;

    @Before
    public void setUp() throws ParseException {
        stockQuery = new StockQuery("GOOG", "08/15/2019", "08/16/2019");
    }

    @Test
    public void getStockQuoteValues() {
        assertEquals("Test for correct symbol", "GOOG", stockQuery.getSymbol());

        Calendar fromDate = new GregorianCalendar(2019, 7, 15);
        assertEquals("test for correct from date", fromDate, stockQuery.getFrom());

        Calendar untilDate = new GregorianCalendar(2019, 7, 16);
        assertEquals("test for correct until date", untilDate, stockQuery.getUntil());
    }
}
