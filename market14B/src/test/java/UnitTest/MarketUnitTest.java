package UnitTest;
import DomainLayer.backend.Market;
import DomainLayer.backend.UserPackage.RegisteredUser;
import DomainLayer.backend.UserPackage.User;
import DomainLayer.backend.UserPackage.UserController;
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
            market.Register("ali","123",18);
            String result = market.initStore("ali", "Store Description");
            assertEquals("store added successfully", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testsuspendUser1(){
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
    public void testsuspendUser2(){
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
    public void testSuspendSec1(){
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
    public void testsuspendUserSec2(){
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
    public void testResUSer1(){
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
    public void testResUSer2(){
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
    public void testViewSuspended(){
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
}
