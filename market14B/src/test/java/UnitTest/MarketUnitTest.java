package UnitTest;
import org.market.DomainLayer.backend.Market;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class MarketUnitTest {
    Market market;
    @BeforeEach
    public void setUp() {
        market=Market.getInstance();
    }

    @AfterEach
    void tearDown() {
        market.setToNull();
    }
    @Test
    public void testSetMarketOnlineSuccess() throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        assertTrue(market.getOnline());
    }

    @Test
    public void testSetMarketOnlineFail() {
        String nonSystemManager = "user";
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        Exception exception = assertThrows(Exception.class, () -> {
            market.setMarketOnline(nonSystemManager);
        });
        assertEquals("only system managers can change market's activity", exception.getMessage());
    }

    @Test
    public void testSetMarketOFFLINESuccess() throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOFFLINE(systemManager);
        assertFalse(market.getOnline());
    }

    @Test
    public void testSetMarketOFFLINEFail() {
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
            market.Register("ali","123",18);
            String result = market.initStore("ali", "Store Description");
            assertEquals("store added successfully", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSuspendUserSuccess(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            String result=market.suspendUser("admin", "ali");
            assertEquals("suspended successfully", result);
        }catch(Exception e){
            fail(("Exception thrown: " + e.getMessage()));
        }
    }

    @Test
    public void testSuspendUserFail(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            String result=market.suspendUser("bob", "ali");
            fail();
        }catch(Exception e){
            assertEquals(e.getMessage(),"bob not a system manager");
        }
    }

    @Test
    public void testSuspendSecSuccess(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            String result=market.suspendUserSeconds("admin", "ali",3);
            assertEquals("ali suspended for 3 seconds", result);
        }catch(Exception e){
            fail(("Exception thrown: " + e.getMessage()));
        }
    }

    @Test
    public void testSuspendUserSecFail(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            String result=market.suspendUserSeconds("bob", "ali",3);
            fail();
        }catch(Exception e){
            assertEquals(e.getMessage(),"bob not a system manager");
        }
    }

    @Test
    public void testResUserSuccess(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            market.suspendUser("admin", "ali");
            String result=market.resumeUser(systemManager, "ali");
            assertEquals(result, "ali unsuspended");
        }catch(Exception e){
            fail(("Exception thrown: " + e.getMessage()));
        }
    }

    @Test
    public void testResUserFail(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            market.suspendUser("admin", "ali");
            String result=market.resumeUser("bob", "ali");
            fail();
        }catch(Exception e){
            assertEquals(e.getMessage(),"bob not a system manager");
        }
    }

    @Test
    public void testViewSuspendedSuccss(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            market.suspendUser(systemManager, "ali");
            String result=market.viewSuspended(systemManager);
            assertTrue(result.contains("ali"));
        }catch(Exception e){
            fail(("Exception thrown: " + e.getMessage()));
        }
    }

    @Test
    public void testViewSuspendedFail(){
        try{
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            market.suspendUser("ali", "ali");
            String result=market.viewSuspended("ali");
            fail();
        }catch(Exception e){
            assertEquals(e.getMessage(),"ali not a system manager");
        }
    }
}
