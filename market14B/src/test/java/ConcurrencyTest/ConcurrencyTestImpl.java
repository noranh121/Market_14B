package ConcurrencyTest;
import org.market.DomainLayer.backend.Permissions;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import org.market.Application;
import org.market.DomainLayer.backend.Market;
import org.market.DomainLayer.backend.StorePackage.StoreController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class ConcurrencyTestImpl {
    @Autowired
    private ApplicationContext context;
    Market market;
    @BeforeEach
    void setUp() {
        market=(Market) context.getBean(Market.class);
        org.market.DataAccessLayer.Entity.Market dataMarket=new org.market.DataAccessLayer.Entity.Market(0,true,new ArrayList<>());
        Market.getDC().getMarketRepository().save(dataMarket);
    }
    @AfterEach
    void tearDown(){
        market.clear();
        Market.getDC().clearAll();
    }

    //2 users try to but the same last product at the same time
    @Test
    public void tess2UserBuy() throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        market.EnterAsGuest(18);
        market.Register("essa","1",18);
        market.Login("0","essa","1");
        market.initStore("essa","name","desc");
        market.addCatagory(0,"meat",systemManager);
        market.initProduct(systemManager,"steak",0,"d","b",5.0);
        market.addProduct(0,0,10,1,"essa",18);
        market.EnterAsGuest(18);
        market.EnterAsGuest(18);
        market.Register("maged","2",18);
        market.Register("ola","5",18);
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
                double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
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
                double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
                if (sum==10.0){
                    result[1]=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                fail(e.getMessage());
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
        assertEquals(Market.getSC().getStore(0).getInventory().getQuantity(0),0);
    }

    //store owner tries to delete a product while a user tries to buy it
    @Test
    public void testDelAndBuy() throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        market.EnterAsGuest(18);
        market.Register("essa","1",18);
        market.Login("0","essa","1");
        market.initStore("essa","name","desc");
        market.addCatagory(0,"meat",systemManager);
        market.initProduct(systemManager,"steak",0,"d","b",5.0);
        market.addProduct(0,0,10,1,"essa",18);
        market.EnterAsGuest(18);
        market.Register("maged","2",18);
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
                double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
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
        try {
            assertNull(Market.getSC().getStore(0).getInventory().getQuantity(0));
        }catch (Exception e){
            assertTrue(e.getMessage().contains("is null"));
        }
    }

    //2 store owners try to assign the same user to store owner at the same time
    @Test
    public void test2AssignOwner() throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        market.EnterAsGuest(18);
        market.Register("essa","1",18);
        market.Login("0","essa","1");
        market.initStore("essa","name","desc");
        market.addCatagory(0,"meat",systemManager);
        market.initProduct(systemManager,"steak",0,"d","b",5.0);
        market.EnterAsGuest(18);
        market.EnterAsGuest(18);
        market.Register("maged","2",18);
        market.Register("ola","5",18);
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
        assertEquals(Market.getP().getPermission(0,"ola").getUserName(),"ola");
        assertTrue(Market.getP().getPermission(0,"ola").getStoreOwner());
    }

    //2 store owners try to assign the same user to store mager at the same time
    @Test
    public void test2AssignManager() throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        market.EnterAsGuest(18);
        market.Register("essa","1",18);
        market.Login("0","essa","1");
        market.initStore("essa","name","desc");
        market.EnterAsGuest(18);
        market.EnterAsGuest(18);
        market.Register("maged","2",18);
        market.Register("ola","5",18);
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
        assertEquals(Market.getP().getPermission(0,"ola").getUserName(),"ola");
        assertTrue(Market.getP().getPermission(0,"ola").getStoreManager());
    }

    //store owners try to close the store and a user and tries to buy from the store at the same time
    @Test
    public void testcloseAndBuy() throws Exception {
        String systemManager = "admin";
        market.getSystemManagers().add(systemManager);
        market.setMarketOnline(systemManager);
        market.EnterAsGuest(18);
        market.Register("essa","1",18);
        market.Login("0","essa","1");
        market.initStore("essa","name","desc");
        market.addCatagory(0,"meat",systemManager);
        market.initProduct(systemManager,"steak",0,"d","b",5.0);
        market.addProduct(0,0,10,1,"essa",5);
        market.EnterAsGuest(18);
        market.Register("maged","2",18);
        market.Login("1","maged","2");
        market.addToCart("maged",0,0,1);
        market.OpenStore(0, "essa");
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
                double sum=market.Buy("ali","dollar","123","5","2027","123","Ab2","city","country","434","20444444");
                if (sum==10.0){
                    result[1]=true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
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
        if ((result[0] && result[1])){
            assertEquals(Market.getSC().getStore(0).getInventory().getQuantity(0),0);
            assertFalse(Market.getSC().getStore(0).isActive());
        }else {
            assertFalse(Market.getSC().getStore(0).isActive());
        }
    }

}
