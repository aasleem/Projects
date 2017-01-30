package stockchecker;

import java.util.*;

public class StockChecker {

    private StockPriceFetcher theFetcher;

    public StockChecker(StockPriceFetcher fetcher){
        theFetcher = fetcher;
    }

    int getStockPrice(String tickerName) {
        return theFetcher.getPrice(tickerName);
    }

    public int computeNetAssetValueInCents(int numOfShares, int stockPriceInCents) {
        return numOfShares * stockPriceInCents;
    }

    public Map<String, Object>  computeTotalAssetValue(Map<String, Integer> stockMap){
        Map<String, Object>  totalValueMap = new LinkedHashMap<>();
        List<String> invalidStockList = new ArrayList<>();
        List<String> networkErrorList = new ArrayList<>();
        int currentAssetVal, totalValue = 0;

        for(String stockName : stockMap.keySet()) {
            try {
                currentAssetVal = computeNetAssetValueInCents(stockMap.get(stockName), getStockPrice(stockName));
                totalValue += currentAssetVal;

                totalValueMap.put(stockName, currentAssetVal);
            } catch(IllegalArgumentException ex) {
                invalidStockList.add(stockName);
            } catch (RuntimeException ex){
                networkErrorList.add(stockName);
            }
        }

        if(invalidStockList.size() > 0)
            totalValueMap.put("Invalid Stock", invalidStockList);
        if(networkErrorList.size() > 0)
            totalValueMap.put("Network Error", networkErrorList);

        totalValueMap.put("TotalNetAssetValue", totalValue);

        return totalValueMap;
    }
}