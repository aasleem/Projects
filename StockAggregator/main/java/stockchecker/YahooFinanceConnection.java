package stockchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class YahooFinanceConnection implements StockPriceFetcher {

    public int getPrice(String tickerName) {
        try {
            BufferedReader connectedBuffer = getConnectionToYahoo(tickerName);
            connectedBuffer.readLine();

            String[] allStockInfo = connectedBuffer.readLine().split(",");
            double stockValue = Double.parseDouble(allStockInfo[6]);
            connectedBuffer.close();

            DecimalFormat decForm = new DecimalFormat("##.##");
            return (int) (Double.parseDouble(decForm.format(stockValue)) * 100);
        } catch (IOException ex){
            throw new RuntimeException("Network Error");
        }

    }

   protected BufferedReader getConnectionToYahoo(String tickerName) throws IOException{
       URL yahooFinnanceURL  = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + tickerName);
       HttpURLConnection connection = (HttpURLConnection) yahooFinnanceURL.openConnection();
       connection.connect();

       try {
           return new BufferedReader(new InputStreamReader(connection.getInputStream()));
       } catch (IOException ex){
           throw new IllegalArgumentException("Invalid Stock");
       }
   }
}