package DomainLayer.backend.StorePackage;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.LookAndFeel;

import DomainLayer.backend.ProductPackage.Inventory;

public class Store {
    private static final Logger LOGGER = Logger.getLogger(Store.class.getName());
    private int id;
    private Inventory inventory;
    private String firstOwnerName;
    private boolean active;
    private String description;


    public Store(String name, String Description,int id){
        this.id = id;
        this.firstOwnerName = name;
        this.description = Description;
        active = false;
        inventory = new Inventory();
    }
        // Getter and Setter for id
        public int getId() {
            return id;
        }
    
        public void setId(int id) {
            this.id = id;
        }
    
        // Getter and Setter for inventory
        public Inventory getInventory() {
            return inventory;
        }
    
        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }
    
        // Getter and Setter for firstOwnerName
        public String getFirstOwnerName() {
            return firstOwnerName;
        }
    
        public void setFirstOwnerName(String firstOwnerName) {
            this.firstOwnerName = firstOwnerName;
        }
    
        // Getter and Setter for active
        public boolean isActive() {
            return active;
        }

        // Getter and Setter for description
        public String getDescription() {
            return description;
        }
    
        public void setDescription(String description) {
            this.description = description;
        }

    //TODO
//    public String openStore() {
//        // new store
//        return "";
//    }
    public void AddProduct(int productId,Double price,int quantity) throws Exception {
        if(price <= 0 || quantity < 0){
            LOGGER.severe("price is <=0 or quantity < 0 while trying to add product");
            throw new Exception("Price cannot be zero or less, quantity can only be 0 or positive!");
        }
        inventory.AddProduct(productId, price, quantity);
        LOGGER.info("product added successfully");
    }
    public void RemoveProduct(int productId) {
        inventory.RemoveProduct(productId);
        LOGGER.info("product removed successfully");
    }
//    public String EditProductName(int productId,int storeId,String newName) {
//
//    }


    public void EditProductPrice(int productId,int storeId,double newPrice) throws Exception {
        if(newPrice <= 0){
            LOGGER.severe("price is <=0 while trying to edit price");
            throw new Exception("Price cannot be zero or less!");
        }
        inventory.EditProductPrice(productId, newPrice);
        LOGGER.info("Product's price edited successfully");
    }
    public void EditProductQuantity(int productId,int newQuantity) throws Exception {
        if(newQuantity < 0){
            LOGGER.severe("quantity is < 0 while trying to edit quantity");
            throw new Exception("Quantity cannot negative!");
        }
        inventory.EditProductQuantity(productId, newQuantity);
        LOGGER.info("Product's price edited successfully");
    }
//    public String AssignStoreOwner(int storeId,int userID) {
//
//    }
//    public String AssignStoreManager(int storeId,int userID) {
//
//    }
//    public String EditPermissions(Enum permission) {
//
//    }
    public void CloseStore() {
        this.active = false;
    }
    public void OpenStore() {
        this.active = true;
    }
    public Inventory getInfo() { //inventory for now!
        return this.inventory;
    }


    
}
