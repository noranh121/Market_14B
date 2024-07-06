package IntegrationTest;

import org.market.DomainLayer.backend.Basket;
import org.market.DomainLayer.backend.Market;
import org.market.DomainLayer.backend.ProductPackage.Category;
import org.market.DomainLayer.backend.ProductPackage.Product;
import org.market.DomainLayer.backend.StorePackage.Store;
import org.market.DomainLayer.backend.StorePackage.StoreController;
import org.market.DomainLayer.backend.UserPackage.GuestUser;
import org.market.DomainLayer.backend.UserPackage.RegisteredUser;
import org.market.DomainLayer.backend.UserPackage.User;
import org.market.DomainLayer.backend.UserPackage.UserController;
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
        market = Market.getInstance();
        storeController = StoreController.getInstance();
        userController = UserController.getInstance();
        u1 = new GuestUser(0, 18);
        u2 = new RegisteredUser("ali", "123", 18);
        u3 = new RegisteredUser("malek", "456", 18);
        s1 = new Store("store1", "decs1", 0);
        s2 = new Store("store2", "decs2", 0);
        c1 = new Category(0, "c1");
        c2 = new Category(1, "c2");
        p1 = new Product("product1", "desc1", "brand1", c1, 5);
        p2 = new Product("product2", "desc2", "brand2", c2, 5);
    }

    @AfterEach
    void tearDown() {
        market.setToNull();
    }

    @Test
    void buyTestFail() {
        try {
            double sum = userController.Buy(u3.getUsername());
            fail("error in buy");
        } catch (Exception e) {
            assertEquals("user not found", e.getMessage());
        }
    }

    @Test
    public void testBuyNotEnoughSupply() throws Exception {
        userController.getGuestUserMap().put(u1.getUsername(), u1);
        Store s=new Store("store","desc",0);
        StoreController.getInstance().GetStores().put(0,s);
        s.getInventory().AddProduct(0,10.0,2,5);
        Basket b=new Basket(u1.getUsername(),0);
        u1.getShoppingCart().addBasket(b);
        b.addProduct(0,3);
        double expectedSum = 10.0;
        try {
            double sum = userController.Buy(u1.getUsername());
            fail();
        } catch (Exception e) {
            assertEquals("invalid cart" , e.getMessage());
            assertEquals(StoreController.getInstance().getStore(0).getInventory().getQuantity(0),2);
            assertEquals(UserController.getInstance().getUser(u1.getUsername()).getShoppingCart().getBasket(u1.getUsername(), 0).getProducts().size(),1);
        }
    }

    @Test
    void buyTestSuccess() throws Exception {
        userController.getGuestUserMap().put(u2.getUsername(), u2);
        storeController.GetStores().put(0, s1);
        storeController.addProduct(0, 0, 10, 15, 5);
        u2.addToCart(0, 0, 5);
        double sum = userController.Buy(u2.getUsername());
        assertEquals(50, sum);
        assertEquals(10, s1.getInventory().getQuantity(0));
    }

    @Test
    void testviewsystemPurchaseHistorySuccess() throws Exception {
        userController.getRegUserMap().put(u2.getUsername(), u2);
        storeController.GetStores().put(0, s1);
        storeController.addProduct(0, 0, 10, 15, 5);
        u2.addToCart(0, 0, 5);
        market.getSystemManagers().add("ali");
        double sum = userController.Buy(u2.getUsername());
        String result = market.viewSystemPurchaseHistory("ali");
        assertTrue(result.contains("Username: ali"));
        assertTrue(result.contains("Store ID: 0"));
        assertTrue(result.contains("Overall Price: 50"));
    }

    @Test
    void testAssignStoreManagerFail() {
        try {
            String result = market.AssignStoreManager(0, u2.getUsername(), u3.getUsername(),
                    new Boolean[] { true, true, true });
            fail("error in buy");
        } catch (Exception e) {
            assertEquals("ownerUserName not found", e.getMessage());
        }
    }

    @Test
    void testAssignStoreManagerSuccess() throws Exception {
        market.Register("ali", "123", 18);
        market.Register("malek", "456", 18);
        market.initStore(u2.getUsername(), "description");
        Boolean[] per = new Boolean[] { true, true, true };
        String result = market.AssignStoreManager(0, u2.getUsername(), u3.getUsername(), per);
        assertEquals("Permission added to store", result);
    }

    @Test
    void testAssignStoreOwnerFail() {
        try {
            String result = market.AssignStoreOwner(0, u2.getUsername(), u3.getUsername(),
                    new Boolean[] { true, true, true });
            fail("error in buy");
        } catch (Exception e) {
            assertEquals("ownerUserName not found", e.getMessage());
        }
    }

    @Test
    void testAssignStoreOwnerSuccess() throws Exception {
        market.Register("ali", "123", 18);
        market.Register("malek", "456", 18);
        market.initStore(u2.getUsername(), "description");
        Boolean[] per = new Boolean[] { true, true, true };
        String result = market.AssignStoreOwner(0, u2.getUsername(), u3.getUsername(), per);
        assertEquals("Permission added to store", result);
    }

    @Test
    public void testEditPermissionsFail() {
        try {
            String result = market.EditPermissions(0, u2.getUsername(), u3.getUsername(), true, false,
                    new Boolean[] { true, true, true });
            fail("error in buy");
        } catch (Exception e) {
            assertEquals("ownerUserName not found", e.getMessage());
        }
    }

    @Test
    public void testEditPermissionsSuccess() throws Exception {
        market.Register("ali", "123", 18);
        market.Register("malek", "456", 18);
        market.initStore(u2.getUsername(), "description");
        Boolean[] per = new Boolean[] { true, true, true };
        String result = market.AssignStoreOwner(0, u2.getUsername(), u3.getUsername(), per);
        Boolean[] per1 = new Boolean[] { true, false, true };
        try {
            String result1 = market.EditPermissions(0, u2.getUsername(), u3.getUsername(), true, false, per1);
            assertNotNull(result1);
            assertEquals("Permission added to store", result1);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testUnassignUserSuccess() throws Exception {
        market.Register("ali", "123", 18);
        market.Register("malek", "456", 18);
        market.initStore(u2.getUsername(), "description");
        Boolean[] per = new Boolean[] { true, true, true };
        String result = market.AssignStoreOwner(0, u2.getUsername(), u3.getUsername(), per);
        String result1 = market.unassignUser(0, u2.getUsername(), u3.getUsername());
        assertEquals(result1, "Permission deleted from store");
    }

    @Test
    public void testUnassignUserFail() {
        try {
            String result = market.unassignUser(0, u2.getUsername(), u3.getUsername());
        } catch (Exception e) {
            assertEquals("0 does not exist", e.getMessage());
        }
    }

    @Test
    public void testRegisterSuccess() {
        String username = "newUser";
        String password = "password123";

        try {
            String result = userController.Register(username, password, 18);
            assertEquals("guest user added successfully", result);
            assertTrue(userController.getRegUserMap().containsKey(username));
            assertFalse(userController.getRegUserMap().get(username).isLoggedIn());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterUsernameExists() {
        String username = "existingUser";
        String password = "password123";
        try {
            userController.Register(username, password, 18);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
        try {
            userController.Register(username, password, 18);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertEquals("username already exists", e.getMessage());
        }
    }

    @Test
    public void testRegisterDoubleRegistration() {
        String username = "newUser";
        String password = "password123";

        try {
            String result = userController.Register(username, password,18);
            assertEquals("guest user added successfully", result);
            assertTrue(userController.getRegUserMap().containsKey(username));
            assertFalse(userController.getRegUserMap().get(username).isLoggedIn());
            String result1 = userController.Register(username, password,18);
        } catch (Exception e) {
            assertEquals("username already exists" , e.getMessage());
        }
    }

    @Test
    public void testLoginSuccess() {
        try {
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            String result = market.Login("0", "ali", "123");
            assertEquals("logged in successfully", result);
            assertNull(userController.getGuestUserMap().get("0"));
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoginIncorrectUsername() {
        try {
            userController.getGuestUserMap().put(u1.getUsername(), u1);
            userController.getRegUserMap().put(u3.getUsername(), u3);
            String result = userController.Login(u1.getUsername(), "wrong", "123");
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertEquals("username or password are incorrect", e.getMessage());
        }
    }

    @Test
    public void testResignSuccess(){
        try{
            market.Register("ali","123",18);
            market.Register("malek","456",18);
            market.initStore(u2.getUsername(),"description");
            Boolean[] per=new Boolean[]{true,true,true};
            market.AssignStoreOwner(0,u2.getUsername(),u3.getUsername(),per);
            String result=market.resign(0, u3.getUsername());
            assertEquals(result,"deleted store owner");
        }catch(Exception e){
            fail(("Exception thrown: " + e.getMessage()));
        }
    }

    @Test
    public void testResignFail(){
        try{
            market.Register("ali","123",18);
            market.initStore(u2.getUsername(),"description");
            String result=market.resign(0, u2.getUsername());
            assertEquals(result,"deleted main store owner - store is closed perminantly");
        }catch(Exception e){
            fail(("Exception thrown: " + e.getMessage()));
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
    public void testProductDiscountPolicySuccess(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            market.Register("malek", "456", 18);
            market.initStore("ali", "d");
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            market.addProductDiscountPolicy(true, 0, 0, 0.1, 0, 0, "ali");
            market.addToCart("ali", 0, 0, 5);
            double sum=market.Buy("ali","dollar","123",5,2027,"123","Ab2","city","country",434);
            assertEquals(sum, 45.0);
        }catch(Exception e){
            fail(("Exception thrown: " + e.getMessage()));
        }
    }

    @Test
    public void testProductPurchasePolicySuccess(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(19);
            market.EnterAsGuest(19);
            market.Register("ali", "123", 19);
            market.Register("malek", "456", 18);
            market.initStore("ali", "d");
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            market.addProductPurchasePolicy(3,-1,null,0,-1,18,0,"ali",0,true);
            market.addToCart("ali", 0, 0, 2);
            double sum=market.Buy("ali","dollar","123",5,2027,"123","Ab2","city","country",434);
            fail("the test must fail");
        }catch(Exception e){
            assertEquals( "purchase failed",e.getMessage());
        }
    }
}
