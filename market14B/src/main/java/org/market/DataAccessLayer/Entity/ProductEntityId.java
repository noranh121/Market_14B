package org.market.DataAccessLayer.Entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;

@Embeddable
public class ProductEntityId implements Serializable{

    @JoinColumn(name = "inventoryID", referencedColumnName = "inventoryID")
    private Integer inventoryID;
    @JoinColumn(name="productID",referencedColumnName = "productID")
    private Integer productID;

    
    public Integer getInventoryID() {
        return inventoryID;
    }
    public void setInventoryID(Integer inventoryID) {
        this.inventoryID = inventoryID;
    }
    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }
}
