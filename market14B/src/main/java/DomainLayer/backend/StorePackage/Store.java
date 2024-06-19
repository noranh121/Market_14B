package DomainLayer.backend.StorePackage;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import DomainLayer.backend.ProductPackage.Inventory;
import DomainLayer.backend.StorePackage.Discount.CompositeDiscountPolicy;
import DomainLayer.backend.StorePackage.Discount.DiscountPolicyController;
import DomainLayer.backend.StorePackage.Discount.Logical.ANDDiscountRule;
import DomainLayer.backend.StorePackage.Purchase.ANDPurchaseRule;
import DomainLayer.backend.StorePackage.Purchase.CompositePurchasePolicy;
import DomainLayer.backend.StorePackage.Purchase.PurchasePolicyController;

public class Store {
    private int id;
    private Inventory inventory;
    private String firstOwnerName;
    private boolean active;
    private String description;
    private double rating; // 0 - 5
    private DiscountPolicyController compositeDiscountPolicy;
    private PurchasePolicyController compositePurchasePolicy;
    private final Lock storeLock = new ReentrantLock();

    private int discountPolicyIDCounter;
    private int purchasePolicyIDCounter;

    public Store(String name, String Description, int id) {
        this.id = id;
        this.firstOwnerName = name;
        this.description = Description;
        active = false;
        inventory = new Inventory();
        this.rating = 0;
        discountPolicyIDCounter=0;
        purchasePolicyIDCounter=0;
        compositeDiscountPolicy=new ANDDiscountRule(discountPolicyIDCounter);
        compositePurchasePolicy=new ANDPurchaseRule(purchasePolicyIDCounter);
        discountPolicyIDCounter++;
        purchasePolicyIDCounter++;
    }
    
    // Discount Policy
    public DiscountPolicyController getCompositeDiscountPolicy(){
        return compositeDiscountPolicy;
    }

    public void addDiscountComposite(CompositeDiscountPolicy discountPolicy){
        discountPolicy.setId(discountPolicyIDCounter);
        compositeDiscountPolicy.addComposite(discountPolicy);
        discountPolicyIDCounter++;
    }

    public void removeDiscountComposite(int discountPolicyId) throws Exception{
        compositeDiscountPolicy.removeComposite(discountPolicyId);
    }
    
    public double calculateDiscount(Map<Integer, double[]> products){
        products=compositeDiscountPolicy.calculateDiscount(products);
        return compositeDiscountPolicy.calculateTotal(products);
    }

    // Purchase Policy
    public PurchasePolicyController getCompositePurchasePolicy() {
        return compositePurchasePolicy;
    }

    public void addPurchaseComposite(CompositePurchasePolicy purchasePolicy){
        purchasePolicy.setId(purchasePolicyIDCounter);
        compositePurchasePolicy.addComposite(purchasePolicy);
        purchasePolicyIDCounter++;
    }

    public void removePurchaseComposite(int purchasePolicyId) throws Exception{
        compositePurchasePolicy.removeComposite(purchasePolicyId);
    }

    public Boolean purchase(Map<Integer, double[]> products, double age){
        return compositePurchasePolicy.purchase(products, age);
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

    public void AddProduct(int productId, Double price, int quantity,double weight) throws Exception {
        if (price <= 0 || quantity < 0) {
            StoreController.LOGGER.severe("price is <=0 or quantity < 0 while trying to add product");
            throw new Exception("Price cannot be zero or less, quantity can only be 0 or positive!");
        }
        inventory.AddProduct(productId, price, quantity,weight);
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
        inventory.addQuantity(productId, quantity);
        StoreController.LOGGER.info("Product's quantity added successfully");
    }

    public double getProdWeight(Integer p){
        double weight = inventory.getWeight(p);
        if (weight == -1) {
            StoreController.LOGGER.warning("Product doesn't appear to be the inventory of the store!");
        } else {
            StoreController.LOGGER.info("Weight of Product fetched successfully");
        }
        return weight;
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
            if (quant < entry.getValue()) {
                StoreController.LOGGER.severe("one of the products's quantity exceeds the availiable stock");
                return false;
            }
        }
        StoreController.LOGGER.info("Basket's contents are available in the store");
        return true;
    }

    public void CloseStore() {
        storeLock.lock();
        try{
            this.active = false;
        }finally{
            storeLock.unlock();
        }
    }

    public void OpenStore() {
        storeLock.lock();
        try{
            this.active = true;
        }finally{
            storeLock.unlock();
        }
    }

    public Lock getLock(){
        return storeLock;
    }

    public String getInfo() {
        return this.inventory.fetchInfo();
    }

}
