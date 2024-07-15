package AcceptanceTest;
import org.market.Application;
import org.market.DomainLayer.backend.Market;
import org.market.DomainLayer.backend.StorePackage.StoreController;
import org.market.DomainLayer.backend.UserPackage.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.yaml.snakeyaml.error.Mark;

import AcceptanceTest.Util.Real;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.market.DomainLayer.backend.UserPackage.UserController.notfications;


@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class AccTestImpl extends AcceptanceTests{
    @Autowired
    public ApplicationContext context;
    public static Market market;
    
    @BeforeEach
    void setUp(){
        market=(Market)context.getBean(Market.class);
        Real.beforeEach();
    }
    
    @AfterEach
    void tearDown() {
        Market.clear();
        Market.getDC().clearAll();
    }
    @Test
    void testSetMarketOnlineSuccess() throws Exception {
        testSetMarketOnline("ali");
        assertTrue(market.getOnline());
    }

    @Test
    void testSetMarketOnlineFail() throws Exception {
        try {
            testSetMarketOnline("bob");
        }catch (Exception e) {
            assertEquals(e.getMessage(),"only system managers can change market's activity");
        }
    }

    @Test
    void testEnterAsGuestSuccess() throws Exception {
        String res=testEnterAsGuest();
        assertEquals(res,"0");
    }

    @Test
    void testGuestExitSuccess() throws Exception {
        String res=testGuestExit("0");
        assertEquals(res,"guest existed successfully");
    }

    @Test
    void testRegisterSuccess() throws Exception {
        String res=testRegister("u","1",18);
        assertEquals(res,"User registered successfully");
    }

    @Test
    void testRegisterFail() throws Exception {
        try {
            String res = testRegister("u", "1", 18);
            assertEquals(res,"User registered successfully");
            String res1 = testRegister("u", "1", 18);
        }catch (Exception e) {
            assertEquals(e.getMessage(),"username already exists");
        }
    }

    @Test
    void testLoginSuccess() throws Exception {
        String res=testLogin("u","1");
        assertEquals(res,"logged in successfully");
    }

    @Test
    void testLoginFail() throws Exception {
        try {
            String res = testLogin("u", "1");
            assertEquals(res,"logged in successfully");
            String res1 = testLogin("u", "1");
        }catch (Exception e) {
            assertEquals(e.getMessage(), "username already exists");
        }
    }

    @Test
    void testGetInfoSuccess() throws Exception {
        String res=testGetInfo(0,"ali");
        assertTrue(res.contains("Product ID: 0"));
        assertTrue(res.contains(", Quantity: 5"));
    }

    @Test
    void testAddToCartSuccess() throws Exception {
        String res=testAddToCart("ali",0,0,5);
        assertNotNull(res);
        assertEquals(res,"added to cart");
    }

    @Test
    void testInspectCartSuccess(){
        String res=testInspectCart("malek");
        assertEquals(res,"");
    }

    @Test
    void testBuySuccess() throws Exception {
        double res=testBuy("ali");
        assertEquals(res,50);
    }

    @Test
    void testBuyNotEnoughSupply() throws Exception {
        try {
            double res=testBuyNotEnoughSupply("ali");
        }catch (Exception e) {
            assertEquals(e.getMessage(),"invalid cart");
            assertEquals(Market.getSC().getStore(0).getInventory().getQuantity(0),15);
            assertEquals(Market.getUC().getUser("ali").getShoppingCart().getBasket("ali", 0).getProducts().size(),1);
        }
    }

    @Test
    void testBuySupplyFail() throws Exception {
        try {
            double res=testBuySupplyFail("ali");
        }catch (Exception e) {
            assertEquals(e.getMessage(),"Supply fail");
            assertEquals(Market.getSC().getStore(0).getInventory().getQuantity(0),15);
            assertEquals(Market.getUC().getUser("ali").getShoppingCart().getBasket("ali", 0).getProducts().size(),1);
        }
    }

    @Test
    void testBuyPaymentFail() throws Exception {
        try {
            double res=testBuyPaymentFail("ali");
        }catch (Exception e) {
            assertEquals(e.getMessage(),"payment fail");
            assertEquals(Market.getSC().getStore(0).getInventory().getQuantity(0),15);
            assertEquals(Market.getUC().getUser("ali").getShoppingCart().getBasket("ali", 0).getProducts().size(),1);
        }
    }

    @Test
    void testBuyShippingFail() throws Exception {
        try {
            double res=testBuyShippingFail("ali");
        }catch (Exception e) {
            assertEquals(e.getMessage(),"shipping fail");
        }
    }

    @Test
    void testLogoutSuccess(){
        String res=testLogout("malek");
        assertEquals("0", res);
    }

    @Test
    void testLogoutFail(){
        try {
            String res = testLogout("malek");
        }catch (Exception e) {
            assertTrue(e.getMessage().contains("null"));
        }
    }

    @Test
    void testInitStoreSuccess(){
        String result=testInitStore("ali","desc");
        assertEquals("store added successfully", result);
    }

    @Test
    void testAddProductSuccess(){
        String res=testAddProduct(0,0,10,0,"ali",5);
        assertEquals("Product added to store Successfully", res);
    }

    @Test
    void testAddProductFail(){
        String res=testAddProduct(5,10,10,0,"ali",5);
        assertEquals("store does not exist", res);
    }

    @Test
    void testRemoveProductSuccess() throws Exception {
        String res=testRemoveProduct(0,0,"ali");
        assertEquals("Product Removed from store Successfully", res);
    }


    @Test
    void testEditProductPriceSuccess() throws Exception {
        String res=testEditProductPrice(0,0,55.0,"ali");
        assertEquals("Product's Price Modified in store Successfully", res);
    }

    @Test
    void testEditProductPriceFail() throws Exception {
        String res=testEditProductPrice(0,15,55.0,"ali");
        assertEquals(Market.getSC().getStore(0).getInventory().getPrice(0),20);
    }

    @Test
    void testEditProductQuantitySuccess() throws Exception {
        String res=testEditProductQuantity(0,0,20,"ali");
        assertEquals("Product's Quantity Modified in store Successfully", res);
    }

    @Test
    void testEditProductQuantityFail() throws Exception {
        String res=testEditProductQuantity(0,20,55,"ali");
        assertEquals(Market.getSC().getStore(0).getInventory().getQuantity(0),10);
    }

    @Test
    void testAssignStoreOwnerSuccess() throws Exception {
        Boolean[] per=new Boolean[]{true,true,true};
        String res=testAssignStoreOwner(0,"ali","malek",per);
        assertEquals("Permission added to store",res);
    }

    @Test
    void testAssignStoreManagerSuccess() throws Exception {
        Boolean[] per=new Boolean[]{true,true,true};
        String res=testAssignStoreManager(0,"ali","malek",per);
        assertEquals("Permission added to store",res);
    }

    @Test
    void testAssignStoreManagerFail() throws Exception {
        try {
            Boolean[] per=new Boolean[]{true,true,true};
            String res=testAssignStoreManager(0,"bob","malek",per);
        }catch (Exception e) {
            assertEquals(e.getMessage(),"ownerUserName not found");
        }
    }

    @Test
    void testEditPermissionsSuccess() throws Exception {
        Boolean[] per=new Boolean[]{true,false,true};
        String res=testEditPermissions(0,"ali","malek",true,false,per);
        assertEquals("Permission added to store",res);
    }

    @Test
    void testEditPermissionsFail() throws Exception {
        try {
            Boolean[] per=new Boolean[]{true,true,true};
            String res=testEditPermissions(0,"bob","malek",true,false,per);
        }catch (Exception e) {
            assertEquals(e.getMessage(),"ownerUserName not found");
        }
    }

    @Test
    void testCloseStoreSuccess() throws Exception{
        String res=testCloseStore(0,"ali");
        assertEquals("Store Closed Successfuly", res);
    }

    @Test
    void testCloseStoreFail() throws Exception{
        String res=testCloseStore(0,"ali");
        assertFalse(Market.getSC().getStore(0).isActive());
    }

    @Test
    void testViewSystemPurchaseHistorySuccess() throws Exception {
        String res=testViewSystemPurchaseHistory("ali");
        assertTrue(res.contains("Username: ali"));
        assertTrue(res.contains("Store ID: 0"));
        assertTrue(res.contains("Overall Price: 50"));
    }

    @Test
    void testProductDiscountPolicySuccessImpl() throws Exception{
        double res=testProductDiscountPolicySuccess("ali");
        assertEquals(res,45);
        assertEquals(notfications.size(),1);
        assertEquals(notfications.get(0)[0],"ali");
        assertEquals(notfications.get(0)[1],"Your purchase was successful");
    }

    @Test
    void testANDDiscountPolicySuccessImpl() throws Exception{
        double res=testANDDiscountPolicySuccess("ali");
        assertEquals(res,36);
        assertEquals(notfications.size(),1);
        assertEquals(notfications.get(0)[0],"ali");
        assertEquals(notfications.get(0)[1],"Your purchase was successful");
    }

    @Test
    void testORDiscountPolicySuccessImpl() throws Exception{
        double res=testORDiscountPolicySuccess("ali");
        assertEquals(res,36);
        assertEquals(notfications.size(),1);
        assertEquals(notfications.get(0)[0],"ali");
        assertEquals(notfications.get(0)[1],"Your purchase was successful");
    }

    @Test
    void testXORDiscountPolicySuccessImpl() throws Exception{
        double res=testXORDiscountPolicySuccess("ali");
        assertEquals(res,45);
        assertEquals(notfications.size(),1);
        assertEquals(notfications.get(0)[0],"ali");
        assertEquals(notfications.get(0)[1],"Your purchase was successful");
    }

    @Test
    void testProductPurchasePolicySuccessImpl() throws Exception{
        double res=testProductPurchasePolicySuccess("ali");
        assertEquals(res,-1);
    }

    @Test
    void testANDProductPurchasePolicySuccessImpl() throws Exception{
        double res=testANDProductPurchasePolicySuccess("ali");
        assertEquals(res,40);
        assertEquals(notfications.size(),1);
        assertEquals(notfications.get(0)[0],"ali");
        assertEquals(notfications.get(0)[1],"Your purchase was successful");
    }

    @Test
    void testANDProductPurchasePolicyFailImpl() throws Exception{
        double res=testANDProductPurchasePolicyFail("ali");
        assertEquals(res,-1);
    }

    @Test
    void testORProductPurchasePolicySuccessImpl() throws Exception{
        double res=testORProductPurchasePolicySuccess("ali");
        assertEquals(res,45);
        assertEquals(notfications.size(),1);
        assertEquals(notfications.get(0)[0],"ali");
        assertEquals(notfications.get(0)[1],"Your purchase was successful");
    }

    @Test
    void testORProductPurchasePolicyFailImpl() throws Exception{
        double res=testORProductPurchasePolicyFail("ali");
        assertEquals(res,-1);
    }

    @Test
    void testComplexDiscountPolicySuccessImpl() throws Exception{
        double res=testComplexDiscountPolicySuccess("ali");
        assertEquals(res,32.400000000000006);
        assertEquals(notfications.size(),1);
        assertEquals(notfications.get(0)[0],"ali");
        assertEquals(notfications.get(0)[1],"Your purchase was successful");
    }

    @Test
    void testComplexPurchasePolicySuccessImpl() throws Exception{
        double res=testComplexPurchasePolicySuccess("ali");
        assertEquals(res,50);
        assertEquals(notfications.size(),1);
        assertEquals(notfications.get(0)[0],"ali");
        assertEquals(notfications.get(0)[1],"Your purchase was successful");
    }

    @Test
    void testUseCase1Impl() throws Exception {
        double res=testUseCase1();
        assertEquals(-1,res);
    }

    @Test
    void testUseCase2Impl() throws Exception{
        String res=testUseCase2();
        assertEquals(res,"useCase passed Successfully");
    }

}
