package AcceptanceTest.Util;

import org.market.Application;
import org.market.DomainLayer.backend.Market;
import org.market.DomainLayer.backend.ProductPackage.Category;
import org.market.DomainLayer.backend.ProductPackage.Product;
import org.market.DomainLayer.backend.StorePackage.Discount.DiscountPolicyController;
import org.market.DomainLayer.backend.StorePackage.Purchase.PurchasePolicyController;
import org.market.DomainLayer.backend.StorePackage.Store;
import org.market.DomainLayer.backend.StorePackage.StoreController;
import org.market.DomainLayer.backend.UserPackage.RegisteredUser;
import org.market.DomainLayer.backend.UserPackage.User;
import org.market.DomainLayer.backend.UserPackage.UserController;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import AcceptanceTest.AccTestImpl;

import static org.junit.jupiter.api.Assertions.*;

public class Real implements Bridge {

    static Market market;
    static StoreController storeController ;
    static UserController userController ;
    Store s1 = new Store("store1", "name","decs1", 0);
    Store s2 = new Store("store2", "name","decs2", 1);
    User u2 = new RegisteredUser("ali", "123", 18);
    User u3 = new RegisteredUser("malek", "456", 18);
    Category c1 = new Category(0, "c1");
    Category c2 = new Category(1, "c2");
    Product p1 = new Product("product1", "desc1", "brand1", c1, 5);
    Product p2 = new Product("product2", "desc2", "brand2", c2, 5);
    public static void beforeEach(){
        market=AccTestImpl.market;
        storeController=Market.getSC();
        userController=Market.getUC();
    }

    @Override
    public void testSetMarketOnline(String username) throws Exception {
        userController.getRegUserMap().put(u2.getUsername(), u2);
        market.getSystemManagers().add(u2.getUsername());
        market.setMarketOnline(username);
    }

    @Override
    public String testEnterAsGuest() throws Exception {
        return market.EnterAsGuest(18);
    }

    @Override
    public String testGuestExit(String username) throws Exception {
        market.EnterAsGuest(18);
        return market.GuestExit("0");
    }

    @Override
    public String testRegister(String username, String password, int age) throws Exception {
        return market.Register(username, password, age);
    }

    @Override
    public String testLogin(String username, String password) throws Exception {
        market.EnterAsGuest(18);
        market.Register(username, password, 18);
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        return market.Login("0", username, password);
    }

    @Override
    public String testGetInfo(int storeId, String username) throws Exception {
        market.Register(username, "12", 18);
        market.initStore(username,"name", "d");
        market.addProduct(0, 0, 10, 5, username, 5);
        return market.getInfo(storeId, username);
    }

    @Override
    public String testAddToCart(String username, Integer product, int storeId, int quantity) throws Exception {
        //beforeEach();
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        market.EnterAsGuest(18);
        market.EnterAsGuest(18);
        market.Register("u", "1", 18);
        market.Register(username, "123", 18);
        market.Login("0", "u", "1");
        market.Login("1", username, "123");
        market.initStore("u","name", "d");
        market.addProduct(0, 0, 10, 5, "u", 5);
        return market.addToCart(username, product, storeId, quantity);
    }

    @Override
    public String testInspectCart(String username) {
        userController.getRegUserMap().put(u3.getUsername(), u3);
        return userController.inspectCart(username);
    }


    @Override
    public double testBuy(String username) throws Exception {
        userController.getGuestUserMap().put(u2.getUsername(), u2);
        storeController.GetStores().put(0, s1);
        storeController.addProduct(0, 0, 10, 15, 5);
        u2.addToCart(0, 0, 5);
        return userController.Buy(u2.getUsername());
    }

    @Override
    public double testBuyNotEnoughSupply(String username) throws Exception {
        userController.getGuestUserMap().put(u2.getUsername(), u2);
        storeController.GetStores().put(0, s1);
        storeController.addProduct(0, 0, 10, 15, 5);
        u2.addToCart(0, 0, 50);
        return userController.Buy(u2.getUsername());
    }

