package UnitTest;
import org.market.Application;
import org.market.DataAccessLayer.Entity.Store;
import org.market.DomainLayer.backend.Market;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class MarketUnitTest {
    @Autowired
    private ApplicationContext context;
    Market market;
    @BeforeEach
    public void setUp() {
        //Market.getDC().clearAll();
        market=(Market) context.getBean(Market.class);
        org.market.DataAccessLayer.Entity.Market dataMarket=new org.market.DataAccessLayer.Entity.Market(0,true,new ArrayList<>());
        Market.getDC().getMarketRepository().save(dataMarket);
    }

    @AfterEach
    void tearDown() {
        market.clear();
        Market.getDC().clearAll();
    }
    @Test
    public void testSetMarketOnlineSuccess() throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        assertTrue(market.getOnline());
        assertTrue(Market.getDC().getOnline());
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
        assertTrue(Market.getDC().getOnline());
    }

    @Test
    public void testSetMarketOFFLINESuccess() throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOFFLINE(systemManager);
        assertFalse(market.getOnline());
        assertFalse(Market.getDC().getOnline());
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
            String result = market.initStore("ali", "stroe name","Store Description");
            assertEquals("store added successfully", result);
            Store s=Market.getDC().getStore(0);
            assertEquals(s.getStoreID(), 0);
            assertEquals(s.getName(),"store name");
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
            assertTrue(Market.getDC().getUserRepository().getReferenceById("ali").getSuspended());
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
            assertNull(Market.getDC().getUserRepository().getReferenceById("ali").getSuspended());
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
            assertNull(Market.getDC().getUserRepository().getReferenceById("ali").getSuspended());
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
            assertFalse(Market.getDC().getUserRepository().getReferenceById("ali").getSuspended());
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
