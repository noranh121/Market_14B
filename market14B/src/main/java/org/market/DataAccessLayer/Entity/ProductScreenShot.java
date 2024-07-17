package org.market.DataAccessLayer.Entity;

import java.io.Serializable;

import jakarta.persistence.*;

// import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductScreenShot implements Serializable{

    @EmbeddedId
    private ProductScreenShotId productScreenShotId;

    @Column(name="price")
    private double price;

    @Column(name="quantity")
    private int quantity;

    public Integer getProductID() {
        return productScreenShotId.getProductID();
    }

    public void setProductID(Integer productID) {
        if(productScreenShotId==null){
            this.productScreenShotId=new ProductScreenShotId();
        }
        this.productScreenShotId.setProductID(productID);
    }

    public Integer getPurchaseID() {
        return this.productScreenShotId.getPurchaseHistoryid();
    }
    public void setPurchaseID(Integer PurchaseID) {
        this.productScreenShotId.setPurchaseHistoryid(PurchaseID);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
