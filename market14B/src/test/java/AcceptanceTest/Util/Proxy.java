package AcceptanceTest.Util;

import org.market.DomainLayer.backend.Market;
import org.springframework.context.ApplicationContext;

public class Proxy implements Bridge{
    public Bridge real=new Real();
    @Override
    public void testSetMarketOnline(String username) throws Exception {
        real.testSetMarketOnline(username);
    }

    @Override
    public String testEnterAsGuest() throws Exception {
        return real.testEnterAsGuest();
    }

    @Override
    public String testGuestExit(String username) throws Exception {
        return real.testGuestExit(username);
    }

    @Override
    public String testRegister(String username, String password,int age) throws Exception {
        return real.testRegister(username, password,age);
    }

    @Override
    public String testLogin(String username, String password) throws Exception {
        return real.testLogin(username, password);
    }

    @Override
    public String testGetInfo(int storeId, String username) throws Exception {
        return real.testGetInfo(storeId, username);
    }

    @Override
    public String testAddToCart(String username, Integer product, int storeId, int quantity) throws Exception {
        return real.testAddToCart(username, product, storeId, quantity);
    }

    @Override
    public String testInspectCart(String username) {
        return real.testInspectCart(username);
    }

    @Override
    public double testBuy(String username) throws Exception {
        return real.testBuy(username);
    }

    @Override
    public double testBuyNotEnoughSupply(String username) throws Exception {
        return real.testBuyNotEnoughSupply(username);
    }

    @Override
    public double testBuySupplyFail(String username) throws Exception {
        return real.testBuySupplyFail(username);
    }

    @Override
    public double testBuyPaymentFail(String username) throws Exception {
        return real.testBuyPaymentFail(username);
    }

    @Override
    public double testBuyShippingFail(String username) throws Exception {
        return real.testBuyShippingFail(username);
    }

    @Override
    public double testProductDiscountPolicySuccess(String username) throws Exception {
        return real.testProductDiscountPolicySuccess(username);
    }

    @Override
    public double testANDDiscountPolicySuccess(String username) throws Exception {
        return real.testANDDiscountPolicySuccess(username);
    }

    @Override
    public double testORDiscountPolicySuccess(String username) throws Exception {
        return real.testORDiscountPolicySuccess(username);
    }

    @Override
    public double testXORDiscountPolicySuccess(String username) throws Exception {
        return real.testXORDiscountPolicySuccess(username);
    }

    @Override
    public double testProductPurchasePolicySuccess(String username) throws Exception {
        return real.testProductPurchasePolicySuccess(username);
    }

    @Override
    public double testANDProductPurchasePolicySuccess(String username) throws Exception {
        return real.testANDProductPurchasePolicySuccess(username);
    }

    @Override
    public double testANDProductPurchasePolicyFail(String username) throws Exception {
        return real.testANDProductPurchasePolicyFail(username);
    }

    @Override
    public double testORProductPurchasePolicySuccess(String username) throws Exception {
        return real.testORProductPurchasePolicySuccess(username);
    }

    @Override
    public double testORProductPurchasePolicyFail(String username) throws Exception {
        return real.testORProductPurchasePolicyFail(username);
    }

    @Override
    public double testComplexDiscountPolicySuccess(String username) throws Exception {
        return real.testComplexDiscountPolicySuccess(username);
    }

    @Override
    public double testComplexPurchasePolicySuccess(String username) throws Exception {
        return real.testComplexPurchasePolicySuccess(username);
    }

    @Override
    public String testLogout(String username) {
        return real.testLogout(username);
    }

    @Override
    public String testInitStore(String userName, String Description) {
        return real.testInitStore(userName, Description);
    }

    @Override
    public String testAddProduct(int productId, int storeId, double price, int quantity, String username,double weight) {
        return real.testAddProduct(productId, storeId, price, quantity, username,weight);
    }

    @Override
    public String testRemoveProduct(int productId, int storeId, String username) throws Exception {
        return real.testRemoveProduct(productId, storeId, username);
    }


    @Override
    public String testEditProductPrice(int productId, int storeId, Double newPrice, String username) throws Exception {
        return real.testEditProductPrice(productId, storeId, newPrice, username);
    }

    @Override
    public String testEditProductQuantity(int productId, int storeId, int newQuantity, String username) throws Exception {
        return real.testEditProductQuantity(productId, storeId, newQuantity, username);
    }

    @Override
    public String testAssignStoreOwner(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
        return real.testAssignStoreOwner(storeId, ownerUserName, username, pType);
    }

    @Override
    public String testAssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
        return real.testAssignStoreManager(storeId, ownerUserName, username, pType);
    }

    @Override
    public String testEditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception {
        return real.testEditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
    }

    @Override
    public String testCloseStore(int storeId, String username) throws Exception {
        return real.testCloseStore(storeId, username);
    }

    @Override
    public String testViewSystemPurchaseHistory(String username) throws Exception {
        return real.testViewSystemPurchaseHistory(username);
    }

    @Override
    public double testUseCase1() throws Exception {
        return real.testUseCase1();
    }

    @Override
    public String testUseCase2() throws Exception {
        return real.testUseCase2();
    }
}
