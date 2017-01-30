package stockchecker.ui;

import stockchecker.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.stream.Collectors;

public class StockCheckerDriver {
    
    public static void main(String args[]) {
        
        StockChecker stockChecker = new StockChecker(new YahooFinanceConnection());
        
        Map<String, Integer> stockInfo = readFile();
        Map<String, Object> retrievedInfo = stockChecker.computeTotalAssetValue(stockInfo);
        
        printResult(retrievedInfo);
    }
    
    private static Map<String, Integer> readFile(){
        Map<String, Integer> stockInfo;
        Path filePath = Paths.get(System.getProperty("user.dir") + "/inputStockInfo.txt");
        try {
            stockInfo = Files.lines(filePath).map(readLines -> {
                if(readLines.matches("^\\w+\\s+\\w+"))
                    return readLines;
                return "";
            }).collect(Collectors.toMap(tickerName -> tickerName.split("\\s+")[0], value -> Integer.parseInt(value.split("\\s+")[1])));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            stockInfo = null;
        }
        return stockInfo;
    }
    
    private static void printResult(Map<String, Object> resultMap){
        for(String key : resultMap.keySet())
            System.out.printf("%s: %s\n", key, resultMap.get(key).toString());
    }
}