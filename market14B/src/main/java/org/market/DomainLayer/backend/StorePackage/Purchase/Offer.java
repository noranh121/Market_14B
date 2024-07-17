package org.market.DomainLayer.backend.StorePackage.Purchase;

public class Offer {

    private int productId;
    private String productName;
    private String username;
    private Double price;
    private Double offerPrice;
    private int storeId;
    private int numOfApprovals = 0;
    private int numOfRejections = 0;
    private int votes = 0;

    public Offer(int productId, String productName ,String username, Double price, Double offerPrice,int storeId) {
        this.productId = productId;
        this.productName = productName;
        this.username = username;
        this.price = price;
        this.offerPrice = offerPrice;
        this.storeId = storeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Double offerPrice) {
        this.offerPrice = offerPrice;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getNumOfApprovals() {
        return numOfApprovals;
    }

    public void addNumOfApprovals() {
        this.numOfApprovals++;
        this.votes++;
    }

    public int getnumOfRejections() {
        return numOfRejections;
    }

    public void addnumOfRejections() {
        this.numOfRejections++;
        this.votes++;
    }

    public int getVotes() {
        return votes;
    }

}
