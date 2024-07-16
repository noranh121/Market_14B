package org.market.Web.Requests;

public class OfferReq {
    private int storeId;
    private int productId;
    private String username;
    private String offerUsername;
    private double offer;

    public OfferReq(){}

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOfferUsername() {
        return offerUsername;
    }

    public void setOfferUsername(String offerUsername) {
        this.offerUsername = offerUsername;
    }

    public double getOffer() {
        return offer;
    }

    public void setOffer(double offer) {
        this.offer = offer;
    }
}
