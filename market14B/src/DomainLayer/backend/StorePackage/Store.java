package DomainLayer.backend.StorePackage;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import DomainLayer.backend.ProductPackage.Inventory;
import DomainLayer.backend.ProductPackage.Product;

public class Store {
    private static final Logger LOGGER = Logger.getLogger(Store.class.getName());
    private int id;
    private Inventory inventory;
    private String firstOwnerName;
    private boolean active;
    private String description;
    private double rating;

    public Store(String name, String Description, int id) {
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

    public void AddProduct(int productId, Double price, int quantity) throws Exception {
        if (price <= 0 || quantity < 0) {
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

    public void EditProductPrice(int productId, double newPrice) throws Exception {
        if (newPrice <= 0) {
            LOGGER.severe("price is <=0 while trying to edit price");
            throw new Exception("Price cannot be zero or less!");
        }
        inventory.EditProductPrice(productId, newPrice);
        LOGGER.info("Product's price edited successfully");
    }

    public void EditProductQuantity(int productId, int newQuantity) throws Exception {
        if (newQuantity < 0) {
            LOGGER.severe("quantity is < 0 while trying to edit quantity");
            throw new Exception("Quantity cannot negative!");
        }
        inventory.EditProductQuantity(productId, newQuantity);
        LOGGER.info("Product's price edited successfully");
    }


    public double getProdPrice(Product p){
        double price = inventory.getPrice(p.getId());
        if(price == -1){
            LOGGER.warning("Product doesn't appear to be the inventory of the store!");
        }else{
            LOGGER.info("Price of Product fetched successfully");
        }
        return price;
    }

    public boolean check(Map<Product,Integer> products){
        for(Map.Entry<Product,Integer> entry: products.entrySet()){
            int quant = inventory.getQuantity(entry.getKey().getId());
            if(quant > entry.getValue()){
                 LOGGER.severe("one of the products's quantity exceeds the availiable stock");
                 return false;
            }
        }
        LOGGER.info("Basket's contents are available in the store");
        return true;
    }


    public void CloseStore() {
        this.active = false;
    }

    public void OpenStore() {
        this.active = true;
    }

    public String getInfo() {
        return this.inventory.fetchInfo();
    }

}
