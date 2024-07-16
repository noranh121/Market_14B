package org.market.DomainLayer.backend.StorePackage;

import org.market.Web.DTOS.OfferDTO;
import org.market.Web.DTOS.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Component("BackendStoreController")
public class StoreController {
    //private static StoreController instance;
//    @Autowired
//    private DataController dataController;
    public static final Logger LOGGER = Logger.getLogger(StoreController.class.getName());

    private Map<Integer, Store> stores;
    private int idCounter;


    private StoreController() {
        idCounter = 0;
        stores = new ConcurrentHashMap<>();
        try {
            FileHandler fileHandler = new FileHandler("StorePackage", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set up logger handler.", e);
        }
    }

    public Store getStore(int storeID) {
        LOGGER.info("storeID: " + storeID);
        if (!stores.containsKey(storeID)) {
            // check and get from dataBase
            // if exists add Store to Map and return Store;
//            org.market.DataAccessLayer.Entity.Store storeEntity = dataController.getStore(storeID);
//            Store store = new Store(storeEntity.getName(),storeEntity.getDesciption(),storeEntity.getStoreID());
//            stores.put(storeEntity.getStoreID(),store);
            // else:
            LOGGER.severe("Trying to retrieve A non existing StoreID");
            return null;
        }
        return stores.get(storeID);
    }

    public String addStore(String username, String name, String Description) {
        // add to database
        Store store = new Store(name, name, Description, idCounter);
        stores.put(idCounter, store);
        idCounter++;
        LOGGER.info("store added");
        return "store added";

    }

    public String removeStore(int id) throws Exception {
        // remove from database
        if (!stores.containsKey(id)) {
            LOGGER.severe("store is not existed");
            throw new Exception("store is not existed");
        } else {
            stores.remove(id);
            LOGGER.info("store removed");
            return "store removed";
        }
    }

    public String addProduct(int productId, int storeId, double price, int quantity,double weight) throws Exception {
        // checkStore(storeID)
        LOGGER.info(
                "productId: " + productId + ", storeId " + storeId + ", price: " + price + ", quantity: " + quantity);
        Store store = getStore(storeId);
        if (store != null) {
            store.AddProduct(productId, price, quantity,weight);
            LOGGER.info("Product added to store Successfully");
            return "Product added to store Successfully";
        }
        return "store does not exist";
    }

    public String removeProduct(int productId, int storeId) {
        // checkStore(storeID)
        LOGGER.info("productId: " + productId + ", storeId " + storeId);
        Store store = getStore(storeId);
        if (store != null) {
            store.RemoveProduct(productId);
            LOGGER.info("Product Removed from store Successfully");
        }
        return "Product Removed from store Successfully";
    }

    public String EditProductName(int productId, int storeId, String newName) {
        return null;
    }

    public String EditProducPrice(int productId, int storeId, Double newPrice) throws Exception {
        // checkStore(storeID)
        LOGGER.info("productId: " + productId + ", storeId " + storeId + ", newPrice: " + newPrice);
        Store store = getStore(storeId);
        if (store != null) {
            store.EditProductPrice(productId, newPrice);
            LOGGER.info("Product's Price Modified in store Successfully");
        }
        return "Product's Price Modified in store Successfully";
    }

    public String EditProductQuantity(int productId, int storeId, int newQuantity) throws Exception {
        // checkStore(storeID)
        LOGGER.info("productId: " + productId + ", storeId " + storeId + ", newQuantity: " + newQuantity);
        Store store = getStore(storeId);
        if (store != null) {
            store.EditProductQuantity(productId, newQuantity);
            LOGGER.info("Product's Quantity Modified in store Successfully");
        }
        return "Product's Quantity Modified in store Successfully";
    }

    public String closeStore(int storeId) throws Exception {
        // checkStore(storeID)
        LOGGER.info("storeId: " + storeId);
        Store store = getStore(storeId);
        if (store != null) {
            if(!store.isActive()){
                throw new Exception("Store is already closed.");
            }
            store.CloseStore();
            LOGGER.info("Store Closed Successfuly");
        }
        return "Store Closed Successfuly";
    }

    public String openStore(int storeId) throws Exception {
        // checkStore(storeID)
        LOGGER.info("storeId: " + storeId);
        Store store = getStore(storeId);
        if (store != null) {
            if(store.isActive()){
                throw new Exception("Store is already opened.");
            }
            store.OpenStore();
            LOGGER.info("Store Opened Successfuly");
        }
        return "Store Opened Successfuly";
    }

    public String deleteStore(int storeId) {
        LOGGER.info("storeId: " + storeId);
        Store store = getStore(storeId);
        if (store != null) {
            stores.remove(storeId);
            LOGGER.info("store deleted successfully");
            return "store deleted successfully";
        }
        LOGGER.severe("store does not exist");
        return "store does not exist";
    }

    public String getInfo(int storeId) {
        // checkStore(storeID)
        LOGGER.info("storeId: " + storeId);
        Store store = getStore(storeId);
        if (store != null) {
            LOGGER.info("Store Info fetched!");
            return store.getInfo();
        }
        return "No such store!";
    }

    public int initStore(String userName, String name, String Description) {
        LOGGER.info("userName: " + userName + ", Description: " + Description);
        int id = idCounter;
        Store newStore = new Store(userName, name, Description, idCounter);
        stores.put(id, newStore);
        // database
        idCounter++;
        return id;
    }

    // remove store ...

    public Map<Integer, Store> GetStores() {
        return stores;
    }

    // this is for testing
    public void setToNull() {
        //instance = null;
        stores.clear();
    }

//    public List<Store> getAllStores() {
//        List<org.market.DataAccessLayer.Entity.Store> dalstrs =  DataController.getinstance().getAllStores();
//        List<Store> storesToReturn = new ArrayList<>();
//        for(org.market.DataAccessLayer.Entity.Store storeEntity: dalstrs){
//            Store store = new Store(storeEntity.getName(),storeEntity.getDesciption(),storeEntity.getStoreID());
//            storesToReturn.add(store);
//        }
//        return storesToReturn;
//    }

    public List<Store> getAllStores() {
        return new ArrayList<Store>(stores.values());
    }

    public double [] getProdInfo(int prodid){
        for(Store s: stores.values()){
            double [] info = s.bring(prodid);
            if(info[0] != -1) return info;
        }
        return new double[]{-1, -1};
    }

    public List<ProductDTO> getStoreProducts(int store_id) {
        if(stores.containsKey(store_id)){
            return stores.get(store_id).bringProds();
        }
        return new ArrayList<>();
    }

    public List<OfferDTO> getStoreOffers(int storeId) {
        if(stores.containsKey(storeId)){
            return stores.get(storeId).bringOffers();
        }
        return new ArrayList<>();
    }

    public String sendOffer(int productId, String productName ,String username, Double price, Double offerPrice, int storeId) {
        if(stores.containsKey(storeId)){
            return stores.get(storeId).sendOffer(productId, productName ,username, price, offerPrice);
        }
        return "No such store!";
    }

    public int approveOffer(int num , String username, int storeId, int productId) {
        if(stores.containsKey(storeId)){
            return stores.get(storeId).approveOffer(num, username,productId);
        }
        return 0;
    }

    public int rejectOffer(int numOfStoreOwners, String username, int storeId, int productId) {
        if(stores.containsKey(storeId)){
            return stores.get(storeId).rejectOffer(numOfStoreOwners, username,productId);
        }
        return 0;
    }

    public double [] getProdInfo(int prodid, int storeid){
        for(Store s: stores.values()){
            if(s.getId() == storeid){
                double [] info = s.bring(prodid);
                if(info[0] != -1) return info;
            }
        }
        return new double[]{-1, -1};
    }
}
