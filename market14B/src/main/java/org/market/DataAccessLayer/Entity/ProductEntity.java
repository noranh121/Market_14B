package org.market.DataAccessLayer.Entity;

import java.io.Serializable;

import jakarta.persistence.*;

// import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity implements Serializable{

    // @Id
    // @JoinColumn(name="productID",referencedColumnName = "productID")
    // private Integer productID;
    @EmbeddedId
    private ProductEntityId inventoryProductId;

    @Column(name="price")
    private double price;

    @Column(name="quantity")
    private int quantity;

    public Integer getProductID() {
        return inventoryProductId.getProductID();
    }

    public void setProductID(Product productID) {
        if(inventoryProductId==null){
            this.inventoryProductId=new ProductEntityId();
        }
        this.inventoryProductId.setProductID(productID.getProductID());
    }

    public Integer getInventoryID() {
        return this.inventoryProductId.getInventoryID();
    }
    public void setInventoryID(Integer inventoryID) {
        this.inventoryProductId.setInventoryID(inventoryID);
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
