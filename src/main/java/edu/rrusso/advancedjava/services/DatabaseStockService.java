package edu.rrusso.advancedjava.services;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import edu.rrusso.advancedjava.model.StockQuery;
import edu.rrusso.advancedjava.model.StockQuote;
import edu.rrusso.advancedjava.util.DatabaseConnectionException;
import edu.rrusso.advancedjava.util.DatabaseUtils;

/**
 * An implementation of the StockService interface that gets
 * stock data from a database.
 */
public class DatabaseStockService implements StockService {

    /**
     * Return the current price for a share of stock  for the given symbol
     *
     * @param symbol the stock symbol of the company you want a quote for.
     *               e.g. APPL for APPLE
     * @return a  <CODE>BigDecimal</CODE> instance
     * @throws StockServiceException if using the service generates an exception.
     *                               If this happens, trying the service may work, depending on the actual cause of the
     *                               error.
     */
    @Override
    public StockQuote getQuote(String symbol) throws StockServiceException {
        StockQuote stockQuote = null;

        try {
            Connection connection = DatabaseUtils.getConnection();
            String sql = "select * from quotes where symbol = ? ORDER BY time desc LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, symbol);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String symbolValue = resultSet.getString("symbol");
                Calendar calendarDate = new GregorianCalendar();
                calendarDate.setTime(resultSet.getDate("time"));
                BigDecimal price = resultSet.getBigDecimal("price");
                stockQuote = new StockQuote(symbolValue, price, calendarDate);
            } else {
                throw new StockServiceException("There is no stock data for:" + symbol);
            }

        } catch (DatabaseConnectionException | SQLException exception) {
            throw new StockServiceException(exception.getMessage(), exception);
        }

        return stockQuote;
    }

    /**
     * Get a historical list of stock quotes for the provide symbol
     * This method will return one StockQuote per 24 hour period.
     * If you wish more or less StockQuotes returned you can specify the
     * Interval yourself using the
     *
     * @param stockQuery the stockQuery object with values to search for
     * @return a list of StockQuote instances. One for each day in the range specified.
     */
    @Override
    public List<StockQuote> getQuote(StockQuery stockQuery) throws StockServiceException {
        List<StockQuote> stockQuotes = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseUtils.getConnection();
            String sql = "select * from quotes where Symbol = ? AND time > ? AND time < (? + INTERVAL 1 DAY) ;";
            statement = connection.prepareStatement(sql);
            statement.setString(1, stockQuery.getSymbol());
            java.sql.Date fromSqlDate = new java.sql.Date(stockQuery.getFrom().getTime().getTime());
            java.sql.Date untilSqlDate = new java.sql.Date(stockQuery.getUntil().getTime().getTime());
            statement.setDate(2, fromSqlDate);
            statement.setDate(3, untilSqlDate);

            resultSet = statement.executeQuery();
            stockQuotes = new ArrayList<StockQuote>(resultSet.getFetchSize());
            while(resultSet.next()) {
                String symbolValue = resultSet.getString("symbol");
                Calendar calendarDate = new GregorianCalendar();
                calendarDate.setTime(resultSet.getDate("time"));
                BigDecimal price = resultSet.getBigDecimal("price");
                stockQuotes.add(new StockQuote(symbolValue, price, calendarDate));
            }

        } catch (DatabaseConnectionException | SQLException exception) {
            throw new StockServiceException(exception.getMessage(), exception);
        }
        finally {
            closeDBObjects(connection, statement, resultSet);
        }
        if (stockQuotes.isEmpty()) {
            throw new StockServiceException("There is no stock data for:" + stockQuery);
        }
        return stockQuotes;
    }


    private void closeDBObjects(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (connection != null){
                connection.close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        try {
            if (statement != null){
                statement.close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        try {
            if (resultSet != null){
                resultSet.close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

}
