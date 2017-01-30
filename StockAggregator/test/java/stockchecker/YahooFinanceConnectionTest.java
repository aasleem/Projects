package stockchecker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class YahooFinanceConnectionTest {

    private YahooFinanceConnection yahooConnection;

    @Before
    public void setUp(){
        yahooConnection = new YahooFinanceConnection();
    }

    @Test
    public void callAndGetPriceOfValidStock(){
        assertTrue(yahooConnection.getPrice("GOOG") > 0 );
    }

    @Test
    public void callAndGetPriceOfInvalidStock(){
        try {
            yahooConnection.getPrice("SVN123");
            fail("Should throw exception invalid stock");
        }
        catch (IllegalArgumentException ex){
            assertEquals("Invalid Stock", ex.getMessage());
        }
    }

    @Test
    public void callAndGetPriceButNetworkFail() throws IOException{
        try {
            yahooConnection = spy(new YahooFinanceConnection());
            when(yahooConnection.getConnectionToYahoo("AAPL")).thenThrow(new IOException());

            yahooConnection.getPrice("AAPL");
            fail("Expected exception for network error");
        }
        catch (RuntimeException ex) {
            assertEquals("Network Error", ex.getMessage());
        }
    }
}