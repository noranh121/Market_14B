// package UnitTest;
// import org.market.DomainLayer.backend.ProductPackage.Category;
// import org.market.DomainLayer.backend.ProductPackage.Product;
// import org.market.DomainLayer.backend.StorePackage.Store;
// import org.market.DomainLayer.backend.StorePackage.StoreController;
// import org.junit.jupiter.api.*;
// import static org.junit.jupiter.api.Assertions.*;

// public class StoreUnitTest {
//     private StoreController storeController ;
//     Store s1;
//     Store s2;
//     Product p1;
//     Product p2;
//     Category c1;
//     Category c2;
//     @BeforeEach
//     public void setUp() {
//         storeController = StoreController.getInstance();
//         s1=new Store("store1","decs1",0);
//         s2=new Store("store2","decs2",1);
//         c1=new Category(0,"c1");
//         c2=new Category(1,"c2");
//         p1=new Product("product1","desc1","brand1",c1,5);
//         p2=new Product("product2","desc2","brand2",c2,5);
//     }

//     @AfterEach
//     void tearDown(){
//         storeController.setToNull();
//     }

//     @Test
//     public void testGetStore_StoreExists() {
//         storeController.GetStores().put(0,s1);
//         storeController.GetStores().put(1,s2);
//         Store result = storeController.getStore(s1.getId());
//         assertNotNull(result);
//         assertEquals(s1, result);
//         result = storeController.getStore(s2.getId());
//         assertNotNull(result);
//         assertEquals(s2, result);
//     }

//     @Test
//     public void testGetStoreFail() {
//         int nonExistentStoreID = 999;
//         Store result = storeController.getStore(nonExistentStoreID);
//         assertNull(result);
//     }

//     @Test
//     public void testAddProductSuccess() {
//         storeController.GetStores().put(0,s1);
//         try {
//             String result = storeController.addProduct(p1.getId(), s1.getId(), 10, 15,5);
//             assertEquals("Product added to store Successfully", result);
//             assertEquals(10, s1.getProdPrice(0));
//             assertEquals(15, s1.getInventory().getQuantity(0));
//         } catch (Exception e) {
//             fail("Exception should not be thrown when adding product to an existing store");
//         }
//     }

//     @Test
//     public void testAddProductFail() throws Exception {
//         int productId = 101;
//         double price = 19.99;
//         int quantity = 10;
//         storeController.GetStores().put(0,s1);
//         String result=storeController.addProduct(productId, 2, price, quantity,5);
//         String expectedMessage = "store does not exist";
//         assertTrue(result.contains(expectedMessage));
//     }

//     @Test
//     public void testRemoveProductSuccess() throws Exception {
//         storeController.GetStores().put(s1.getId(), s1);
//         storeController.GetStores().put(s2.getId(), s2);
//         s1.AddProduct(p1.getId(), 10.0, 5,5);
//         s1.AddProduct(p2.getId(), 20.0, 10,5);
//         int productId = p1.getId();

//         try {
//             String result = storeController.removeProduct(productId, s1.getId());
//             assertEquals("Product Removed from store Successfully", result);
//         } catch (Exception e) {
//             fail("Exception should not be thrown when removing product from an existing store");
//         }
//     }

//     @Test
//     public void testRemoveProductFail() throws Exception {
//         storeController.GetStores().put(s1.getId(), s1);
//         storeController.GetStores().put(s2.getId(), s2);
//         s1.AddProduct(p1.getId(), 10.0, 5,5);
//         s1.AddProduct(p2.getId(), 20.0, 10,5);
//         int productId = p1.getId();
//         int nonExistentStoreID = 999;

//         String result = storeController.removeProduct(productId, nonExistentStoreID);
//         assertEquals("Product Removed from store Successfully", result);
//     }

//     @Test
//     public void testEditProductPriceSuccess() throws Exception {
//         storeController.GetStores().put(s1.getId(), s1);
//         storeController.GetStores().put(s2.getId(), s2);
//         s1.AddProduct(p1.getId(), 10.0, 5,5);
//         s1.AddProduct(p2.getId(), 20.0, 10,5);
//         String result = storeController.EditProducPrice(p1.getId(), s1.getId(), 150.0);
//         assertEquals("Product's Price Modified in store Successfully", result);
//         assertEquals(150.0, s1.getProdPrice(p1.getId()));
//     }

//     @Test
//     public void testEditProductPriceFail() throws Exception {
//         storeController.GetStores().put(s1.getId(), s1);
//         storeController.GetStores().put(s2.getId(), s2);
//         s1.AddProduct(p1.getId(), 10.0, 5,5);
//         s1.AddProduct(p2.getId(), 20.0, 10,5);
//         String result = storeController.EditProducPrice(1, s1.getId(), 150.0);
//         assertEquals(20, s1.getProdPrice(p1.getId()));
//     }


//     @Test
//     public void testEditProductQuantitySuccess() throws Exception {
//         storeController.GetStores().put(s1.getId(), s1);
//         storeController.GetStores().put(s2.getId(), s2);
//         s1.AddProduct(p1.getId(), 10.0, 5,5);
//         s1.AddProduct(p2.getId(), 20.0, 10,5);
//         String result = storeController.EditProductQuantity(p1.getId(), s1.getId(), 20);
//         assertEquals("Product's Quantity Modified in store Successfully", result);
//         assertEquals(20, s1.getInventory().getQuantity(p1.getId()));
//     }

//     @Test
//     public void testEditProductQuantityFail() throws Exception {
//         storeController.GetStores().put(s1.getId(), s1);
//         storeController.GetStores().put(s2.getId(), s2);
//         s1.AddProduct(p1.getId(), 10.0, 5,5);
//         s1.AddProduct(p2.getId(), 20.0, 10,5);
//         String result = storeController.EditProductQuantity(2, s1.getId(), 20);
//         assertEquals(10, s1.getInventory().getQuantity(p1.getId()));
//     }

//     @Test
//     public void testCloseStoreSuccess() {
//         storeController.GetStores().put(s1.getId(), s1);
//         String result = storeController.closeStore(s1.getId());
//         assertEquals("Store Closed Successfuly", result);
//         assertFalse(s1.isActive());
//     }

//     @Test
//     public void testCloseStoreFail() {
//         storeController.GetStores().put(s1.getId(), s1);
//         String result = storeController.closeStore(5);
//         assertEquals(storeController.GetStores().size() ,1);
//         assertEquals(storeController.GetStores().get(s1.getId()).isActive(),false);
//     }

//     @Test
//     public void testOpenStoreSuccess() {
//         storeController.GetStores().put(s1.getId(), s1);
//         String result = storeController.openStore(s1.getId());
//         assertEquals("Store Opened Successfuly", result);
//         assertTrue(s1.isActive());
//     }

//     @Test
//     public void testOpenStoreFail() {
//         storeController.GetStores().put(s1.getId(), s1);
//         String result = storeController.openStore(5);
//         assertEquals(storeController.GetStores().size() ,1);
//         assertEquals(storeController.GetStores().get(s1.getId()).isActive(),false );
//     }

//     @Test
//     public void testGetInfo(){
//         storeController.GetStores().put(s1.getId(), s1);
//         String result = storeController.getInfo(s2.getId());
//         assertEquals("No such store!", result);
//     }

// }
