package org.market.DataAccessLayer.Entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
public class ProductEntity implements Serializable{

    @Id
    @JoinColumn(name="productID",referencedColumnName = "productID")
    private Product productID;

    @Column(name="price")
    private double price;

    @Column(name="quantity")
    private double quantity;

    public Product getProductID() {
        return productID;
    }

    public void setProductID(Product productID) {
        this.productID = productID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

}
