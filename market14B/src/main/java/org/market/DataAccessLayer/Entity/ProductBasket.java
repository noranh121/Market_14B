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

    @Id
    @JoinColumn(name="productID",referencedColumnName = "productID")
    private Integer productID;

    @Column(name="price")
    private double price;

    @Column(name="quantity")
    private int quantity;

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Product productID) {
        this.productID = productID.getProductID();
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
