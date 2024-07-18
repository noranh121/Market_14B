package IntegrationTest;

import org.market.Application;
import org.market.DomainLayer.backend.Basket;
import org.market.DomainLayer.backend.Market;
import org.market.DomainLayer.backend.ProductPackage.Category;
import org.market.DomainLayer.backend.ProductPackage.Product;
import org.market.DomainLayer.backend.StorePackage.Discount.DiscountPolicyController;
import org.market.DomainLayer.backend.StorePackage.Purchase.OfferMethod;
import org.market.DomainLayer.backend.StorePackage.Purchase.PurchasePolicyController;
import org.market.DomainLayer.backend.StorePackage.Store;
import org.market.DomainLayer.backend.StorePackage.StoreController;
import org.market.DomainLayer.backend.UserPackage.GuestUser;
import org.market.DomainLayer.backend.UserPackage.RegisteredUser;
import org.market.DomainLayer.backend.UserPackage.User;
import org.market.DomainLayer.backend.UserPackage.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.market.DomainLayer.backend.UserPackage.UserController.notfications;

import java.util.ArrayList;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
//@Transactional
public class integrationTests {
    @Autowired
    private ApplicationContext context;

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
        //Market.getDC().clearAll();
        market=(Market) context.getBean(Market.class);
        storeController = Market.getSC();
        userController = Market.getUC();
        org.market.DataAccessLayer.Entity.Market dataMarket=new org.market.DataAccessLayer.Entity.Market(0,true,new ArrayList<>());
        Market.getDC().getMarketRepository().save(dataMarket);
        u1 = new GuestUser(0, 18);
        u2 = new RegisteredUser("ali", "123", 18);
        u3 = new RegisteredUser("malek", "456", 18);
        s1 = new Store("store1","name", "decs1", 0);
        s2 = new Store("store2", "name","decs2", 0);
        c1 = new Category(0, "c1");
        c2 = new Category(1, "c2");
        p1 = new Product("product1", "desc1", "brand1", c1, 5);
        p2 = new Product("product2", "desc2", "brand2", c2, 5);
    }

    @AfterEach
    void tearDown() {
        market.clear();
        Market.getDC().clearAll();
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
        Store s=new Store("store","name","desc",0);
        Market.getSC().GetStores().put(0,s);
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
            assertEquals(Market.getSC().getStore(0).getInventory().getQuantity(0),2);
            assertEquals(Market.getUC().getUser(u1.getUsername()).getShoppingCart().getBasket(u1.getUsername(), 0).getProducts().size(),1);
        }
    }


    @Test
    void testviewsystemPurchaseHistorySuccess() throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        market.EnterAsGuest(18);
        market.Register("ali", "123", 18);
        market.Login("0","ali", "123");
        market.addCatagory(0,"meat",systemManager);
        market.initProduct(systemManager, "steak", 0, "d", "nike", 0);
        market.initStore("ali","name", "d");
        market.addProduct(0, 0, 10.0, 10, "ali", 5);
        market.addToCart("ali", 0, 0, 5);
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
        market.initStore(u2.getUsername(),"name", "description");
        Boolean[] per = new Boolean[] { true, true, true };
        String result = market.AssignStoreManager(0, u2.getUsername(), u3.getUsername(), per);
        assertEquals("Permission added to store", result);
        assertEquals(notfications.size(),1);
        assertEquals(notfications.get(0)[0],"malek");
        assertEquals(notfications.get(0)[1],"you have an updated permission");
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
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        market.EnterAsGuest(18);
        market.EnterAsGuest(18);
        market.Register("ali", "123", 18);
        market.Register("malek", "456", 18);
        market.Login("0","malek", "456");
        market.initStore("ali","name", "description");
        Boolean[] per = new Boolean[] { true, true, true };
        String result = market.AssignStoreOwner(0, u2.getUsername(), u3.getUsername(), per);
        assertEquals("Permission added to store", result);
        assertEquals(notfications.size(),1);
        assertEquals(notfications.get(0)[1],"you have an updated permission");
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
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        market.EnterAsGuest(18);
        market.EnterAsGuest(18);
        market.Register("ali", "123", 18);
        market.Register("malek", "456", 18);
        market.Login("1", "malek", "456");
        market.initStore(u2.getUsername(),"name", "description");
        Boolean[] per = new Boolean[] { true, true, true };
        String result = market.AssignStoreOwner(0, u2.getUsername(), u3.getUsername(), per);
        Boolean[] per1 = new Boolean[] { true, false, true };
        try {
            String result1 = market.EditPermissions(0, u2.getUsername(), u3.getUsername(), true, false, per1);
            assertNotNull(result1);
            assertEquals("Permission added to store", result1);
            assertEquals(notfications.size(),2);
            assertEquals(notfications.get(1)[1],"edited your permission");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testUnassignUserSuccess() throws Exception {
        market.Register("ali", "123", 18);
        market.Register("malek", "456", 18);
        market.initStore(u2.getUsername(),"name", "description");
        Boolean[] per = new Boolean[] { true, true, true };
        String result = market.AssignStoreOwner(0, u2.getUsername(), u3.getUsername(), per);
        String result1 = market.unassignUser(0, u2.getUsername(), u3.getUsername());
        assertEquals(3,notfications.size());
        assertEquals(notfications.get(0)[0],"malek");
        assertEquals(notfications.get(2)[1],"deleted your permission");
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
            assertEquals("User registered successfully", result);
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
            assertEquals("User registered successfully", result);
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
            market.initStore(u2.getUsername(),"name", "description");
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
            market.initStore(u2.getUsername(),"name", "description");
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
            assertEquals(notfications.size(),1);
            assertEquals(notfications.get(0)[1],"you were suspended, you can only view");
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
            assertEquals(notfications.size(),1);
            assertEquals(notfications.get(0)[1],"you were suspended for 3 seconds, you can only view");
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
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager, "steak", 0, "d", "nike", 0);
            market.initStore("ali","name", "d");
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            market.addProductDiscountPolicy(true, 0, 0, 0.1, 0, 0, "ali",0);
            market.addToCart("ali", 0, 0, 5);
            double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
            assertEquals(sum, 45.0);
            assertEquals(notfications.size(),1);
            assertEquals(notfications.get(0)[0],"ali");
            assertEquals(notfications.get(0)[1],"Your purchase was successful");
        }catch(Exception e){
            fail(("Exception thrown: " + e.getMessage()));
        }
    }

    @Test
    public void testANDDiscountPolicySuccess(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            market.Register("malek", "456", 18);
            market.initStore("ali","name", "d");
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager,"steak",0,"d","b",5.0);
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            DiscountPolicyController.LogicalRule role=DiscountPolicyController.LogicalRule.AND;
            market.addLogicalDiscount("ali",0,"And",0);
            market.addProductDiscountPolicy(true, 0, 0, 0.1, 0, 0, "ali",1);
            market.addCategoryDiscountPolicy(true,0,0,0.2,0,0,"ali",1);
            market.addToCart("ali", 0, 0, 5);
            double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
            assertEquals(sum, 36.0);
            assertEquals(notfications.size(),1);
            assertEquals(notfications.get(0)[0],"ali");
            assertEquals(notfications.get(0)[1],"Your purchase was successful");
        }catch(Exception e){
            fail(("Exception thrown: " + e.getMessage()));
        }
    }


    @Test
    public void testORDiscountPolicySuccess(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            market.Register("malek", "456", 18);
            market.initStore("ali","name", "d");
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager,"steak",0,"d","b",5.0);
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            DiscountPolicyController.LogicalRule role=DiscountPolicyController.LogicalRule.OR;
            market.addLogicalDiscount("ali",0,"Or",0);
            market.addProductDiscountPolicy(true, 0, 0, 0.1, 0, 0, "ali",1);
            market.addCategoryDiscountPolicy(true,0,0,0.2,0,0,"ali",1);
            market.addToCart("ali", 0, 0, 5);
            double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
            assertEquals(sum, 36.0);
            assertEquals(notfications.size(),1);
            assertEquals(notfications.get(0)[0],"ali");
            assertEquals(notfications.get(0)[1],"Your purchase was successful");
        }catch(Exception e){
            fail(("Exception thrown: " + e.getMessage()));
        }
    }


    @Test
    @Transactional
    public void testXORDiscountPolicySuccess(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            market.Register("malek", "456", 18);
            market.initStore("ali","name", "d");
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager,"steak",0,"d","b",5.0);
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            DiscountPolicyController.LogicalRule role=DiscountPolicyController.LogicalRule.XOR;
            market.addLogicalDiscount("ali",0,"Xor",0);
            market.addProductDiscountPolicy(true, 0, 0, 0.1, 0, 0, "ali",1);
            market.addCategoryDiscountPolicy(true,0,0,0.2,0,0,"ali",1);
            market.addToCart("ali", 0, 0, 5);
            double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
            assertEquals(sum, 45.0);
            assertEquals(notfications.size(),1);
            assertEquals(notfications.get(0)[0],"ali");
            assertEquals(notfications.get(0)[1],"Your purchase was successful");
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
            market.initStore("ali","name", "d");
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager,"steak",0,"d","b",5.0);
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            market.addProductPurchasePolicy(3,-1,null,0,-1,18,0,"ali",0,true,0);
            market.addToCart("ali", 0, 0, 2);
            double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
            fail("the test must fail");
        }catch(Exception e){
            assertEquals( "purchase failed",e.getMessage());
        }
    }

    @Test
    public void testANDProductPurchasePolicySuccess(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(19);
            market.EnterAsGuest(19);
            market.Register("ali", "123", 19);
            market.Register("malek", "456", 18);
            market.initStore("ali","name", "d");
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager,"steak",0,"d","b",5.0);
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            PurchasePolicyController.LogicalRule role=PurchasePolicyController.LogicalRule.AND;
            market.addLogicalPurchase("ali",0,"And",0);
            market.addProductPurchasePolicy(3,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addCategoryPurchasePolicy(4,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addToCart("ali", 0, 0, 4);
            double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
            assertEquals(sum, 40.0);
            assertEquals(notfications.size(),1);
            assertEquals(notfications.get(0)[0],"ali");
            assertEquals(notfications.get(0)[1],"Your purchase was successful");
        }catch(Exception e){
            fail("error thrown: " + e.getMessage());
        }
    }

    @Test
    public void testANDProductPurchasePolicyFail(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(19);
            market.EnterAsGuest(19);
            market.Register("ali", "123", 19);
            market.Register("malek", "456", 18);
            market.initStore("ali","name", "d");
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager,"steak",0,"d","b",5.0);
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            PurchasePolicyController.LogicalRule role=PurchasePolicyController.LogicalRule.AND;
            market.addLogicalPurchase("ali",0,"And",0);
            market.addProductPurchasePolicy(3,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addCategoryPurchasePolicy(5,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addToCart("ali", 0, 0, 4);
            double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
            fail("the test must fail");
        }catch(Exception e){
            assertEquals( "purchase failed",e.getMessage());
        }
    }


    @Test
    public void testORProductPurchasePolicySuccess(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(19);
            market.EnterAsGuest(19);
            market.Register("ali", "123", 19);
            market.Register("malek", "456", 18);
            market.initStore("ali","name", "d");
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager,"steak",0,"d","b",5.0);
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            PurchasePolicyController.LogicalRule role=PurchasePolicyController.LogicalRule.OR;
            market.addLogicalPurchase("ali",0,"Or",0);
            market.addProductPurchasePolicy(3,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addCategoryPurchasePolicy(4,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addToCart("ali", 0, 0, 4);
            double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
            assertEquals(sum, 40.0);
            assertEquals(notfications.size(),1);
            assertEquals(notfications.get(0)[0],"ali");
            assertEquals(notfications.get(0)[1],"Your purchase was successful");
        }catch(Exception e){
            fail("error thrown: " + e.getMessage());
        }
    }

    @Test
    public void testORProductPurchasePolicyFail(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(19);
            market.EnterAsGuest(19);
            market.Register("ali", "123", 19);
            market.Register("malek", "456", 18);
            market.initStore("ali","name", "d");
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager,"steak",0,"d","b",5.0);
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            PurchasePolicyController.LogicalRule role=PurchasePolicyController.LogicalRule.OR;
            market.addLogicalPurchase("ali",0,"Or",0);
            market.addProductPurchasePolicy(3,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addCategoryPurchasePolicy(5,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addToCart("ali", 0, 0, 2);
            double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
            fail("the test must fail");
        }catch(Exception e){
            assertEquals( "purchase failed",e.getMessage());
        }
    }

    @Test
    public void testComplexDiscountPolicySuccess(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            market.Register("malek", "456", 18);
            market.initStore("ali","name", "d");
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager,"steak",0,"d","b",5.0);
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            DiscountPolicyController.LogicalRule role1=DiscountPolicyController.LogicalRule.AND;
            market.addLogicalDiscount("ali",0,"And",0);
            market.addProductDiscountPolicy(true, 0, 0, 0.1, 0, 0, "ali",1);
            market.addCategoryDiscountPolicy(true,0,0,0.2,0,0,"ali",1);
            DiscountPolicyController.LogicalRule role2=DiscountPolicyController.LogicalRule.OR;
            market.addLogicalDiscount("ali",0,"Or",1);
            market.addStoreDiscountPolicy(true, 0, 0, 0.1, 0,  "ali",4);
            market.addToCart("ali", 0, 0, 5);
            double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
            assertEquals(sum, 32.400000000000006);
            assertEquals(notfications.size(),1);
            assertEquals(notfications.get(0)[0],"ali");
            assertEquals(notfications.get(0)[1],"Your purchase was successful");
        }catch(Exception e){
            fail(("Exception thrown: " + e.getMessage()));
        }
    }

    @Test
    public void testComplexPurchasePolicySuccess(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.EnterAsGuest(18);
            market.Register("ali", "123", 18);
            market.Register("malek", "456", 18);
            market.initStore("ali","name", "d");
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager,"steak",0,"d","b",5.0);
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            PurchasePolicyController.LogicalRule role1=PurchasePolicyController.LogicalRule.AND;
            market.addLogicalPurchase("ali",0,"And",0);
            market.addProductPurchasePolicy(3,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addCategoryPurchasePolicy(5,-1,null,0,-1,18,0,"ali",0,true,1);
            PurchasePolicyController.LogicalRule role2=PurchasePolicyController.LogicalRule.OR;
            market.addLogicalPurchase("ali",0,"Or",1);
            market.addUserPurchasePolicy(3,-1,null,0,-1,18,19,"ali",0,true,4);
            market.addToCart("ali", 0, 0, 5);
            double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
            assertEquals(sum, 50);
            assertEquals(notfications.size(),1);
            assertEquals(notfications.get(0)[0],"ali");
            assertEquals(notfications.get(0)[1],"Your purchase was successful");
        }catch(Exception e){
            fail(("Exception thrown: " + e.getMessage()));
        }
    }

    @Test
    public void testUseCase1(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.EnterAsGuest(18);
            market.EnterAsGuest(18);
            market.Register("u1", "123", 18);
            market.Register("u2", "456", 18);
            market.Register("u3","789",18);
            market.initStore("u1","name", "d");
            Boolean[] per=new Boolean[]{true,true,true};
            market.AssignStoreOwner(0,"u1","u2",per);
            market.AssignStoreOwner(0,"u2","u3",per);
            market.resign(0,"u2");
            market.AssignStoreOwner(0,"u3","u2",per);
            fail("the test must fail");
        }catch (Exception e){
            assertEquals("u3 not owner",e.getMessage());
        }
    }

    @Test
    public void testUseCase2(){
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(18);
            market.EnterAsGuest(18);
            market.EnterAsGuest(18);
            market.Register("u1", "123", 18);
            market.Register("u2", "456", 18);
            market.initStore("u1","name", "d");
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager,"steak",0,"d","b",5.0);
            market.addProduct(0, 0, 10.0, 10, "u1", 5);
            market.suspendUser(systemManager,"u2");
            try {
                market.addToCart("u2",0,0,5);
                fail("the test must fail");
            }catch (Exception ex){
                assertEquals("can't add to cart user is suspended",ex.getMessage());
            }
            String res1=market.viewSuspended(systemManager);
            assertTrue(res1.contains("u2"));
            market.resumeUser(systemManager,"u2");
            String res2=market.addToCart("u2",0,0,5);
            assertEquals("added to cart",res2);
        }catch (Exception e){
            fail("test must not fail");
        }
    }

    // @Test
    // public void testOfferMethodSuccess(){
    //     try{
    //         String systemManager = "admin";
    //         market.getSystemManagers().add(systemManager);
    //         market.setMarketOnline(systemManager);
    //         market.EnterAsGuest(18);
    //         market.EnterAsGuest(18);
    //         market.Register("ali", "123", 18);
    //         market.Register("malek", "456", 18);
    //         market.initStore("ali","name", "d");
    //         market.addCatagory(0,"meat",systemManager);
    //         market.initProduct(systemManager,"steak",0,"d","b",5.0);
    //         market.addProduct(0, 0, 10.0, 10, "ali", 5);
    //         OfferMethod offerMethod=new OfferMethod(3, -1, null, 0, -1, 18, 0, "ali");
    //         //storeController.getStore(0).addPurchaseComposite(offerMethod,0);
    //     }catch(Exception ex){
    //         fail(ex.getMessage());
    //     }
    // }

    // @Test
    // public void testOfferAccepted() {
    //     try{
    //         String systemManager = "admin";
    //         market.getSystemManagers().add(systemManager);
    //         market.setMarketOnline(systemManager);
    //         market.EnterAsGuest(18);
    //         market.EnterAsGuest(18);
    //         market.Register("ali", "123", 18);
    //         market.Register("malek", "456", 18);
    //         market.initStore("ali","name", "d");
    //         market.addCatagory(0,"meat",systemManager);
    //         market.initProduct(systemManager,"steak",0,"d","b",5.0);
    //         market.addProduct(0, 0, 10.0, 10, "ali", 5);
    //         market.sendOffer("malek",0,0,10.0,9.0);
    //         String ans = market.approveOffer("ali", "malek", 0, 0, 9);
    //         assertTrue(ans.contains("accepted!"));
    //         assertTrue(market.cartContains("malek",0));
    //     }catch(Exception ex){
    //         fail(ex.getMessage());
    //     }
    // }

    // @Test
    // public void testOfferRejected() {
    //     try{
    //         String systemManager = "admin";
    //         market.getSystemManagers().add(systemManager);
    //         market.setMarketOnline(systemManager);// no please
    //         market.EnterAsGuest(18);
    //         market.EnterAsGuest(18);
    //         market.Register("ali", "123", 18);
    //         market.Register("malek", "456", 18);
    //         market.initStore("ali","name", "d");
    //         market.addCatagory(0,"meat",systemManager);
    //         market.initProduct(systemManager,"steak",0,"d","b",5.0);
    //         market.addProduct(0, 0, 10.0, 10, "ali", 5);
    //         market.sendOffer("malek",0,0,10.0,9.0);
    //         String ans = market.rejectOffer("ali", "malek", 0, 0);
    //         assertTrue(ans.contains("rejected"));
    //         assertFalse(market.cartContains("ali",0));
    //     }catch(Exception ex){
    //         fail(ex.getMessage());
    //     }
    // }

    // @Test
    // public void testInitFileSuccess() {
    //     try{
    //         String u1 = "admin";
    //         market.getSystemManagers().add(u1);
    //         market.setMarketOnline(u1);
    //         market.EnterAsGuest(18);
    //         market.EnterAsGuest(18);
    //         market.EnterAsGuest(18);
    //         market.EnterAsGuest(18);
    //         market.EnterAsGuest(18);
    //         market.Register("u2","123",18);
    //         market.Register("u3","123",18);
    //         market.Register("u4","123",18);
    //         market.Register("u5","123",18);
    //         market.Register("u6","123",18);
    //         market.Login("0","u2","123");
    //         market.initStore("u2","s1","groceries");
    //         market.addCatagory(0,"snacks",u1);
    //         market.initProduct(u1,"Bamba",0,"peanut-butter flavored","Osem",20);
    //         market.addProduct(0,0,30,20,"u2",20);
    //         market.AssignStoreManager(0,"u2","u3",new Boolean[]{true,false,false});
    //         market.AssignStoreOwner(0,"u2","u4",new Boolean[]{true,false,false}); 
    //         market.AssignStoreOwner(0,"u2","u5",new Boolean[]{true,false,false}); 
    //         assertTrue(market.getAllStores().size() > 0);
    //         assertEquals("s1", market.getStore(0).getName());
    //         assertTrue(market.getAllProducts().size() > 0);
    //     }catch(Exception ex){
    //         fail(ex.getMessage());
    //     }
    // }

    // @Test
    // public void testInitFileFail() {
    //     try{
    //         String u1 = "admin";
    //         market.getSystemManagers().add(u1);
    //         market.setMarketOnline(u1);
    //         market.EnterAsGuest(18);
    //         market.EnterAsGuest(18);
    //         market.EnterAsGuest(18);
    //         market.EnterAsGuest(18);
    //         market.EnterAsGuest(18);
    //         market.Register("u2","123",18);
    //         market.Register("u3","123",18);
    //         market.Register("u4","123",18);
    //         market.Register("u5","123",18);
    //         market.Register("u6","123",18);
    //         market.Login("0","u2","123");
    //         market.initStore("u2","s1","groceries");
    //         market.addCatagory(0,"snacks","u2");
    //         market.initProduct("u2","Bamba",0,"peanut-butter flavored","Osem",20);
    //         market.addProduct(1,0,30,20,"u2",20);
    //         market.AssignStoreManager(0,"u2","u3",new Boolean[]{true,false,false});
    //         //market.AssignStoreOwner(0,"u2","u4",new Boolean[]{true,false,false}); 
    //         market.AssignStoreOwner(0,"u4","u5",new Boolean[]{true,false,false}); 
    //         fail();
    //     }catch(Exception ex){
    //         assertEquals("u2 is not system manager", ex.getMessage());
    //     }
    // }

}
