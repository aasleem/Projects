package stockchecker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.*;

public class StockCheckerTest {
    final double delta = 0.001;
    StockChecker stockChecker;
    StockPriceFetcher priceFetcher;
    Map<String, Integer> tickersAndValue;

    @Before
    public void setUp() throws Exception {
        priceFetcher = Mockito.mock(StockPriceFetcher.class);
        stockChecker = new StockChecker(priceFetcher);
        tickersAndValue = new HashMap<>();
    }

    @Test
    public void computeValueOf10SharesWorth100(){
        assertEquals(100000, stockChecker.computeNetAssetValueInCents(10, 10000), delta);
    }

    @Test
    public void computeValueOf0SharesWorth100(){
        assertEquals(0, stockChecker.computeNetAssetValueInCents(0, 10000), delta);
    }

    @Test
    public void computeValueOf10SharesWorth500(){
        assertEquals(500230, stockChecker.computeNetAssetValueInCents(10, 50023), delta);
    }

    @Test
    public void getPriceOfGOOGStock(){
        when(priceFetcher.getPrice("GOOG")).thenReturn(81496);
        assertEquals(81496, stockChecker.getStockPrice("GOOG"));
    }

    @Test
    public void getPriceOfAAPLStock(){
        when(priceFetcher.getPrice("AAPL")).thenReturn(2058);
        assertEquals(2058, stockChecker.getStockPrice("AAPL"));
    }

    @Test
    public void getPriceOfInvalidStock() {
        when(priceFetcher.getPrice("SVN")).thenThrow(new IllegalArgumentException("Invalid Stock"));
        try{
            stockChecker.getStockPrice("SVN");
            fail("Expected exception for an invalid stock");
        }
        catch (IllegalArgumentException ex){
            assertEquals("Invalid Stock", ex.getMessage());
        }
    }

    @Test
    public void networkErrorTest() {
        when(priceFetcher.getPrice("INTC")).thenThrow(new RuntimeException("Network Error"));
        try{
            stockChecker.getStockPrice("INTC");
            fail("Expected exception for network error");
        }
        catch (RuntimeException ex) {
            assertEquals("Network Error", ex.getMessage());
        }
    }

    @Test
    public void calculateValueOfTwoStocksWithQuantityOfTen(){
        Map<String, Object>  expectedTotalAssetValueMap = new LinkedHashMap<>();

        when(stockChecker.getStockPrice("AAPL")).thenReturn(2048);
        when(stockChecker.getStockPrice("GOOG")).thenReturn(81496);

        tickersAndValue.put("AAPL", 10);
        tickersAndValue.put("GOOG", 10);

        expectedTotalAssetValueMap.put("AAPL", 20480);
        expectedTotalAssetValueMap.put("GOOG", 814960);
        expectedTotalAssetValueMap.put("TotalNetAssetValue", 835440);

        Map<String, Object>  actualTotalAssetValueMap = stockChecker.computeTotalAssetValue(tickersAndValue);
        assertEquals(expectedTotalAssetValueMap, actualTotalAssetValueMap);
    }

    @Test
    public void calculateValueOfTwoStocksWithOneStockQuantityOfZero(){
        Map<String, Object>  expectedTotalAssetValueMap = new LinkedHashMap<>();

        when(stockChecker.getStockPrice("AAPL")).thenReturn(2048);
        when(stockChecker.getStockPrice("GOOG")).thenReturn(81496);

        tickersAndValue.put("AAPL", 10);
        tickersAndValue.put("GOOG", 0);

        expectedTotalAssetValueMap.put("AAPL", 20480);
        expectedTotalAssetValueMap.put("GOOG", 0);
        expectedTotalAssetValueMap.put("TotalNetAssetValue", 20480);

        Map<String, Object>  actualTotalAssetValueMap = stockChecker.computeTotalAssetValue(tickersAndValue);
        assertEquals(expectedTotalAssetValueMap, actualTotalAssetValueMap);
    }

    @Test
    public void calculateValueOfTwoDifferentStockWithOneStockInvalid(){
        Map<String, Object>  expectedTotalAssetValueMap = new LinkedHashMap<>();
        List<String> invalidStockList = new ArrayList<>();

        when(stockChecker.getStockPrice("GOOG")).thenReturn(81496);
        when(stockChecker.getStockPrice("SVN")).thenThrow(new IllegalArgumentException("Invalid Stock"));

        tickersAndValue.put("GOOG", 10);
        tickersAndValue.put("SVN", 10);

        invalidStockList.add("SVN");
        expectedTotalAssetValueMap.put("GOOG", 814960);
        expectedTotalAssetValueMap.put("Invalid Stock", invalidStockList);
        expectedTotalAssetValueMap.put("TotalNetAssetValue", 814960);

        Map<String, Object>  actualTotalAssetValueMap = stockChecker.computeTotalAssetValue(tickersAndValue);
        assertEquals(expectedTotalAssetValueMap, actualTotalAssetValueMap);
    }

    @Test
    public void calculateValueOfThreeDifferentStockOneInvalidAndOneNetworkError()  {
        Map<String, Object> expectedTotalAssetValueMap = new LinkedHashMap<>();
        List<String> invalidStockList = new ArrayList<>();
        List<String> networkErrorList = new ArrayList<>();

        when(stockChecker.getStockPrice("GOOG")).thenReturn(81496);
        when(stockChecker.getStockPrice("INTC")).thenThrow(new RuntimeException("Network Error"));
        when(stockChecker.getStockPrice("SVN")).thenThrow(new IllegalArgumentException("Invalid Stock"));

        tickersAndValue.put("GOOG", 10);
        tickersAndValue.put("SVN", 10);
        tickersAndValue.put("INTC", 10);

        networkErrorList.add("INTC");
        invalidStockList.add("SVN");
        expectedTotalAssetValueMap.put("GOOG", 814960);
        expectedTotalAssetValueMap.put("Invalid Stock", invalidStockList);
        expectedTotalAssetValueMap.put("Network Error", networkErrorList);
        expectedTotalAssetValueMap.put("TotalNetAssetValue", 814960);

        Map<String, Object>  actualTotalAssetValueMap = stockChecker.computeTotalAssetValue(tickersAndValue);
        assertEquals(expectedTotalAssetValueMap, actualTotalAssetValueMap);
    }

    @Test
    public void calculateValueOfThreeDifferentStockTwoAreInvalid() throws Exception {
        Map<String, Object> expectedTotalAssetValueMap = new LinkedHashMap<>();
        List<String> invalidStockList = new ArrayList<>();

        when(stockChecker.getStockPrice("GOOG")).thenReturn(81496);
        when(stockChecker.getStockPrice("SVN")).thenThrow(new IllegalArgumentException("Invalid Stock"));
        when(stockChecker.getStockPrice("IT")).thenThrow(new IllegalArgumentException("Invalid Stock"));

        tickersAndValue.put("GOOG", 10);
        tickersAndValue.put("SVN", 10);
        tickersAndValue.put("IT", 10);

        invalidStockList.add("SVN");
        invalidStockList.add("IT");
        expectedTotalAssetValueMap.put("GOOG", 814960);
        expectedTotalAssetValueMap.put("Invalid Stock", invalidStockList);
        expectedTotalAssetValueMap.put("TotalNetAssetValue", 814960);

        Map<String, Object>  actualTotalAssetValueMap = stockChecker.computeTotalAssetValue(tickersAndValue);
        assertEquals(expectedTotalAssetValueMap, actualTotalAssetValueMap);
    }
}