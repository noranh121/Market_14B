package Tests.ConcurrencyTest;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.concurrent.CountDownLatch;
import DomainLayer.backend.Market;
import DomainLayer.backend.ProductPackage.Category;
import DomainLayer.backend.ProductPackage.Product;
import DomainLayer.backend.StorePackage.Store;
import DomainLayer.backend.StorePackage.StoreController;
import DomainLayer.backend.UserPackage.GuestUser;
import DomainLayer.backend.UserPackage.RegisteredUser;
import DomainLayer.backend.UserPackage.User;
import DomainLayer.backend.UserPackage.UserController;
import org.junit.jupiter.api.Test;
public class ConcurrencyTestImpl {
    Market market;
    @BeforeEach
    void setUp() {
        market=Market.getInstance();
    }
    @AfterEach
    void tearDown(){
        market.setToNull();
    }

    //2 users try to but the same last product at the same time
    @Test
    public void tess2UserBuy() throws Exception {
        market.EnterAsGuest();
        market.Register("essa","1");
        market.Login("0","essa","1");
        market.initStore("essa","desc");
        market.addProduct(0,0,10,1,"essa");
        market.EnterAsGuest();
        market.EnterAsGuest();
        market.Register("maged","2");
        market.Register("ola","5");
        market.Login("1","maged","2");
        market.Login("2","ola","5");
        market.addToCart("maged",0,0,1);
        market.addToCart("ola",0,0,1);
        boolean[] result=new boolean[2];
        result[0]=false;
        result[1]=false;
        int numberOfThreads = 2; // We want to test with two threads
        CountDownLatch readyLatch = new CountDownLatch(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);

        Runnable task1 = () -> {
            try {
                readyLatch.countDown(); // Indicate this thread is ready
                startLatch.await(); // Wait for the start signal
                double sum=market.Buy("maged");
                if (sum==10.0){
                    result[0]=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
            } finally {
                doneLatch.countDown(); // Indicate this thread is done
            }
        };
        Runnable task2 = () -> {
            try {
                readyLatch.countDown(); // Indicate this thread is ready
                startLatch.await(); // Wait for the start signal
                double sum=market.Buy("ola");
                if (sum==10.0){
                    result[1]=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                doneLatch.countDown(); // Indicate this thread is done
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();

        readyLatch.await(); // Wait for both threads to be ready
        startLatch.countDown(); // Signal both threads to start
        doneLatch.await(); // Wait for both threads to finish

        assertTrue(result[0] || result[1]);
        assertFalse(result[0] && result[1]);
    }

    //store owner tries to delete a product while a user tries to buy it
    @Test
    public void testDelAndBuy() throws Exception {
        market.EnterAsGuest();
        market.Register("essa","1");
        market.Login("0","essa","1");
        market.initStore("essa","desc");
        market.addProduct(0,0,10,1,"essa");
        market.EnterAsGuest();
        market.Register("maged","2");
        market.Login("1","maged","2");
        market.addToCart("maged",0,0,1);
        boolean[] result=new boolean[2];
        result[0]=false;
        result[1]=false;
        int numberOfThreads = 2; // We want to test with two threads
        CountDownLatch readyLatch = new CountDownLatch(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);

        Runnable task1 = () -> {
            try {
                readyLatch.countDown(); // Indicate this thread is ready
                startLatch.await(); // Wait for the start signal
                double sum=market.Buy("maged");
                if (sum==10.0){
                    result[0]=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
            } finally {
                doneLatch.countDown(); // Indicate this thread is done
            }
        };
        Runnable task2 = () -> {
            try {
                readyLatch.countDown(); // Indicate this thread is ready
                startLatch.await(); // Wait for the start signal
                String res=market.RemoveProduct(0,0,"essa");
                if (res.equals("Product Removed from store Successfully")){
                    result[1]=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                doneLatch.countDown(); // Indicate this thread is done
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();

        readyLatch.await(); // Wait for both threads to be ready
        startLatch.countDown(); // Signal both threads to start
        doneLatch.await(); // Wait for both threads to finish

        assertTrue((result[0] && result[1])||(!result[0] && result[1]));
    }

    //2 store owners try to assign the same user to store owner at the same time
    @Test
    public void test2AssignOwner() throws Exception {
        market.EnterAsGuest();
        market.Register("essa","1");
        market.Login("0","essa","1");
        market.initStore("essa","desc");
        market.EnterAsGuest();
        market.EnterAsGuest();
        market.Register("maged","2");
        market.Register("ola","5");
        market.Login("1","maged","2");
        market.Login("2","ola","5");
        Boolean[] p={true,true,true};
        market.AssignStoreOwner(0,"essa","maged",p);
        boolean[] result=new boolean[2];
        result[0]=false;
        result[1]=false;
        int numberOfThreads = 2; // We want to test with two threads
        CountDownLatch readyLatch = new CountDownLatch(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);

        Runnable task1 = () -> {
            try {
                readyLatch.countDown(); // Indicate this thread is ready
                startLatch.await(); // Wait for the start signal
                String res=market.AssignStoreOwner(0,"essa","ola",p);
                if (res.equals("Permission added to store")){
                    result[0]=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
            } finally {
                doneLatch.countDown(); // Indicate this thread is done
            }
        };
        Runnable task2 = () -> {
            try {
                readyLatch.countDown(); // Indicate this thread is ready
                startLatch.await(); // Wait for the start signal
                String res=market.AssignStoreOwner(0,"maged","ola",p);
                if (res.equals("Permission added to store")){
                    result[1]=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                doneLatch.countDown(); // Indicate this thread is done
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();

        readyLatch.await(); // Wait for both threads to be ready
        startLatch.countDown(); // Signal both threads to start
        doneLatch.await(); // Wait for both threads to finish

        assertTrue(result[0] || result[1]);
        assertFalse(result[0] && result[1]);
    }

    //2 store owners try to assign the same user to store mager at the same time
    @Test
    public void test2AssignManager() throws Exception {
        market.EnterAsGuest();
        market.Register("essa","1");
        market.Login("0","essa","1");
        market.initStore("essa","desc");
        market.EnterAsGuest();
        market.EnterAsGuest();
        market.Register("maged","2");
        market.Register("ola","5");
        market.Login("1","maged","2");
        market.Login("2","ola","5");
        Boolean[] p={true,true,true};
        market.AssignStoreOwner(0,"essa","maged",p);
        boolean[] result=new boolean[2];
        result[0]=false;
        result[1]=false;
        int numberOfThreads = 2; // We want to test with two threads
        CountDownLatch readyLatch = new CountDownLatch(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);

        Runnable task1 = () -> {
            try {
                readyLatch.countDown(); // Indicate this thread is ready
                startLatch.await(); // Wait for the start signal
                String res=market.AssignStoreManager(0,"essa","ola",p);
                if (res.equals("Permission added to store")){
                    result[0]=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
            } finally {
                doneLatch.countDown(); // Indicate this thread is done
            }
        };
        Runnable task2 = () -> {
            try {
                readyLatch.countDown(); // Indicate this thread is ready
                startLatch.await(); // Wait for the start signal
                String res=market.AssignStoreManager(0,"maged","ola",p);
                if (res.equals("Permission added to store")){
                    result[1]=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                doneLatch.countDown(); // Indicate this thread is done
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();

        readyLatch.await(); // Wait for both threads to be ready
        startLatch.countDown(); // Signal both threads to start
        doneLatch.await(); // Wait for both threads to finish

        assertTrue(result[0] || result[1]);
        assertFalse(result[0] && result[1]);
    }

    //store owners try to close the store and a user and tries to buy from the store at the same time
    @Test
    public void testcloseAndBuy() throws Exception {
        market.EnterAsGuest();
        market.Register("essa","1");
        market.Login("0","essa","1");
        market.initStore("essa","desc");
        market.addProduct(0,0,10,1,"essa");
        market.EnterAsGuest();
        market.Register("maged","2");
        market.Login("1","maged","2");
        market.addToCart("maged",0,0,1);
        boolean[] result=new boolean[2];
        result[0]=false;
        result[1]=false;
        int numberOfThreads = 2; // We want to test with two threads
        CountDownLatch readyLatch = new CountDownLatch(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);

        Runnable task1 = () -> {
            try {
                readyLatch.countDown(); // Indicate this thread is ready
                startLatch.await(); // Wait for the start signal
                String res=market.CloseStore(0,"essa");
                if (res.equals("Store Closed Successfuly")){
                    result[0]=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
            } finally {
                doneLatch.countDown(); // Indicate this thread is done
            }
        };
        Runnable task2 = () -> {
            try {
                readyLatch.countDown(); // Indicate this thread is ready
                startLatch.await(); // Wait for the start signal
                double sum=market.Buy("maged");
                if (sum==10.0){
                    result[1]=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                doneLatch.countDown(); // Indicate this thread is done
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();

        readyLatch.await(); // Wait for both threads to be ready
        startLatch.countDown(); // Signal both threads to start
        doneLatch.await(); // Wait for both threads to finish

        assertTrue((result[0] && result[1])||(!result[1] && result[0]));
    }

}
