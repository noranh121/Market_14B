package org.market.DataAccessLayer.Entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;


@Embeddable
public class ProductBasketId implements Serializable{


    @JoinColumn(name="productID",referencedColumnName = "productID")
    private Integer prodID;
    @JoinColumn(name = "username", referencedColumnName = "username")
    private String username;
    @JoinColumn(name = "storeID", referencedColumnName = "storeID")
    private Integer storeID;

    
    public Integer getProdID() {
        return prodID;
    }
    public void setProdID(Integer prodID) {
        this.prodID = prodID;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getStoreID() {
        return storeID;
    }
    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    
}
