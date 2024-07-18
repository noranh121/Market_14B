package org.market.Web.Requests;


public class ReqStore {

    private String username;
    private Integer productId;
    private Integer storeId;
    private Integer quantity;
    private Double price;
    private Double weight;


    public ReqStore(String username,Integer prodid,Integer storeid, Integer quant,Double price, Double weight){
        this.username = username;
        this.productId = prodid;
        this.storeId = storeid;
        this.quantity = quant;
        this.price = price;
        this.weight = weight;
    }

    public ReqStore(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
