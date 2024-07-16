 package UnitTest;
 import org.market.Application;
import org.market.DataAccessLayer.DataController;
import org.market.DomainLayer.backend.Basket;
import org.market.DomainLayer.backend.Market;
import org.market.DomainLayer.backend.ProductPackage.Category;
 import org.market.DomainLayer.backend.ProductPackage.Product;
 import org.market.DomainLayer.backend.StorePackage.Store;
 import org.market.DomainLayer.backend.StorePackage.StoreController;
 import org.market.DomainLayer.backend.UserPackage.GuestUser;
 import org.market.DomainLayer.backend.UserPackage.RegisteredUser;
 import org.market.DomainLayer.backend.UserPackage.User;
 import org.market.DomainLayer.backend.UserPackage.UserController;
 import org.junit.jupiter.api.*;
 //import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.context.SpringBootTest;
 //import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Transactional
 public class UserUnitTests {
    UserController userController;
     User u1;
     User u2;
     User u3;
     User u4;

     @BeforeEach
     public void setUp() {
         userController=Market.getUC();
         u1=new GuestUser(0,18);
         u2=new GuestUser(1,18);
         u3=new RegisteredUser("ali","123",18);
         u4=new RegisteredUser("essa","456",18);
     }

     @AfterEach
     void tearDown(){
        userController.clear();
        //Market.getDC().clearAll();
     }

     @Test
     //@DirtiesContext
     public void testEnterAsGuestSuccess() throws Exception {
        
         String result = userController.EnterAsGuest(18);
         assertNotNull(result);
         assertEquals("0", result);
         assertEquals(1, userController.getGuestUserMap().size());
     }

    @Test
    //@DirtiesContext
    public void testEnterAsGuestSuccess2() throws Exception {
        String result1 = userController.EnterAsGuest(18);
        String result2 = userController.EnterAsGuest(18);
        String result3 = userController.EnterAsGuest(18);
        String result4 = userController.EnterAsGuest(18);
        assertEquals("0",result1);
        assertEquals("1",result2);
        assertEquals("2",result3);
        assertEquals("3",result4);
        assertEquals(4, userController.getGuestUserMap().size());
    }
    @Test
    public void testGuestExit_Success() {
        try {
            userController.getGuestUserMap().put(u1.getUsername(), u1);
            String result = userController.GuestExit(u1.getUsername());
            assertEquals("guest existed successfully", result);
            assertNull(userController.getGuestUserMap().get(u1.getUsername()));
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGuestExit_Failure() {
        try {  
            userController.GuestExit("nonExistentUser");
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertEquals("guest user cannot be deleted", e.getMessage());
        }
    }


    @Test
    public void testLogin_Success() {
        try {
            userController.getGuestUserMap().put(u1.getUsername(), u1);
            userController.Register(u3.getUsername(),"123",18);
            String result = userController.Login(u1.getUsername(), u3.getUsername(),"123");
            assertEquals("logged in successfully", result);
            assertNull(userController.getGuestUserMap().get(u1.getUsername()));
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLogin_IncorrectUsername() {
        try { 
            userController.getGuestUserMap().put(u1.getUsername(), u1);
            userController.getRegUserMap().put(u3.getUsername(), u3);
            String result = userController.Login(u1.getUsername(), "wrong","123");
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertEquals("username or password are incorrect", e.getMessage());
        }
    }

    @Test
    public void testLogin_IncorrectPassword() {
        try { 
            userController.getGuestUserMap().put(u1.getUsername(), u1);
            userController.getRegUserMap().put(u3.getUsername(), u3);
            String result = userController.Login(u1.getUsername(), u3.getUsername(),"11111");
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertEquals("username or password are incorrect", e.getMessage());
        }
    }

    @Test
    public void testLogout_Success() {
        try {
            userController.Register(u3.getUsername(),"123",18);
            userController.getRegUserMap().get(u3.getUsername()).setLoggedIn(true);
            String result = userController.Logout(u3.getUsername());
            assertEquals("0", result);
            assertFalse(u3.isLoggedIn());
            assertTrue(userController.getGuestUserMap().size()==1);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLogout_UserNotFound() {
        try {
            
            String nonExistentUser = "nonExistentUser";
            userController.Logout(nonExistentUser);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException, "Expected NullPointerException");
        }
    }

    @Test
    public void testRegister_Success() {
        String username = "newUser";
        String password = "password123";
        
        try {
            String result = userController.Register(username, password,18);
            assertEquals("User registered successfully", result);
            assertTrue(userController.getRegUserMap().containsKey(username));
            assertFalse(userController.getRegUserMap().get(username).isLoggedIn());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterDoubleRegistration() {
        String username = "newUser";
        String password = "password123";
        
        try {
            String result = userController.Register(username, password,18);
            assertEquals("User registered successfully", result);
            assertTrue(userController.getRegUserMap().containsKey(username));
            assertFalse(userController.getRegUserMap().get(username).isLoggedIn());
            String result1 = userController.Register(username, password,18);
        } catch (Exception e) {
            assertEquals("username already exists" , e.getMessage());
        }
    }
    @Test
    public void testRegisterUsernameExists() {
        String username = "existingUser";
        String password = "password123";
        
        try {
            userController.Register(username, password,18);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
        try {
            userController.Register(username, password,18);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertEquals("username already exists", e.getMessage());
        }
    }

    @Test
    public void testBuySuccess() throws Exception {
        
        userController.getRegUserMap().put(u3.getUsername(), u3);
        Store s=new Store(u1.getUsername(),"s1","desc",0);
        Market.getSC().GetStores().put(0,s);
        s.getInventory().AddProduct(0,10.0,2,5);
        Basket b=new Basket(u3.getUsername(),0);
        u3.getShoppingCart().addBasket(b);
        b.addProduct(0,1);
        double expectedSum = 10.0;
        try {
            double sum = userController.Buy(u3.getUsername());
            assertEquals(expectedSum, sum);
            assertEquals(Market.getSC().getStore(0).getInventory().getQuantity(0),1);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBuyGuestUserExists() throws Exception {
        
        userController.getGuestUserMap().put(u1.getUsername(), u1);
        Store s=new Store(u1.getUsername(),"s1","desc",0);
        Market.getSC().GetStores().put(0,s);
        s.getInventory().AddProduct(0,10.0,2,5);
        Basket b=new Basket(u1.getUsername(),0);
        u1.getShoppingCart().addBasket(b);
        b.addProduct(0,1);
        double expectedSum = 10.0;
        try {
            double sum = userController.Buy(u1.getUsername());
            assertEquals(expectedSum, sum);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBuyUserNotExists() {
        
        String nonExistentUser = "nonExistentUser";
        try {
            userController.Buy(nonExistentUser);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertEquals("user not found", e.getMessage());
        }
    }

    @Test
    public void testBuyNotEnoughSupply() throws Exception {
        
        userController.getGuestUserMap().put(u1.getUsername(), u1);
        Store s=new Store(u1.getUsername(),"s1","desc",0);
        Market.getSC().GetStores().put(0,s);
        s.getInventory().AddProduct(0,10.0,2,5);
        Basket b=new Basket(u1.getUsername(),0);
        u1.getShoppingCart().addBasket(b);
        b.addProduct(0,3);
        double expectedSum = 10.0;
        try {
            double sum = userController.Buy(u1.getUsername());
            fail();
        } catch (Exception e) {
            assertEquals("invalid cart" , e.getMessage());
            assertEquals(Market.getSC().getStore(0).getInventory().getQuantity(0),2);
            assertEquals(userController.getUser(u1.getUsername()).getShoppingCart().getBasket(u1.getUsername(), 0).getProducts().size(),1);
        }
    }


    @Test
    public void testGetUser_RegisteredUserExists() {
        
        String username = "registeredUser";
        RegisteredUser registeredUser = new RegisteredUser(username, "password",18);
        userController.getRegUserMap().put(username, registeredUser);
        assertEquals(registeredUser, userController.getUser(username));
    }

    @Test
    public void testGetUser_GuestUserExists() {
        
        String username = "guestUser";
        GuestUser guestUser = new GuestUser(1,18);
        userController.getGuestUserMap().put(username, guestUser);
        assertEquals(guestUser, userController.getUser(username));
    }

    @Test
    public void testGetUser_UserNotExists() {
        
        String nonExistentUsername = "nonExistentUser";
        assertNull(userController.getUser(nonExistentUsername));
    }

    @Test
    public void testAddToCart() {
        try {
            
            userController.getRegUserMap().put(u3.getUsername(), u3);
            Category c=new Category(0,"cat");
            Product p1=new Product("a","d","b",c,5);
            Basket basket=new Basket(u3.getUsername(),0);
            u3.getShoppingCart().getBaskets().add(basket);
            String res=userController.addToCart(u3.getUsername(), p1.getId(), 0,5);
            assertTrue(res.contains("added to cart"));
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void inspectCartTest(){
        
        userController.getRegUserMap().put(u3.getUsername(), u3);
        String res=userController.inspectCart(u3.getUsername());
        assertTrue(res.length()==0);
    }

    @Test
    public void testRemoveEmptyCartItem(){
        
        userController.getRegUserMap().put(u3.getUsername(), u3);
        String res=userController.removeCartItem(u3.getUsername(),0,0);
        assertTrue(res.contains("no such item"));
    }

    @Test
    public void testRemoveCartItemSuccess() throws Exception {
        
        userController.getRegUserMap().put(u3.getUsername(), u3);
        Category c=new Category(0,"cat");
        Product p1=new Product("a","d","b",c,5);
        Basket basket=new Basket(u3.getUsername(),0);
        basket.addProduct(0,5);
        u3.getShoppingCart().getBaskets().add(basket);
        String res=userController.removeCartItem(u3.getUsername(),0,0);
        assertTrue(res.contains("item removed successfully"));
    }

    @Test
    public void testIsRegisteredSuccess() throws Exception {
        
        userController.getRegUserMap().put(u3.getUsername(), u3);
        assertTrue(userController.isRegistered(u3.getUsername()));
    }

    @Test
    public void testIsRegisteredFail() {
        
        String nonExistentUsername = "nonExistentUser";
        Exception exception = assertThrows(Exception.class, () -> {
            userController.isRegistered(nonExistentUsername);
        });
        assertEquals("nonExistentUser is not registered", exception.getMessage());
    }
 }

