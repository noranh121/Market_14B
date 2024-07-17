package org.market.DataAccessLayer.Entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;

@Embeddable
public class ProductScreenShotId implements Serializable{

    @JoinColumn(name = "purchaseID", referencedColumnName = "purchaseID")
    private Integer purchaseHistoryid;
    @JoinColumn(name="productID",referencedColumnName = "productID")
    private Integer productID;
    public Integer getPurchaseHistoryid() {
        return purchaseHistoryid;
    }
    public void setPurchaseHistoryid(Integer purchaseHistoryid) {
        this.purchaseHistoryid = purchaseHistoryid;
    }
    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }
}
