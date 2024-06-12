package IntegrationTest;
import DomainLayer.backend.Market;
import DomainLayer.backend.ProductPackage.Category;
import DomainLayer.backend.ProductPackage.Product;
import DomainLayer.backend.StorePackage.Store;
import DomainLayer.backend.StorePackage.StoreController;
import DomainLayer.backend.UserPackage.GuestUser;
import DomainLayer.backend.UserPackage.RegisteredUser;
import DomainLayer.backend.UserPackage.User;
import DomainLayer.backend.UserPackage.UserController;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class integrationTests {
    Market market;
    StoreController storeController;
    UserController userController;
    Store s1;
    Store s2;
    User u1;
    User u2;
    User u3;
    Product p1;
    Product p2;
    Category c1;
    Category c2;
    @BeforeEach
    void setUp() {
        market=Market.getInstance();
        storeController = StoreController.getInstance();
        userController = UserController.getInstance();
        u1=new GuestUser(0);
        u2=new RegisteredUser("ali","123");
        u3=new RegisteredUser("malek","456");
        s1=new Store("store1","decs1",0);
        s2=new Store("store2","decs2",0);
        c1=new Category(0,"c1");
        c2=new Category(1,"c2");
        p1=new Product("product1","desc1","brand1",c1);
        p2=new Product("product2","desc2","brand2",c2);
    }
    @AfterEach
    void tearDown() {
        market.setToNull();
    }

    @Test
    void buyTest1(){
        try {
            double sum = userController.Buy(u3.getUsername());
            fail("error in buy");
        } catch (Exception e) {
            assertEquals("user not found" , e.getMessage());
        }
    }

    @Test
    void buyTest2() throws Exception {
        userController.getGuestUserMap().put(u2.getUsername(),u2);
        storeController.GetStores().put(0,s1);
        storeController.addProduct(0,0,10,15);
        u2.addToCart(0,0,5);
        double sum = userController.Buy(u2.getUsername());
        assertEquals(50,sum);
        assertEquals(10,s1.getInventory().getQuantity(0)); 
    }

    @Test
    void testviewsystemPurchaseHistory1() throws Exception {
        userController.getRegUserMap().put(u2.getUsername(),u2);
        storeController.GetStores().put(0,s1);
        storeController.addProduct(0,0,10,15);
        u2.addToCart(0,0,5);
        market.getSystemManagers().add("ali");
        double sum = userController.Buy(u2.getUsername());
        String result=market.viewsystemPurchaseHistory("ali");
        assertTrue(result.contains("Username: ali"));
        assertTrue(result.contains("Store ID: 0"));
        assertTrue(result.contains("Overall Price: 50"));
    }

    @Test
    void testAssignStoreManager1() {
        try {
            String result = market.AssignStoreManager(0,u2.getUsername(), u3.getUsername(), new Boolean[]{true,true,true});
            fail("error in buy");
        } catch (Exception e) {
            assertEquals("ownerUserName not found" , e.getMessage());
        }
    }

    @Test
    void testAssignStoreManager2() throws Exception {
        market.Register("ali","123");
        market.Register("malek","456");
        market.initStore(u2.getUsername(),"description");
        Boolean[] per=new Boolean[]{true,true,true};
        String result=market.AssignStoreManager(0,u2.getUsername(),u3.getUsername(),per);
        assertEquals("Permission added to store",result);
    }

    @Test
    void testAssignStoreOwner1() {
        try {
            String result = market.AssignStoreOwner(0,u2.getUsername(), u3.getUsername(), new Boolean[]{true,true,true});
            fail("error in buy");
        } catch (Exception e) {
            assertEquals("ownerUserName not found" , e.getMessage());
        }
    }

    @Test
    void testAssignStoreOwner2() throws Exception {
        market.Register("ali","123");
        market.Register("malek","456");
        market.initStore(u2.getUsername(),"description");
        Boolean[] per=new Boolean[]{true,true,true};
        String result=market.AssignStoreOwner(0,u2.getUsername(),u3.getUsername(),per);
        assertEquals("Permission added to store",result);
    }

    @Test
    public void testEditPermissions1()  {
        try {
            String result = market.EditPermissions(0,u2.getUsername(), u3.getUsername(),true,false, new Boolean[]{true,true,true});
            fail("error in buy");
        } catch (Exception e) {
            assertEquals("ownerUserName not found" , e.getMessage());
        }
    }


    @Test
    public void testEditPermissions2() throws Exception {
        market.Register("ali","123");
        market.Register("malek","456");
        market.initStore(u2.getUsername(),"description");
        Boolean[] per=new Boolean[]{true,true,true};
        String result=market.AssignStoreOwner(0,u2.getUsername(),u3.getUsername(),per);
        Boolean[] per1=new Boolean[]{true,false,true};
        try {
            String result1 = market.EditPermissions(0, u2.getUsername(), u3.getUsername(), true, false, per1);
            assertNotNull(result1);
            assertEquals("Permission added to store",result1);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testunassignUser1() throws Exception {
        market.Register("ali","123");
        market.Register("malek","456");
        market.initStore(u2.getUsername(),"description");
        Boolean[] per=new Boolean[]{true,true,true};
        String result=market.AssignStoreOwner(0,u2.getUsername(),u3.getUsername(),per);
        String result1=market.unassignUser(0, u2.getUsername(), u3.getUsername());
        assertEquals(result1,"Permission deleted from store");
    }

    @Test
    public void testunassignUser2()  {
        try {
            String result = market.unassignUser(0,u2.getUsername(), u3.getUsername());
        } catch (Exception e) {
            assertEquals("0 does not exist" , e.getMessage());
        }
    }

    @Test
    public void testRegister_Success() {
        String username = "newUser";
        String password = "password123";

        try {
            String result = userController.Register(username, password);
            assertEquals("guest user added successfully", result);
            assertTrue(userController.getRegUserMap().containsKey(username));
            assertFalse(userController.getRegUserMap().get(username).isLoggedIn());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testRegister_UsernameExists() {
        String username = "existingUser";
        String password = "password123";
        try {
            userController.Register(username, password);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
        try {
            userController.Register(username, password);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertEquals("username already exists", e.getMessage());
        }
    }
    @Test
    public void testLogin_Success() {
        try {
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest();
            market.Register("ali","123");
            String result = market.Login("0", "ali","123");
            assertEquals("logged in successfully", result);
            assertNull(userController.getGuestUserMap().get("0"));
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLogin_IncorrectUsername() {
        try {
            userController.getGuestUserMap().put(u1.getUsername(), u1);
            userController.getRegUserMap().put(u3.getUsername(), u3);
            String result = userController.Login(u1.getUsername(), "wrong","123");
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertEquals("username or password are incorrect", e.getMessage());
        }
    }


}
