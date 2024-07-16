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

    @Id
    @JoinColumn(name="productID",referencedColumnName = "productID")
    private Integer productID;

    @Column(name="price")
    private double price;

    @Column(name="quantity")
    private double quantity;

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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

}
