package org.market.Web.Requests;

public class cartOp {
    private String username;
    private Integer ProductId;
    private Integer StoreId;
    private Integer Quantity;


    public cartOp(String username,Integer quantity,Integer prodid,Integer storeid){
        this.username = username;
        this.ProductId = prodid;
        this.StoreId = storeid;
        this.Quantity = quantity;
    }

    public cartOp(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getProductId() {
        return ProductId;
    }

    public void setProductId(Integer productId) {
        ProductId = productId;
    }

    public Integer getStoreId() {
        return StoreId;
    }

    public void setStoreId(Integer storeId) {
        StoreId = storeId;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }
}