    @Override
    public double testBuySupplyFail(String username) throws Exception {
        userController.getGuestUserMap().put(u2.getUsername(), u2);
        storeController.GetStores().put(0, s1);
        storeController.addProduct(0, 0, 10, 15, 5);
        u2.addToCart(0, 0, 5);
        if (supplyApi()) {
            return userController.Buy(u2.getUsername());
        }
        throw new Exception("Supply fail");
    }

    @Override
    public double testBuyPaymentFail(String username) throws Exception {
        userController.getGuestUserMap().put(u2.getUsername(), u2);
        storeController.GetStores().put(0, s1);
        storeController.addProduct(0, 0, 10, 15, 5);
        u2.addToCart(0, 0, 5);
        if (paymentApi()) {
            return userController.Buy(u2.getUsername());
        }
        throw new Exception("payment fail");
    }

    @Override
    public double testBuyShippingFail(String username) throws Exception {
        userController.getGuestUserMap().put(u2.getUsername(), u2);
        storeController.GetStores().put(0, s1);
        storeController.addProduct(0, 0, 10, 15, 5);
        u2.addToCart(0, 0, 5);
        if (shippingApi()) {
            return userController.Buy(u2.getUsername());
        }
        throw new Exception("shipping fail");
    }

    @Override
    public double testProductDiscountPolicySuccess(String username) throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        market.EnterAsGuest(18);
        market.EnterAsGuest(18);
        market.Register(username, "123", 18);
        market.Register("malek", "456", 18);
        market.initStore(username,"name", "d");
        market.addProduct(0, 0, 10.0, 10, "ali", 5);
        market.addProductDiscountPolicy(true, 0, 0, 0.1, 0, 0, "ali",0);
        market.addToCart("ali", 0, 0, 5);
        return market.Buy(username, "dollar", "123", 5, 2027, "123", "Ab2", "city", "country", 434);
    }

    @Override
    public double testANDDiscountPolicySuccess(String username) throws Exception {
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
        market.addLogicalDiscount("ali",0,role,0);
        market.addProductDiscountPolicy(true, 0, 0, 0.1, 0, 0, "ali",1);
        market.addCategoryDiscountPolicy(true,0,0,0.2,0,0,"ali",1);
        market.addToCart("ali", 0, 0, 5);
        return market.Buy("ali","dollar","123",5,2027,"123","Ab2","city","country",434);
    }

    @Override
    public double testORDiscountPolicySuccess(String username) throws Exception {
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
        market.addLogicalDiscount("ali",0,role,0);
        market.addProductDiscountPolicy(true, 0, 0, 0.1, 0, 0, "ali",1);
        market.addCategoryDiscountPolicy(true,0,0,0.2,0,0,"ali",1);
        market.addToCart("ali", 0, 0, 5);
        return market.Buy("ali","dollar","123",5,2027,"123","Ab2","city","country",434);
    }

    @Override
    public double testXORDiscountPolicySuccess(String username) throws Exception {
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
        market.addLogicalDiscount("ali",0,role,0);
        market.addProductDiscountPolicy(true, 0, 0, 0.1, 0, 0, "ali",1);
        market.addCategoryDiscountPolicy(true,0,0,0.2,0,0,"ali",1);
        market.addToCart("ali", 0, 0, 5);
        return market.Buy("ali","dollar","123",5,2027,"123","Ab2","city","country",434);
    }

    @Override
    public double testProductPurchasePolicySuccess(String username) throws Exception {
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
            double sum=market.Buy("ali","dollar","123",5,2027,"123","Ab2","city","country",434);
            return sum;
        }catch(Exception e){
            return -1;
        }
    }

    @Override
    public double testANDProductPurchasePolicySuccess(String username) throws Exception {
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
        market.addLogicalPurchase("ali",0,role,0);
        market.addProductPurchasePolicy(3,-1,null,0,-1,18,0,"ali",0,true,1);
        market.addCategoryPurchasePolicy(4,-1,null,0,-1,18,0,"ali",0,true,1);
        market.addToCart("ali", 0, 0, 4);
        return market.Buy("ali","dollar","123",5,2027,"123","Ab2","city","country",434);
    }

    @Override
    public double testANDProductPurchasePolicyFail(String username) throws Exception {
        try{
            String systemManager = "admin";
            market.getSystemManagers().add(systemManager);
            market.setMarketOnline(systemManager);
            market.EnterAsGuest(19);
            market.EnterAsGuest(19);
            market.Register("ali", "123", 19);
            market.Register("malek", "456", 18);
            market.initStore("ali", "name","d");
            market.addCatagory(0,"meat",systemManager);
            market.initProduct(systemManager,"steak",0,"d","b",5.0);
            market.addProduct(0, 0, 10.0, 10, "ali", 5);
            PurchasePolicyController.LogicalRule role=PurchasePolicyController.LogicalRule.AND;
            market.addLogicalPurchase("ali",0,role,0);
            market.addProductPurchasePolicy(3,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addCategoryPurchasePolicy(5,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addToCart("ali", 0, 0, 4);
            return market.Buy("ali","dollar","123",5,2027,"123","Ab2","city","country",434);
        }catch(Exception e){
            return -1;
        }
    }

    @Override
    public double testORProductPurchasePolicySuccess(String username) throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        market.EnterAsGuest(19);
        market.EnterAsGuest(19);
        market.Register("ali", "123", 19);
        market.Register("malek", "456", 18);
        market.initStore("ali","name" ,"d");
        market.addCatagory(0,"meat",systemManager);
        market.initProduct(systemManager,"steak",0,"d","b",5.0);
        market.addProduct(0, 0, 10.0, 10, "ali", 5);
        PurchasePolicyController.LogicalRule role=PurchasePolicyController.LogicalRule.OR;
        market.addLogicalPurchase("ali",0,role,0);
        market.addProductPurchasePolicy(3,-1,null,0,-1,18,0,"ali",0,true,1);
        market.addCategoryPurchasePolicy(4,-1,null,0,-1,18,0,"ali",0,true,1);
        market.addToCart("ali", 0, 0, 4);
        return market.Buy("ali","dollar","123",5,2027,"123","Ab2","city","country",434);
    }

    @Override
    public double testORProductPurchasePolicyFail(String username) throws Exception {
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
            market.addLogicalPurchase("ali",0,role,0);
            market.addProductPurchasePolicy(3,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addCategoryPurchasePolicy(5,-1,null,0,-1,18,0,"ali",0,true,1);
            market.addToCart("ali", 0, 0, 2);
            return market.Buy("ali","dollar","123",5,2027,"123","Ab2","city","country",434);
        }catch(Exception e){
            return -1;
        }
    }

    @Override
    public double testComplexDiscountPolicySuccess(String username) throws Exception {
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
        market.addLogicalDiscount("ali",0,role1,0);
        market.addProductDiscountPolicy(true, 0, 0, 0.1, 0, 0, "ali",1);
        market.addCategoryDiscountPolicy(true,0,0,0.2,0,0,"ali",1);
        DiscountPolicyController.LogicalRule role2=DiscountPolicyController.LogicalRule.OR;
        market.addLogicalDiscount("ali",0,role2,1);
        market.addStoreDiscountPolicy(true, 0, 0, 0.1, 0,  "ali",4);
        market.addToCart("ali", 0, 0, 5);
        return market.Buy("ali","dollar","123",5,2027,"123","Ab2","city","country",434);
    }

    @Override
    public double testComplexPurchasePolicySuccess(String username) throws Exception {
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
        market.addLogicalPurchase("ali",0,role1,0);
        market.addProductPurchasePolicy(3,-1,null,0,-1,18,0,"ali",0,true,1);
        market.addCategoryPurchasePolicy(5,-1,null,0,-1,18,0,"ali",0,true,1);
        PurchasePolicyController.LogicalRule role2=PurchasePolicyController.LogicalRule.OR;
        market.addLogicalPurchase("ali",0,role2,1);
        market.addUserPurchasePolicy(3,-1,null,0,-1,18,19,"ali",0,true,4);
        market.addToCart("ali", 0, 0, 5);
        return market.Buy("ali","dollar","123",5,2027,"123","Ab2","city","country",434);
    }

    @Override
    public String testLogout(String username) {
        try {
            userController.getRegUserMap().put(u3.getUsername(), u3);
            u3.setLoggedIn(true);
            return userController.Logout(username);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
        return "";
    }

    @Override
    public String testInitStore(String userName, String Description) {
        try {
            market.Register(userName, "123", 18);
            return market.initStore(userName,"name", Description);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        return "";
    }

    @Override
    public String testAddProduct(int productId, int storeId, double price, int quantity, String username, double weight) {
        storeController.GetStores().put(0, s1);
        try {
            return storeController.addProduct(productId, storeId, price, quantity, weight);
        } catch (Exception e) {
            fail("Exception should not be thrown when adding product to an existing store");
        }
        return "";
    }

    @Override
    public String testRemoveProduct(int productId, int storeId, String username) throws Exception {
        storeController.GetStores().put(s1.getId(), s1);
        storeController.GetStores().put(s2.getId(), s2);
        s1.AddProduct(p1.getId(), 10.0, 5, 5);
        s1.AddProduct(p2.getId(), 20.0, 10, 5);
        try {
            return storeController.removeProduct(productId, storeId);
        } catch (Exception e) {
            fail("Exception should not be thrown when removing product from an existing store");
        }
        return "";
    }


    @Override
    public String testEditProductPrice(int productId, int storeId, Double newPrice, String username) throws Exception {
        storeController.GetStores().put(s1.getId(), s1);
        storeController.GetStores().put(s2.getId(), s2);
        s1.AddProduct(p1.getId(), 10.0, 5, 5);
        s1.AddProduct(p2.getId(), 20.0, 10, 5);
        return storeController.EditProducPrice(productId, storeId, newPrice);
    }

    @Override
    public String testEditProductQuantity(int productId, int storeId, int newQuantity, String username) throws Exception {
        storeController.GetStores().put(s1.getId(), s1);
        storeController.GetStores().put(s2.getId(), s2);
        s1.AddProduct(p1.getId(), 10.0, 5, 5);
        s1.AddProduct(p2.getId(), 20.0, 10, 5);
        return storeController.EditProductQuantity(productId, storeId, newQuantity);
    }

    @Override
    public String testAssignStoreOwner(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
        market.Register("ali", "123", 18);
        market.Register("malek", "456", 18);
        market.initStore(u2.getUsername(),"name", "description");
        return market.AssignStoreOwner(storeId, ownerUserName, username, pType);
    }

    @Override
    public String testAssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
        market.Register("ali", "123", 18);
        market.Register("malek", "456", 18);
        market.initStore(u2.getUsername(),"name", "description");
        return market.AssignStoreManager(storeId, ownerUserName, username, pType);
    }

    @Override
    public String testEditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception {
        market.Register("ali", "123", 18);
        market.Register("malek", "456", 18);
        market.initStore(u2.getUsername(),"name", "description");
        Boolean[] per = new Boolean[]{true, true, true};
        String result = market.AssignStoreOwner(0, u2.getUsername(), u3.getUsername(), per);
        return market.EditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
    }

    @Override
    public String testCloseStore(int storeId, String username) throws Exception {
        storeController.GetStores().put(storeId, s1);
        storeController.getStore(0).OpenStore();
        return storeController.closeStore(storeId);
    }

    @Override
    public String testViewSystemPurchaseHistory(String username) throws Exception {
        userController.getGuestUserMap().put(username, u2);
        storeController.GetStores().put(0, s1);
        storeController.addProduct(0, 0, 10, 15, 5);
        u2.addToCart(0, 0, 5);
        market.getSystemManagers().add("ali");
        double sum = userController.Buy(username);
        return market.viewSystemPurchaseHistory(username);
    }

    @Override
    public double testUseCase1() throws Exception {
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
            return 0;
        }catch (Exception e){
            return -1;
        }
    }

    @Override
    public String testUseCase2() throws Exception {
        try{
            String systemManager = "admin";
            market.addToSystemManagers(systemManager);
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
                return null;
            }catch (Exception ex){
            }
            String res1=market.viewSuspended(systemManager);
            if (!res1.contains("u2")){
                return null;
            }
            market.resumeUser(systemManager,"u2");
            String res2=market.addToCart("u2",0,0,5);
            if (!res2.equals("added to cart")){
                return null;
            }
            return "useCase passed Successfully";
        }catch (Exception e){
        }
        return null;
    }

    public boolean paymentApi() {
        return false;
    }

    public boolean supplyApi() {
        return false;
    }

    public boolean shippingApi() {
        return false;
    }
}
