package DomainLayer.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import DomainLayer.backend.UserPackage.UserController;

public class PurchaseHistory {

    private static PurchaseHistory instance;
    private static final Logger LOGGER = Logger.getLogger(Purchase.class.getName());
    private static int counterID;

    private Map<Integer,List<Purchase>> storeHistory; //storeid ==> purchases
    private Map<Integer,List<Purchase>> userHistory;  //userid ==> purchases
    private Map<Integer,Purchase> allPurchases; //purchaseid ==> purchase

    private PurchaseHistory(){
        counterID = 0;
        storeHistory = new HashMap<>();
        userHistory = new HashMap<>();
    }
    public static synchronized PurchaseHistory getInstance() {
        if(instance == null){
            instance = new PurchaseHistory();
        }
        return instance;
    }


    //id's should be checked in an earlier stage
    public synchronized void addPurchase(int storeId, int userId, Purchase purchase) {
        // Add to all history
        purchase.setID(counterID++);
        allPurchases.put(purchase.getID(), purchase);

        // Add to store history
        storeHistory.computeIfAbsent(storeId, k -> new ArrayList<>()).add(purchase);
        LOGGER.info("Added purchase to store history: Store ID " + storeId);
    
        // Add to user history
        userHistory.computeIfAbsent(userId, k -> new ArrayList<>()).add(purchase);
        LOGGER.info("Added purchase to user history: User ID " + userId);
    }
    
        public List<Purchase> getStorePurchaseHistory(int storeId) {
            return storeHistory.getOrDefault(storeId, new ArrayList<>());
        }
    
        public List<Purchase> getUserPurchaseHistory(int userId) {
            return userHistory.getOrDefault(userId, new ArrayList<>());
        }
    
        // public synchronized void removePurchaseFromStore(int storeId, Purchase purchase) {
        //     List<Purchase> purchases = storeHistory.get(storeId);
        //     if (purchases != null && purchases.remove(purchase)) {
        //         LOGGER.info("Removed purchase from store history: Store ID " + storeId);
        //     } else {
        //         LOGGER.warning("Purchase not found in store history: Store ID " + storeId);
        //     }
        // }
    
        // public synchronized void removePurchaseFromUser(int userId, Purchase purchase) {
        //     List<Purchase> purchases = userHistory.get(userId);
        //     if (purchases != null && purchases.remove(purchase)) {
        //         LOGGER.info("Removed purchase from user history: User ID " + userId);
        //     } else {
        //         LOGGER.warning("Purchase not found in user history: User ID " + userId);
        //     }
        // }
}
