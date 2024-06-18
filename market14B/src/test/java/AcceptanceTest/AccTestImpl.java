package AcceptanceTest;
import DomainLayer.backend.Market;
import DomainLayer.backend.UserPackage.UserController;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class AccTestImpl extends AcceptanceTests{

    @AfterEach
    void tearDown() {
        Market.getInstance().setToNull();
    }
    @Test
    void testSetMarketOnline1() throws Exception {
        testSetMarketOnline("ali");
        assertTrue(Market.getInstance().getOnline());
    }

    @Test
    void testEnterAsGuest1() throws Exception {
        String res=testEnterAsGuest();
        assertEquals(res,"guest user added successfully");
    }

    @Test
    void testGuestExit1() throws Exception {
        String res=testGuestExit("0");
        assertEquals(res,"guest existed successfully");
    }

    @Test
    void testRegister1() throws Exception {
        String res=testRegister("u","1",18);
        assertEquals(res,"guest user added successfully");
    }

    @Test
    void testLogin1() throws Exception {
        String res=testLogin("u","1");
        assertEquals(res,"logged in successfully");
    }

    @Test
    void testGetInfo1() throws Exception {
        String res=testGetInfo(0,"ali");
        assertTrue(res.contains("Product ID: 0"));
        assertTrue(res.contains(", Quantity: 5"));
    }

    @Test
    void testAddToCart1() throws Exception {
        String res=testAddToCart("ali",0,0,5);
        assertNotNull(res);
        assertEquals(res,"added to cart");
    }

    @Test
    void testInspectCart1(){
        String res=testInspectCart("malek");
        assertEquals(res,"Your shopping cart is empty.");
    }

    @Test
    void testBuy1() throws Exception {
        double res=testBuy("ali");
        assertEquals(res,50);
    }

    @Test
    void testLogout1(){
        String res=testLogout("malek");
        assertEquals("guest user added successfully", res);
    }

    @Test
    void testInitStore1(){
        String result=testInitStore("ali","desc");
        assertEquals("store added successfully", result);
    }

    @Test
    void testAddProduct1(){
        String res=testAddProduct(0,0,10,0,"ali",5);
        assertEquals("Product added to store Successfully", res);
    }

    @Test
    void testRemoveProduct1() throws Exception {
        String res=testRemoveProduct(0,0,"ali");
        assertEquals("Product Removed from store Successfully", res);
    }

    @Test
    void testEditProductPrice1() throws Exception {
        String res=testEditProductPrice(0,0,55.0,"ali");
        assertEquals("Product's Price Modified in store Successfully", res);
    }

    @Test
    void testEditProductQuantity1() throws Exception {
        String res=testEditProductQuantity(0,0,20,"ali");
        assertEquals("Product's Quantity Modified in store Successfully", res);
    }

    @Test
    void testAssignStoreOwner1() throws Exception {
        Boolean[] per=new Boolean[]{true,true,true};
        String res=testAssignStoreOwner(0,"ali","malek",per);
        assertEquals("Permission added to store",res);
    }

    @Test
    void testAssignStoreManager1() throws Exception {
        Boolean[] per=new Boolean[]{true,true,true};
        String res=testAssignStoreManager(0,"ali","malek",per);
        assertEquals("Permission added to store",res);
    }

    @Test
    void testEditPermissions1() throws Exception {
        Boolean[] per=new Boolean[]{true,false,true};
        String res=testEditPermissions(0,"ali","malek",true,false,per);
        assertEquals("Permission added to store",res);
    }

    @Test
    void testCloseStore1(){
        String res=testCloseStore(0,"ali");
        assertEquals("Store Closed Successfuly", res);
    }

    @Test
    void testViewSystemPurchaseHistory1() throws Exception {
        String res=testViewSystemPurchaseHistory("ali");
        assertTrue(res.contains("Username: ali"));
        assertTrue(res.contains("Store ID: 0"));
        assertTrue(res.contains("Overall Price: 50"));
    }
}
