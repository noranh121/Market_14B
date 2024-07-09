package AcceptanceTest.Util;

public interface Bridge {
    void testSetMarketOnline(String username) throws Exception;
    String testEnterAsGuest() throws Exception;
    String testGuestExit(String username) throws Exception;
    String testRegister(String username, String password,int age) throws Exception;
    String testLogin(String username, String password) throws Exception;
    String testGetInfo(int storeId,String username) throws Exception;
    String testAddToCart(String username, Integer product, int storeId, int quantity) throws Exception;
    String testInspectCart(String username);
    double testBuy(String username) throws Exception;
    double testBuyNotEnoughSupply(String username) throws Exception;
    double testBuySupplyFail(String username) throws Exception;
    double testBuyPaymentFail(String username) throws Exception;
    double testBuyShippingFail(String username) throws Exception;
    double testProductDiscountPolicySuccess(String username) throws Exception;
    double testANDDiscountPolicySuccess (String username) throws Exception;
    double testORDiscountPolicySuccess (String username) throws Exception;
    double testXORDiscountPolicySuccess (String username) throws Exception;
    double testProductPurchasePolicySuccess (String username) throws Exception;
    double testANDProductPurchasePolicySuccess (String username) throws Exception;
    double testANDProductPurchasePolicyFail (String username) throws Exception;
    double testORProductPurchasePolicySuccess (String username) throws Exception;
    double testORProductPurchasePolicyFail (String username) throws Exception;
    double testComplexDiscountPolicySuccess (String username) throws Exception;
    double testComplexPurchasePolicySuccess (String username) throws Exception;
    String testLogout(String username);
    String testInitStore(String userName, String Description);
    String testAddProduct(int productId,int storeId,double price,int quantity,String username,double weight);
    String testRemoveProduct(int productId,int storeId,String username) throws Exception;
    String testEditProductPrice(int productId,int storeId,Double newPrice,String username) throws Exception;
    String testEditProductQuantity(int productId,int storeId,int newQuantity,String username) throws Exception;
    String testAssignStoreOwner(int storeId,String ownerUserName,String username,Boolean[] pType) throws Exception;
    String testAssignStoreManager(int storeId,String ownerUserName,String username,Boolean[] pType) throws Exception;
    String testEditPermissions(int storeID,String ownerUserName, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception;
    String testCloseStore(int storeId,String username);
    String testViewSystemPurchaseHistory(String username) throws Exception;
}
