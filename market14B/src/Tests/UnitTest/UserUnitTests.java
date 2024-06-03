package Tests.UnitTest;
import DomainLayer.backend.Basket;
import DomainLayer.backend.ProductPackage.Category;
import DomainLayer.backend.ProductPackage.Product;
import DomainLayer.backend.StorePackage.Store;
import DomainLayer.backend.UserPackage.GuestUser;
import DomainLayer.backend.UserPackage.RegisteredUser;
import DomainLayer.backend.UserPackage.User;
import DomainLayer.backend.UserPackage.UserController;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserUnitTests {
    private UserController userController;
    User u1;
    User u2;
    User u3;
    User u4;

    @BeforeEach
    public void setUp() {
        userController = UserController.getInstance();
        u1=new GuestUser(0);
        u2=new GuestUser(1);
        u3=new RegisteredUser("ali","123");
        u4=new RegisteredUser("essa","456");
    }

    @Test
    public void testEnterAsGuest1() throws Exception {
        String result = userController.EnterAsGuest();
        assertNotNull(result);
        assertTrue(result.contains("guest user added successfully"));
        assertEquals(1, userController.getGuestUserMap().size());
    }
    @Test
    public void testEnterAsGuest2() throws Exception {
        String result1 = userController.EnterAsGuest();
        String result2 = userController.EnterAsGuest();
        String result3 = userController.EnterAsGuest();
        String result4 = userController.EnterAsGuest();
        assertTrue(result1.contains("guest user added successfully"));
        assertTrue(result2.contains("guest user added successfully"));
        assertTrue(result3.contains("guest user added successfully"));
        assertTrue(result4.contains("guest user added successfully"));
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
            userController.getRegUserMap().put(u3.getUsername(), u3);
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
            userController.getRegUserMap().put(u3.getUsername(), u3);
            u3.setLoggedIn(true);
            String result = userController.Logout(u3.getUsername());
            assertEquals("guest user added successfully", result);
            assertFalse(u3.isLoggedIn());
            assertTrue(userController.getGuestUserMap().containsKey(u3.getUsername()));
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
            assertEquals("guest user cannot be added", e.getMessage());
        }
    }

    @Test
    public void testRegister_Success() {
        String username = "newUser";
        String password = "password123";

        try {
            String result = userController.Register(username, password);
            assertEquals("guest user added successfully", result);
            assertTrue(userController.getRegUserMap().containsKey(username));
            assertTrue(userController.getRegUserMap().get(username).isLoggedIn());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testRegister_UsernameExists() {
        String username = "existingUser";
        String password = "password123";
        try {
            userController.Register(username, password);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
        try {
            userController.Register(username, password);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertEquals("username already exists", e.getMessage());
        }
    }

    @Test
    public void testBuy_GuestUserExists() throws Exception {
        userController.getRegUserMap().put(u3.getUsername(), u3);
        Store s=new Store("store","desc",0);
        Basket b=new Basket(u3.getUsername(),0);
        u3.getShoppingCart().addBasket(b);
        b.addProduct(0,1);
        double expectedSum = 0;
        try {
            double sum = userController.Buy(u3.getUsername());
            assertEquals(expectedSum, sum);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBuy_RegUserExists() throws Exception {
        userController.getGuestUserMap().put(u1.getUsername(), u1);
        Store s=new Store("store","desc",0);
        Basket b=new Basket(u1.getUsername(),0);
        u1.getShoppingCart().addBasket(b);
        b.addProduct(0,1);
        double expectedSum = 0;
        try {
            double sum = userController.Buy(u1.getUsername());
            assertEquals(expectedSum, sum);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBuy_UserNotExists() {
        String nonExistentUser = "nonExistentUser";
        try {
            userController.Buy(nonExistentUser);
            fail("Exception should have been thrown");
        } catch (Exception e) {
            assertEquals("user not found", e.getMessage());
        }
    }

    @Test
    public void testGetUser_RegisteredUserExists() {
        String username = "registeredUser";
        RegisteredUser registeredUser = new RegisteredUser(username, "password");
        userController.getRegUserMap().put(username, registeredUser);
        assertEquals(registeredUser, userController.getUser(username));
    }

    @Test
    public void testGetUser_GuestUserExists() {
        String username = "guestUser";
        GuestUser guestUser = new GuestUser(1);
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
            Product p1=new Product("a","d","b",c);
            Basket basket=new Basket(u3.getUsername(),0);
            u3.getShoppingCart().getBaskets().add(basket);
            String res=userController.addToCart(u3.getUsername(), p1.getId(), 0,5);
            assertTrue(res.contains("product added successfully"));
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void inspectCartTest(){
        userController.getRegUserMap().put(u3.getUsername(), u3);
        String res=userController.inspectCart(u3.getUsername());
        assertTrue(res.contains("Your shopping cart is empty."));
    }

    @Test
    public void removeCartItemTest1(){
        userController.getRegUserMap().put(u3.getUsername(), u3);
        String res=userController.removeCartItem(u3.getUsername(),0,0);
        assertTrue(res.contains("no such item"));
    }

    @Test
    public void removeCartItemTest2(){
        userController.getRegUserMap().put(u3.getUsername(), u3);
        Category c=new Category(0,"cat");
        Product p1=new Product("a","d","b",c);
        Basket basket=new Basket(u3.getUsername(),0);
        u3.getShoppingCart().getBaskets().add(basket);
        String res=userController.removeCartItem(u3.getUsername(),0,0);
        assertTrue(res.contains("item removed successfully"));
    }

    @Test
    public void testIsRegistered_UserIsRegistered() throws Exception {
        userController.getRegUserMap().put(u3.getUsername(), u3);
        assertTrue(userController.isRegistered(u3.getUsername()));
    }

    @Test
    public void testIsRegistered_UserIsNotRegistered() {
        String nonExistentUsername = "nonExistentUser";
        Exception exception = assertThrows(Exception.class, () -> {
            userController.isRegistered(nonExistentUsername);
        });
        assertEquals("nonExistentUser is not registered", exception.getMessage());
    }
}

