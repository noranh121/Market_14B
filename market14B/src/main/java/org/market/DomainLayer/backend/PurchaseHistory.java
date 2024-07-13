package org.market.DomainLayer.backend;

import org.market.DomainLayer.backend.StorePackage.StoreController;
import org.market.DomainLayer.backend.UserPackage.UserController;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class PurchaseHistory {

    private static PurchaseHistory instance;
    private static int counterID;

    private Map<Integer, List<Purchase>> storeHistory; // storeid ==> purchases
    private Map<String, List<Purchase>> userHistory; // userid ==> purchases
    private Map<Integer, Purchase> allPurchases; // purchaseid ==> purchase
    private final Lock purchaseHistoryLock = new ReentrantLock();

    private PurchaseHistory() {
        counterID = 0;
        storeHistory = new ConcurrentHashMap<>();
        userHistory = new ConcurrentHashMap<>();
        allPurchases= new ConcurrentHashMap<>();
    }

    public static synchronized PurchaseHistory getInstance() {
        if (instance == null) {
            instance = new PurchaseHistory();
        }
        return instance;
    }

    // id's should be checked in an earlier stage
    public synchronized void addPurchase(int storeId, String userId, Purchase purchase) {
        purchaseHistoryLock.lock();
        try{
                // Add to all history
            purchase.setID(counterID++);
            allPurchases.put(purchase.getID(), purchase);

            // Add to store history
            storeHistory.computeIfAbsent(storeId, k -> Collections.synchronizedList(new ArrayList<>())).add(purchase);
            StoreController.LOGGER.info("Added purchase to store history: Store ID " + storeId);

            // Add to user history
            userHistory.computeIfAbsent(userId, k -> Collections.synchronizedList(new ArrayList<>())).add(purchase);
            UserController.LOGGER.info("Added purchase to user history: User ID " + userId);
        }finally{
            purchaseHistoryLock.unlock();
        }
        
    }

    public List<Purchase> getStorePurchaseHistory(int storeId) throws InterruptedException {
        while(!purchaseHistoryLock.tryLock()){
            Thread.sleep(1000);
        }
        try{
            StoreController.LOGGER.info("storeId: " + storeId);
            return storeHistory.getOrDefault(storeId, Collections.synchronizedList(new ArrayList<>()));
        }finally{
            purchaseHistoryLock.unlock();
        }
        
    }

    public List<Purchase> getUserPurchaseHistory(String userId) throws InterruptedException {
        while(!purchaseHistoryLock.tryLock()){
            Thread.sleep(1000);
        }
        try{
            UserController.LOGGER.info("userId: " + userId);
            return userHistory.getOrDefault(userId, Collections.synchronizedList(new ArrayList<>()));
        }finally {
            purchaseHistoryLock.unlock();
        }
    }

    public synchronized String removePurchaseFromStore(int storeId, int purchaseId) throws Exception {
        StoreController.LOGGER.info("storeId: " + storeId + ", purchaseId: " + purchaseId);
        List<Purchase> purchases = storeHistory.get(storeId);
        Purchase purchase = null;
        for (Purchase p : purchases) {
            if (p.getID() == purchaseId) {
                purchase = p;
                break;
            }

        }
        if (purchases != null && purchases.remove(purchase)) {
            StoreController.LOGGER.info("Removed purchase from store history: Store ID " + storeId);
            return "Removed purchase from store history: Store ID " + storeId;
        } else {
            StoreController.LOGGER.warning("Purchase not found in store history: Store ID " + storeId);
            throw new Exception("Purchase not found in store history: Store ID " + storeId);
        }
    }

    public synchronized String removePurchaseFromUser(String userId, int purchaseId) throws Exception {
        UserController.LOGGER.info("userId: " + userId + ", purchaseId: " + purchaseId);
        List<Purchase> purchases = userHistory.get(userId);
        Purchase purchase = null;
        for (Purchase p : purchases) {
            if (p.getID() == purchaseId) {
                purchase = p;
                break;
            }

        }
        if (purchases != null && purchases.remove(purchase)) {
            UserController.LOGGER.info("Removed purchase from user history: User ID " + userId);
            return "Removed purchase from user history: User ID " + userId;
        } else {
            UserController.LOGGER.warning("Purchase not found in user history: User ID " + userId);
            throw new Exception("Purchase not found in user history: User ID " + userId);
        }
    }

    public String viewPurchaseHistory() {
        String info = "";
        // if (storeHistory.isEmpty()) {
        //     List<DataAccessLayer.Entity.PurchaseHistory> phs = DataController.getinstance().getPutchaseHistory();
        //     if (phs.isEmpty())
        //         return "<Empty>";
        //     for (DataAccessLayer.Entity.PurchaseHistory ph : phs) {
        //         
        //     }
        // }
        for (Integer storeId : storeHistory.keySet()) {
            for (Purchase purchase : storeHistory.get(storeId)) {
                info += purchase.FetchInfo() + "\n";
            }
        }
        return info;
    }
}
