package Tests.UnitTest;
import DomainLayer.backend.Market;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class MarketUnitTest {
    Market market;
    @BeforeEach
    public void setUp() {
        market=Market.getInstance();
    }
    @Test
    public void testSetMarketOnline_Success() throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        assertTrue(market.getOnline());
    }

    @Test
    public void testSetMarketOnline_NotSystemManager() {
        String nonSystemManager = "user";
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        Exception exception = assertThrows(Exception.class, () -> {
            market.setMarketOnline(nonSystemManager);
        });
        assertEquals("only system managers can change market's activity", exception.getMessage());
    }

    @Test
    public void testSetMarketOFFLINE_Success() throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOFFLINE(systemManager);
        assertFalse(market.getOnline());
    }

    @Test
    public void testSetMarketOFFLINE_NotSystemManager() {
        String nonSystemManager = "user";
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        Exception exception = assertThrows(Exception.class, () -> {
            market.setMarketOFFLINE(nonSystemManager);
        });
        assertEquals("only system managers can change market's activity", exception.getMessage());
    }

    @Test
    public void testInitStore() {
        try {
            String result = market.initStore("ali", "Store Description");
            assertEquals("store added successfully", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
