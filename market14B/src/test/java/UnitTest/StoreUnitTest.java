package UnitTest;
import DomainLayer.backend.ProductPackage.Category;
import DomainLayer.backend.ProductPackage.Product;
import DomainLayer.backend.StorePackage.Store;
import DomainLayer.backend.StorePackage.StoreController;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class StoreUnitTest {
    private StoreController storeController ;
    Store s1;
    Store s2;
    Product p1;
    Product p2;
    Category c1;
    Category c2;
    @BeforeEach
    public void setUp() {
        storeController = StoreController.getInstance();
        s1=new Store("store1","decs1",0);
        s2=new Store("store2","decs2",0);
        c1=new Category(0,"c1");
        c2=new Category(1,"c2");
        p1=new Product("product1","desc1","brand1",c1);
        p2=new Product("product2","desc2","brand2",c2);
    }

    @AfterEach
    void tearDown(){
        storeController.setToNull();
    }

    @Test
    public void testGetStore_StoreExists() {
        storeController.GetStores().put(0,s1);
        storeController.GetStores().put(1,s2);
        Store result = storeController.getStore(s1.getId());
        assertNotNull(result);
        assertEquals(s1, result);
        result = storeController.getStore(s2.getId());
        assertNotNull(result);
        assertEquals(s2, result);
    }

    @Test
    public void testGetStore_StoreDoesNotExist() {
        int nonExistentStoreID = 999;
        Store result = storeController.getStore(nonExistentStoreID);
        assertNull(result);
    }

    @Test
    public void testAddProduct_StoreExists() {
        storeController.GetStores().put(0,s1);
        try {
            String result = storeController.addProduct(p1.getId(), s1.getId(), 10, 15);
            assertEquals("Product added to store successfully", result);
            assertEquals(10, s1.getProdPrice(0));
            assertEquals(15, s1.getInventory().getQuantity(0));
        } catch (Exception e) {
            fail("Exception should not be thrown when adding product to an existing store");
        }
    }

    @Test
    public void testAddProduct_StoreDoesNotExist() {
        int productId = 101;
        double price = 19.99;
        int quantity = 10;
        storeController.GetStores().put(0,s1);
        Exception exception = assertThrows(Exception.class, () -> {
            storeController.addProduct(productId, 0, price, quantity);
        });

        String expectedMessage = "Price cannot be zero or less, quantity can only be 0 or positive!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRemoveProduct_StoreExists() throws Exception {
        storeController.GetStores().put(s1.getId(), s1);
        storeController.GetStores().put(s2.getId(), s2);
        s1.AddProduct(p1.getId(), 10.0, 5);
        s1.AddProduct(p2.getId(), 20.0, 10);
        int productId = p1.getId();

        try {
            String result = storeController.removeProduct(productId, s1.getId());
            assertEquals("Product Removed from store Successfully", result);
        } catch (Exception e) {
            fail("Exception should not be thrown when removing product from an existing store");
        }
    }

    @Test
    public void testRemoveProduct_StoreDoesNotExist() throws Exception {
        storeController.GetStores().put(s1.getId(), s1);
        storeController.GetStores().put(s2.getId(), s2);
        s1.AddProduct(p1.getId(), 10.0, 5);
        s1.AddProduct(p2.getId(), 20.0, 10);
        int productId = p1.getId();
        int nonExistentStoreID = 999;

        String result = storeController.removeProduct(productId, nonExistentStoreID);
        assertEquals("Product Removed from store Successfully", result);
    }

    @Test
    public void testEditProductPrice_Success() throws Exception {
        storeController.GetStores().put(s1.getId(), s1);
        storeController.GetStores().put(s2.getId(), s2);
        s1.AddProduct(p1.getId(), 10.0, 5);
        s1.AddProduct(p2.getId(), 20.0, 10);
        String result = storeController.EditProducPrice(p1.getId(), s1.getId(), 150.0);
        assertEquals("Product's Price Modified in store Successfully", result);
        assertEquals(150.0, s1.getProdPrice(p1.getId()));
    }


    @Test
    public void testEditProductQuantity_Success() throws Exception {
        storeController.GetStores().put(s1.getId(), s1);
        storeController.GetStores().put(s2.getId(), s2);
        s1.AddProduct(p1.getId(), 10.0, 5);
        s1.AddProduct(p2.getId(), 20.0, 10);
        String result = storeController.EditProductQuantity(p1.getId(), s1.getId(), 20);
        assertEquals("Product's Quantity Modified in store Successfully", result);
        assertEquals(20, s1.getInventory().getQuantity(p1.getId()));
    }

    @Test
    public void testCloseStore_Success() {
        storeController.GetStores().put(s1.getId(), s1);
        String result = storeController.closeStore(s1.getId());
        assertEquals("Store Closed Successfully", result);
        assertFalse(s1.isActive());
    }

    @Test
    public void testOpenStore_Success() {
        storeController.GetStores().put(s1.getId(), s1);
        String result = storeController.openStore(s1.getId());
        assertEquals("Store Opened Successfuly", result);
        assertTrue(s1.isActive());
    }

    @Test
    public void testGetInfo(){
        storeController.GetStores().put(s1.getId(), s1);
        String result = storeController.getInfo(s2.getId());
        assertEquals("No such store!", result);
    }

}