package org.market.DomainLayer.backend.StorePackage;

import org.market.DomainLayer.backend.ProductPackage.Inventory;
import org.market.DomainLayer.backend.ProductPackage.Product;
import org.market.DomainLayer.backend.StorePackage.Discount.CompositeDiscountPolicy;
import org.market.DomainLayer.backend.StorePackage.Discount.DiscountPolicyController;
import org.market.DomainLayer.backend.StorePackage.Discount.Logical.ANDDiscountRule;
import org.market.DomainLayer.backend.StorePackage.Purchase.ANDPurchaseRule;
import org.market.DomainLayer.backend.StorePackage.Purchase.CompositePurchasePolicy;
import org.market.DomainLayer.backend.StorePackage.Purchase.Offer;
import org.market.DomainLayer.backend.StorePackage.Purchase.PurchasePolicyController;
import org.market.Web.DTOS.OfferDTO;
import org.market.Web.DTOS.ProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Store {
    private int id;
    private String name;
    private Inventory inventory;
    private String firstOwnerName;
    private boolean active;
    private String description;
    private double rating; // 0 - 5
    private DiscountPolicyController compositeDiscountPolicy;
    private PurchasePolicyController compositePurchasePolicy;
    private final Lock storeLock = new ReentrantLock();
    private List<Offer> offers;

    private int discountPolicyIDCounter;
    private int purchasePolicyIDCounter;

    public Store(String username, String name, String Description, int id) {
        this.id = id;
        this.name = name;
        this.firstOwnerName = username;
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
        offers = new ArrayList<>();
    }
    
    // Discount Policy
    public DiscountPolicyController getCompositeDiscountPolicy(){
        return compositeDiscountPolicy;
    }

    public void addDiscountComposite(CompositeDiscountPolicy discountPolicy,int id){
        discountPolicy.setId(discountPolicyIDCounter);
        compositeDiscountPolicy.addComposite(discountPolicy,id);
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

    public void addPurchaseComposite(CompositePurchasePolicy purchasePolicy,int id){
        purchasePolicy.setId(purchasePolicyIDCounter);
        compositePurchasePolicy.addComposite(purchasePolicy,id);
        purchasePolicyIDCounter++;
    }

    public void removePurchaseComposite(int purchasePolicyId) throws Exception{
        compositePurchasePolicy.removeComposite(purchasePolicyId);
    }

    public Boolean purchase(Map<Integer, double[]> products, double age) throws Exception{
        return compositePurchasePolicy.purchase(products, age);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double[] bring(int prodid) {
        double [] info = new double[2];
        double price = inventory.getPrice(prodid);
        if(price == -1){
            info[0] = -1;
            info[1] = -1;
        }else{
            info[0] = price;
            info[1] = this.id;
        }
        return info;
    }

    public List<ProductDTO> bringProds() {
        List<ProductDTO> prods = new ArrayList<>();
        Map<Product,Double> map = inventory.gatherProds();
        for(Map.Entry<Product, Double> entry: map.entrySet()){
            double price = entry.getValue();
            ProductDTO pdto = new ProductDTO(entry.getKey(),price , this.id);
            prods.add(pdto);
        }
        return prods;
    }

    public List<OfferDTO> bringOffers() {
        List<OfferDTO> offersDTOs = new ArrayList<>();
        for (Offer offer : offers) {
            OfferDTO dto = new OfferDTO();
            dto.setName(offer.getProductName());
            dto.setOffer(offer.getOfferPrice());
            dto.setPrice(offer.getPrice());
            dto.setProductId(offer.getProductId());
            dto.setUsername(offer.getUsername());
            offersDTOs.add(dto);
        }
        return offersDTOs;
    }

    public String sendOffer(int productId, String productName ,String username, Double price, Double offerPrice) {
        Offer offer = new Offer(productId, productName ,username, price, offerPrice, this.id);
        offers.add(offer);
        return "sent";
    }

    public Offer getOffer(String username, int productId) {
        for (Offer offer: offers) {
            if (offer.getUsername() == username && offer.getProductId() == productId) {
                return offer;
            }
        }
        return null;
    }

    public int approveOffer(int num, String username, int productId) {
        Offer offer = getOffer(username, productId);
        offer.addNumOfApprovals();
        if (offer.getVotes() == num) {
            if (offer.getNumOfApprovals() == offer.getVotes())
                return 1; // the offer was accepted by everyone
            else
                return -1; // the offer was regected by some
        }
        return 0; // not everyone voted yet
    }

    public int rejectOffer(int num, String username, int productId) {
        Offer offer = getOffer(username, productId);
        offer.addnumOfRejections();
        if (offer.getVotes() == num) {
            if (offer.getnumOfRejections() == offer.getVotes())
                return -1; // the offer was accepted by everyone
            else
                return 1; // the offer was rejected by some
        }
        return 0; // not everyone voted yet
    }

}
