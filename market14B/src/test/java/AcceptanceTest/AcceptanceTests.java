// package AcceptanceTest;

// import AcceptanceTest.Util.Bridge;
// import AcceptanceTest.Util.Proxy;

// public abstract class AcceptanceTests {
//     public Bridge bridge = new Proxy();
//     public void testSetMarketOnline(String username) throws Exception {
//         bridge.testSetMarketOnline(username);
//     }

//     public String testEnterAsGuest() throws Exception {
//         return bridge.testEnterAsGuest();
//     }

//     public String testGuestExit(String username) throws Exception {
//         return bridge.testGuestExit(username);
//     }

//     public String testRegister(String username, String password,int age) throws Exception {
//         return bridge.testRegister(username, password,age);
//     }

//     public String testLogin(String username, String password) throws Exception {
//         return bridge.testLogin(username, password);
//     }

//     public String testGetInfo(int storeId, String username) throws Exception {
//         return bridge.testGetInfo(storeId, username);
//     }

//     public String testAddToCart(String username, Integer product, int storeId, int quantity) throws Exception {
//         return bridge.testAddToCart(username, product, storeId, quantity);
//     }

//     public String testInspectCart(String username) {
//         return bridge.testInspectCart(username);
//     }

//     public double testBuy(String username) throws Exception {
//         return bridge.testBuy(username);
//     }

//     public double testBuyNotEnoughSupply(String username) throws Exception {
//         return bridge.testBuyNotEnoughSupply(username);
//     }

//     public double testBuySupplyFail(String username) throws Exception {
//         return bridge.testBuySupplyFail(username);
//     }

//     public double testBuyPaymentFail(String username) throws Exception {
//         return bridge.testBuyPaymentFail(username);
//     }

//     public double testBuyShippingFail(String username) throws Exception {
//         return bridge.testBuyShippingFail(username);
//     }

//     public double testProductDiscountPolicySuccess(String username) throws Exception{
//         return bridge.testProductDiscountPolicySuccess(username);
//     }

//     public double testANDDiscountPolicySuccess(String username) throws Exception{
//         return bridge.testANDDiscountPolicySuccess(username);
//     }

//     public double testORDiscountPolicySuccess(String username) throws Exception{
//         return bridge.testORDiscountPolicySuccess(username);
//     }

//     public double testXORDiscountPolicySuccess(String username) throws Exception{
//         return bridge.testXORDiscountPolicySuccess(username);
//     }

//     public double testProductPurchasePolicySuccess(String username) throws Exception{
//         return bridge.testProductPurchasePolicySuccess(username);
//     }

//     public double testANDProductPurchasePolicySuccess(String username) throws Exception{
//         return bridge.testANDProductPurchasePolicySuccess(username);
//     }

//     public double testANDProductPurchasePolicyFail(String username) throws Exception{
//         return bridge.testANDProductPurchasePolicyFail(username);
//     }

//     public double testORProductPurchasePolicySuccess(String username) throws Exception{
//         return bridge.testProductDiscountPolicySuccess(username);
//     }

//     public double testORProductPurchasePolicyFail(String username) throws Exception{
//         return bridge.testORProductPurchasePolicyFail(username);
//     }

//     public double testComplexDiscountPolicySuccess(String username) throws Exception{
//         return bridge.testComplexDiscountPolicySuccess(username);
//     }

//     public double testComplexPurchasePolicySuccess(String username) throws Exception{
//         return bridge.testComplexPurchasePolicySuccess(username);
//     }

//     public String testLogout(String username) {
//         return bridge.testLogout(username);
//     }

//     public String testInitStore(String userName, String Description) {
//         return bridge.testInitStore(userName, Description);
//     }

//     public String testAddProduct(int productId, int storeId, double price, int quantity, String username,double weight) {
//         return bridge.testAddProduct(productId, storeId, price, quantity, username,weight);
//     }

//     public String testRemoveProduct(int productId, int storeId, String username) throws Exception {
//         return bridge.testRemoveProduct(productId, storeId, username);
//     }

//     public String testEditProductPrice(int productId, int storeId, Double newPrice, String username) throws Exception {
//         return bridge.testEditProductPrice(productId, storeId, newPrice, username);
//     }

//     public String testEditProductQuantity(int productId, int storeId, int newQuantity, String username) throws Exception {
//         return bridge.testEditProductQuantity(productId, storeId, newQuantity, username);
//     }

//     public String testAssignStoreOwner(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
//         return bridge.testAssignStoreOwner(storeId, ownerUserName, username, pType);
//     }


//     public String testAssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
//         return bridge.testAssignStoreManager(storeId, ownerUserName, username, pType);
//     }

//     public String testEditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception {
//         return bridge.testEditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
//     }

//     public String testCloseStore(int storeId, String username) {
//         return bridge.testCloseStore(storeId, username);
//     }

//     public String testViewSystemPurchaseHistory(String username) throws Exception {
//         return bridge.testViewSystemPurchaseHistory(username);
//     }

//     public double testUseCase1() throws Exception {
//         return bridge.testUseCase1();
//     }

//     public String testUseCase2() throws Exception {
//         return bridge.testUseCase2();
//     }
// }
