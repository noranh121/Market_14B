package DomainLayer.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DomainLayer.backend.StorePackage.StoreController;
import DomainLayer.backend.UserPackage.UserController;

public class PurchaseHistory {

    private static PurchaseHistory instance;
    private static int counterID;

    private Map<Integer, List<Purchase>> storeHistory; // storeid ==> purchases
    private Map<Integer, List<Purchase>> userHistory; // userid ==> purchases
    private Map<Integer, Purchase> allPurchases; // purchaseid ==> purchase

    private PurchaseHistory() {
        counterID = 0;
        storeHistory = new HashMap<>();
        userHistory = new HashMap<>();
    }

    public static synchronized PurchaseHistory getInstance() {
        if (instance == null) {
            instance = new PurchaseHistory();
        }
        return instance;
    }

    // id's should be checked in an earlier stage
    public synchronized void addPurchase(int storeId, int userId, Purchase purchase) {
        // Add to all history
        purchase.setID(counterID++);
        allPurchases.put(purchase.getID(), purchase);

        // Add to store history
        storeHistory.computeIfAbsent(storeId, k -> new ArrayList<>()).add(purchase);
        StoreController.LOGGER.info("Added purchase to store history: Store ID " + storeId);

        // Add to user history
        userHistory.computeIfAbsent(userId, k -> new ArrayList<>()).add(purchase);
        UserController.LOGGER.info("Added purchase to user history: User ID " + userId);
    }

    public List<Purchase> getStorePurchaseHistory(int storeId) {
        StoreController.LOGGER.info("storeId: " + storeId);
        return storeHistory.getOrDefault(storeId, new ArrayList<>());
    }

    public List<Purchase> getUserPurchaseHistory(int userId) {
        UserController.LOGGER.info("userId: " + userId);
        return userHistory.getOrDefault(userId, new ArrayList<>());
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

    public synchronized String removePurchaseFromUser(int userId, int purchaseId) throws Exception {
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
        for (Integer storeId : storeHistory.keySet()) {
            for (Purchase purchase : storeHistory.get(storeId)) {
                info += purchase.FetchInfo() + "\n";
            }
        }
        return info;
    }
}
