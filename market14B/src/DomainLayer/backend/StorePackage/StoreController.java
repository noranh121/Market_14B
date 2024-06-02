package DomainLayer.backend.StorePackage;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class StoreController {
    private static StoreController instance;
    public static final Logger LOGGER = Logger.getLogger(StoreController.class.getName());
    

    private Map<Integer,Store> stores;
    private int idCounter;

    public static synchronized StoreController getInstance() {
        if(instance == null){
            instance = new StoreController();
        }
        return instance;
    }

    private StoreController(){
        idCounter = 0;
        stores = new HashMap<>();
        try{
            FileHandler fileHandler = new FileHandler("StorePackage", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set up logger handler.", e);
        }
    }


    // public Store checkStore(int storeID){
    //     if(!stores.containsKey(storeID)){
    //         //check and get from dataBase
    //         //if exists return Store;
    //         //else:
    //         LOGGER.severe("Trying to retrieve A non existing StoreID");
    //     }
    //     return null
    // }

    public Store getStore(int storeID) {
        LOGGER.info("storeID: " + storeID);
        if(!stores.containsKey(storeID)){
            //check and get from dataBase
            //if exists add Store to Map and return Store;
            //else:
            LOGGER.severe("Trying to retrieve A non existing StoreID");
            return null;
        }
        return stores.get(storeID);
    }

    // public String addStore(){

    // }

    // public String removeStore(){

    // }

    public String addProduct(int productId, int storeId, double price, int quantity) throws Exception {
        //checkStore(storeID)
        LOGGER.info("productId: " + productId + ", storeId " + storeId+", price: "+price+", quantity: "+quantity);
        Store store = getStore(storeId);
        if(store != null){
            store.AddProduct(productId, price, quantity);
            LOGGER.info("Product added to store Successfully");
        }
        return "Product added to store successfully";
    }

    public String removeProduct(int productId, int storeId) {
            //checkStore(storeID)
        LOGGER.info("productId: " + productId + ", storeId "+storeId);
            Store store = getStore(storeId);
            if(store != null){
                store.RemoveProduct(productId);
                LOGGER.info("Product Removed from store Successfully");
            }
            return "Product Removed from store Successfully";
    }

    public String EditProductName(int productId, int storeId, String newName) {
        return null;
    }

    public String EditProducPrice(int productId, int storeId, Double newPrice) throws Exception {
           //checkStore(storeID)
        LOGGER.info("productId: " + productId + ", storeId "+storeId+", newPrice: "+newPrice);
           Store store = getStore(storeId);
           if(store != null){
               store.EditProductPrice(productId, newPrice);
               LOGGER.info("Product's Price Modified in store Successfully");
           }
           return "Product's Price Modified in store Successfully";        
    }

    public String EditProductQuantity(int productId, int storeId, int newQuantity) throws Exception {
           //checkStore(storeID)
        LOGGER.info("productId: " + productId + ", storeId "+storeId+", newQuantity: "+newQuantity);
           Store store = getStore(storeId);
           if(store != null){
               store.EditProductQuantity(productId, newQuantity);
               LOGGER.info("Product's Quantity Modified in store Successfully");
           }
           return "Product's Quantity Modified in store Successfully";
    }

    public String closeStore(int storeId) {
           //checkStore(storeID)
        LOGGER.info("storeId: "+storeId);
           Store store = getStore(storeId);
           if(store != null){
               store.CloseStore();
               LOGGER.info("Store Closed Successfuly");
           }
           return "Store Closed Successfuly";
    }

    public String openStore(int storeId) {
           //checkStore(storeID)
        LOGGER.info("storeId: "+storeId);
           Store store = getStore(storeId);
           if(store != null){
               store.OpenStore();
               LOGGER.info("Store Opened Successfuly");
           }
           return "Store Opened Successfuly";
    }

    public String getInfo(int storeId) {
           //checkStore(storeID)
        LOGGER.info("storeId: "+storeId);
           Store store = getStore(storeId);
           if(store != null){
               LOGGER.info("Store Info fetched!");
               return store.getInfo();
           }
           return "No such store!";
    }

    public int initStore(String userName, String Description) {
        LOGGER.info("userName: "+userName+", Description: "+Description);
        int id = idCounter;
        Store newStore = new Store(userName, userName, idCounter);
        stores.put(id, newStore);
        //database
        idCounter++;
        return id;
    }

    
    //remove store ...

}
