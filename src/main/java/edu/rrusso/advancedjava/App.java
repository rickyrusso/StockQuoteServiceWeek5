package edu.rrusso.advancedjava;

import edu.rrusso.advancedjava.model.StockQuery;
import edu.rrusso.advancedjava.model.StockQuote;
import edu.rrusso.advancedjava.services.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class App {

    private enum ProgramTerminationStatusEnum {
        // for now, we just have normal or abnormal but could more specific ones as needed.
        NORMAL(0),
        ABNORMAL(-1);

        // when the program exits, this value will be reported to underlying OS
        private int statusCode;

        private ProgramTerminationStatusEnum(int statusCodeValue) {
            this.statusCode = statusCodeValue;
        }

        /**
         * @return The value sent to OS when the program ends.
         */
        private int getStatusCode() {
            return statusCode;
        }
    }


    /**
     * program to retrieve the Stock Quotes in a given date range
     *
     * @param args command line parameters to receive a symbol, from date, until date
     */
    public static void main(String[] args) {
        if (args == null || args.length != 3) {
            exit(ProgramTerminationStatusEnum.ABNORMAL,
                    "Please supply 3 arguments a stock symbol, a start date (MM/DD/YYYY) and end date (MM/DD/YYYY)");
        }

        ProgramTerminationStatusEnum exitStatus = ProgramTerminationStatusEnum.NORMAL;
        String programTerminationMessage;

        try {
            StockServiceFactory stockServiceFactory = new StockServiceFactory(new DatabaseStockService());
            StockService stockService = stockServiceFactory.getStockService();

            List<StockQuote> filteredStockQuotes = stockService.getQuote(new StockQuery(args[0], args[1], args[2]));
            filteredStockQuotes.forEach(System.out::println);
            programTerminationMessage = "Program executed successfully";
        } catch (ParseException e) {
            programTerminationMessage = "Invalid date data: " + e.getMessage();
            exitStatus = ProgramTerminationStatusEnum.ABNORMAL;
        } catch (StockServiceException e) {
            programTerminationMessage = "StockService failed: " + e.getMessage();
            exitStatus = ProgramTerminationStatusEnum.ABNORMAL;
        } catch (Exception e){
            programTerminationMessage = "Unexpected error: " + e.getMessage();
            exitStatus = ProgramTerminationStatusEnum.ABNORMAL;
        }

        exit(exitStatus, programTerminationMessage);
    }


    /**
     * Terminate the application.
     *
     * @param statusCode        an enum value that indicates if the program terminated ok or not.
     * @param diagnosticMessage A message to display to the user when the program ends.
     *                          This should be an error message in the case of abnormal termination
     *                          <p/>
     *                          NOTE: This is an example of DRY in action.
     *                          A program should only have one exit point. This makes it easy to do any clean up
     *                          operations before a program quits from just one place in the code.
     *                          It also makes for a consistent user experience.
     */
    private static void exit(ProgramTerminationStatusEnum statusCode, String diagnosticMessage) {
        if (statusCode == ProgramTerminationStatusEnum.NORMAL) {
            System.out.println(diagnosticMessage);
        } else if (statusCode == ProgramTerminationStatusEnum.ABNORMAL) {
            System.err.println(diagnosticMessage);
        } else {
            throw new IllegalStateException("Unknown ProgramTerminationStatusEnum.");
        }
        System.exit(statusCode.getStatusCode());
    }

}
