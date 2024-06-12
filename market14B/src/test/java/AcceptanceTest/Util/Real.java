package AcceptanceTest.Util;
import DomainLayer.backend.Market;
import DomainLayer.backend.ProductPackage.Category;
import DomainLayer.backend.ProductPackage.Product;
import DomainLayer.backend.StorePackage.Store;
import DomainLayer.backend.StorePackage.StoreController;
import DomainLayer.backend.UserPackage.GuestUser;
import DomainLayer.backend.UserPackage.RegisteredUser;
import DomainLayer.backend.UserPackage.User;
import DomainLayer.backend.UserPackage.UserController;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class Real implements Bridge{
    Market market=Market.getInstance();
    StoreController storeController=StoreController.getInstance();
    UserController userController=UserController.getInstance();
    Store s1=new Store("store1","decs1",0);
    Store s2=new Store("store2","decs2",1);
    User u2=new RegisteredUser("ali","123");
    User u3=new RegisteredUser("malek","456");
    Category c1=new Category(0,"c1");
    Category c2=new Category(1,"c2");
    Product p1=new Product("product1","desc1","brand1",c1);
    Product p2=new Product("product2","desc2","brand2",c2);


//    @BeforeEach
//    void setup(){
//        market=Market.getInstance();
//        storeController=StoreController.getInstance();
//        userController=UserController.getInstance();
//        s1=new Store("store1","decs1",0);
//        s2=new Store("store2","decs2",1);
//        u2=new RegisteredUser("ali","123");
//        u3=new RegisteredUser("malek","456");
//        c1=new Category(0,"c1");
//        c2=new Category(1,"c2");
//        p1=new Product("product1","desc1","brand1",c1);
//        p2=new Product("product2","desc2","brand2",c2);
//    }
    @Override
    public void testSetMarketOnline(String username) throws Exception {
        userController.getRegUserMap().put(u2.getUsername(),u2);
        market.getSystemManagers().add(u2.getUsername());
        market.setMarketOnline(username);
    }

    @Override
    public String testEnterAsGuest() throws Exception {
        return market.EnterAsGuest();
    }

    @Override
    public String testGuestExit(String username) throws Exception {
        market.EnterAsGuest();
        return market.GuestExit("0");
    }

    @Override
    public String testRegister(String username, String password) throws Exception {
        return market.Register(username,password);
    }

    @Override
    public String testLogin(String username, String password) throws Exception {
        market.EnterAsGuest();
        market.Register(username,password);
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        return market.Login("0",username,password);
    }

    @Override
    public String testGetInfo(int storeId, String username) throws Exception {
        market.Register(username,"12");
        market.initStore(username,"d");
        market.addProduct(0,0,10,5,username);
        return market.getInfo(storeId,username);
    }

    @Override
    public String testAddToCart(String username, Integer product, int storeId, int quantity) throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        market.EnterAsGuest();
        market.EnterAsGuest();
        market.Register("u","1");
        market.Register(username,"123");
        market.Login("0","u","1");
        market.Login("1",username,"123");
        market.initStore("u","d");
        market.addProduct(0,0,10,5,"u");
        return market.addToCart(username,product,storeId,quantity);
    }

    @Override
    public String testInspectCart(String username) {
        userController.getRegUserMap().put(u3.getUsername(), u3);
        return userController.inspectCart(username);
    }


    @Override
    public double testBuy(String username) throws Exception {
        userController.getGuestUserMap().put(u2.getUsername(),u2);
        storeController.GetStores().put(0,s1);
        storeController.addProduct(0,0,10,15);
        u2.addToCart(0,0,5);
        return userController.Buy(u2.getUsername());
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
            market.Register(userName,"123");
            return market.initStore(userName, Description);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        return "";
    }

    @Override
    public String testAddProduct(int productId, int storeId, double price, int quantity, String username) {
        storeController.GetStores().put(0,s1);
        try {
            return storeController.addProduct(productId, storeId, price, quantity);
        } catch (Exception e) {
            fail("Exception should not be thrown when adding product to an existing store");
        }
        return "";
    }

    @Override
    public String testRemoveProduct(int productId, int storeId, String username) throws Exception {
        storeController.GetStores().put(s1.getId(), s1);
        storeController.GetStores().put(s2.getId(), s2);
        s1.AddProduct(p1.getId(), 10.0, 5);
        s1.AddProduct(p2.getId(), 20.0, 10);
        try {
            return storeController.removeProduct(productId, s1.getId());
        } catch (Exception e) {
            fail("Exception should not be thrown when removing product from an existing store");
        }
        return "";
    }


    @Override
    public String testEditProductPrice(int productId, int storeId, Double newPrice, String username) throws Exception {
        storeController.GetStores().put(s1.getId(), s1);
        storeController.GetStores().put(s2.getId(), s2);
        s1.AddProduct(p1.getId(), 10.0, 5);
        s1.AddProduct(p2.getId(), 20.0, 10);
        return storeController.EditProducPrice(p1.getId(), s1.getId(), newPrice);
    }

    @Override
    public String testEditProductQuantity(int productId, int storeId, int newQuantity, String username) throws Exception {
        storeController.GetStores().put(s1.getId(), s1);
        storeController.GetStores().put(s2.getId(), s2);
        s1.AddProduct(p1.getId(), 10.0, 5);
        s1.AddProduct(p2.getId(), 20.0, 10);
        return storeController.EditProductQuantity(productId,storeId, newQuantity);
    }

    @Override
    public String testAssignStoreOwner(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
        market.Register("ali","123");
        market.Register("malek","456");
        market.initStore(u2.getUsername(),"description");
        return market.AssignStoreOwner(storeId,ownerUserName,username,pType);
    }

    @Override
    public String testAssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
        market.Register("ali","123");
        market.Register("malek","456");
        market.initStore(u2.getUsername(),"description");
        return market.AssignStoreManager(storeId,ownerUserName,username,pType);
    }

    @Override
    public String testEditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception {
        market.Register("ali","123");
        market.Register("malek","456");
        market.initStore(u2.getUsername(),"description");
        Boolean[] per=new Boolean[]{true,true,true};
        String result=market.AssignStoreOwner(0,u2.getUsername(),u3.getUsername(),per);
        try {
            return market.EditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        return "";
    }

    @Override
    public String testCloseStore(int storeId, String username) {
        storeController.GetStores().put(s1.getId(), s1);
        return storeController.closeStore(s1.getId());
    }

    @Override
    public String testViewSystemPurchaseHistory(String username) throws Exception {
        userController.getGuestUserMap().put(u2.getUsername(),u2);
        storeController.GetStores().put(0,s1);
        storeController.addProduct(0,0,10,15);
        u2.addToCart(0,0,5);
        market.getSystemManagers().add("ali");
        double sum = userController.Buy(u2.getUsername());
        return market.viewsystemPurchaseHistory(username);
    }
}
