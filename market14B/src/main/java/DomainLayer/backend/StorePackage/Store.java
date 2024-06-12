package DomainLayer.backend.StorePackage;

import java.util.Map;

import DomainLayer.backend.ProductPackage.Inventory;

public class Store {
    private int id;
    private Inventory inventory;
    private String firstOwnerName;
    private boolean active;
    private String description;
    private double rating; // 0 - 5

    public Store(String name, String Description, int id) {
        this.id = id;
        this.firstOwnerName = name;
        this.description = Description;
        active = false;
        inventory = new Inventory();
        this.rating = 0;
    }

    // Getter and Setter for rating
    public double getRating() {
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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
            StoreController.LOGGER.severe("price is <=0 or quantity < 0 while trying to add product");
            throw new Exception("Price cannot be zero or less, quantity can only be 0 or positive!");
        }
        inventory.AddProduct(productId, price, quantity);
        StoreController.LOGGER.info("product added successfully");
    }

    public void RemoveProduct(int productId) {
        inventory.RemoveProduct(productId);
        StoreController.LOGGER.info("product removed successfully");
    }

    public void EditProductPrice(int productId, double newPrice) throws Exception {
        if (newPrice <= 0) {
            StoreController.LOGGER.severe("price is <=0 while trying to edit price");
            throw new Exception("Price cannot be zero or less!");
        }
        inventory.EditProductPrice(productId, newPrice);
        StoreController.LOGGER.info("Product's price edited successfully");
    }

    public void EditProductQuantity(int productId, int newQuantity) throws Exception {
        if (newQuantity < 0) {
            StoreController.LOGGER.severe("quantity is < 0 while trying to edit quantity");
            throw new Exception("Quantity cannot negative!");
        }
        inventory.EditProductQuantity(productId, newQuantity);
        StoreController.LOGGER.info("Product's quantity edited successfully");
    }

    public void subQuantity(int productId, int quantity) throws Exception {
        if (quantity < 0) {
            StoreController.LOGGER.severe("quantity is < 0 while trying to edit quantity");
            throw new Exception("Quantity cannot be negative!");
        }
        inventory.subQuantity(productId, quantity);
        StoreController.LOGGER.info("Product's quantity subtracted successfully");
    }

    public void addQuantity(int productId, int quantity) throws Exception {
        if (quantity < 0) {
            StoreController.LOGGER.severe("quantity is < 0 while trying to edit quantity");
            throw new Exception("Quantity cannot be negative!");
        }
        inventory.subQuantity(productId, quantity);
        StoreController.LOGGER.info("Product's quantity added successfully");
    }

    public double getProdPrice(Integer p) {
        double price = inventory.getPrice(p);
        if (price == -1) {
            StoreController.LOGGER.warning("Product doesn't appear to be the inventory of the store!");
        } else {
            StoreController.LOGGER.info("Price of Product fetched successfully");
        }
        return price;
    }

    public boolean check(Map<Integer, Integer> products) {
        for (Map.Entry<Integer, Integer> entry : products.entrySet()) {
            int quant = inventory.getQuantity(entry.getKey());
            if (quant > entry.getValue()) {
                StoreController.LOGGER.severe("one of the products's quantity exceeds the availiable stock");
                return false;
            }
        }
        StoreController.LOGGER.info("Basket's contents are available in the store");
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
