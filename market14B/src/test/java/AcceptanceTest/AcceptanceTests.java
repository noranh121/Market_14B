package AcceptanceTest;

import AcceptanceTest.Util.Bridge;
import AcceptanceTest.Util.Proxy;

public abstract class AcceptanceTests {
    public Bridge bridge = new Proxy();
    public void testSetMarketOnline(String username) throws Exception {
        bridge.testSetMarketOnline(username);
    }

    public String testEnterAsGuest() throws Exception {
        return bridge.testEnterAsGuest();
    }

    public String testGuestExit(String username) throws Exception {
        return bridge.testGuestExit(username);
    }

    public String testRegister(String username, String password) throws Exception {
        return bridge.testRegister(username, password);
    }

    public String testLogin(String username, String password) throws Exception {
        return bridge.testLogin(username, password);
    }

    public String testGetInfo(int storeId, String username) throws Exception {
        return bridge.testGetInfo(storeId, username);
    }

    public String testAddToCart(String username, Integer product, int storeId, int quantity) {
        return bridge.testAddToCart(username, product, storeId, quantity);
    }

    public String testInspectCart(String username) {
        return bridge.testInspectCart(username);
    }

    public double testBuy(String username) throws Exception {
        return bridge.testBuy(username);
    }

    public String testLogout(String username) {
        return bridge.testLogout(username);
    }

    public String testInitStore(String userName, String Description) {
        return bridge.testInitStore(userName, Description);
    }

    public String testAddProduct(int productId, int storeId, double price, int quantity, String username) {
        return bridge.testAddProduct(productId, storeId, price, quantity, username);
    }

    public String testRemoveProduct(int productId, int storeId, String username) throws Exception {
        return bridge.testRemoveProduct(productId, storeId, username);
    }

    public String testEditProductPrice(int productId, int storeId, Double newPrice, String username) throws Exception {
        return bridge.testEditProductPrice(productId, storeId, newPrice, username);
    }

    public String testEditProductQuantity(int productId, int storeId, int newQuantity, String username) throws Exception {
        return bridge.testEditProductQuantity(productId, storeId, newQuantity, username);
    }

    public String testAssignStoreOwner(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
        return bridge.testAssignStoreOwner(storeId, ownerUserName, username, pType);
    }


    public String testAssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
        return bridge.testAssignStoreManager(storeId, ownerUserName, username, pType);
    }

    public String testEditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception {
        return bridge.testEditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
    }

    public String testCloseStore(int storeId, String username) {
        return bridge.testCloseStore(storeId, username);
    }

    public String testViewSystemPurchaseHistory(String username) throws Exception {
        return bridge.testViewSystemPurchaseHistory(username);
    }
}
