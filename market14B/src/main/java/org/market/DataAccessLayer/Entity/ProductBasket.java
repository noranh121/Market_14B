package org.market.DataAccessLayer.Entity;

import java.io.Serializable;

import jakarta.persistence.*;

// import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductBasket implements Serializable{

    // @Id
    // @JoinColumn(name="productID",referencedColumnName = "productID")
    // private Integer productID;
    @EmbeddedId
    private ProductBasketId productBasketId;

    @Column(name="price")
    private double price;

    @Column(name="quantity")
    private int quantity;

    public Integer getProductID() {
        return productBasketId.getProdID();
    }
    public String getUsername() {
        return productBasketId.getUsername();
    }
    public void setUsername(String username) {
        this.productBasketId.setUsername(username);
    }
    public Integer getStoreID() {
        return this.productBasketId.getStoreID();
    }
    public void setStoreID(Integer storeID) {
        if (this.productBasketId==null) {
            this.productBasketId=new ProductBasketId();
        }
        this.productBasketId.setStoreID(storeID);
    }

    public void setProductID(Product productID) {
        if (this.productBasketId==null) {
            this.productBasketId=new ProductBasketId();
        }
        this.productBasketId.setProdID(productID.getProductID());
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
