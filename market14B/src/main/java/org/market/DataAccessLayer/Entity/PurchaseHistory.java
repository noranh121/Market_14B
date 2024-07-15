package org.market.DataAccessLayer.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="PurchaseHistory")
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseHistory implements java.io.Serializable {

    @Id
    @Column(name = "purchaseID")
    private Integer purchaseID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="productID",referencedColumnName = "productID")
    private Product productID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="storeID",referencedColumnName = "storeID")
    private Store storeID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="username",referencedColumnName = "username")
    private User username;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Integer price;

    @Column(name = "ovlprice")
    private Integer ovlprice;

    public Integer getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(Integer purchaseID) {
        this.purchaseID = purchaseID;
    }

    public Product getProductID() {
        return productID;
    }

    public void setProductID(Product productID) {
        this.productID = productID;
    }

    public Store getStoreID() {
        return storeID;
    }

    public void setStoreID(Store storeID) {
        this.storeID = storeID;
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getOvlprice() {
        return ovlprice;
    }

    public void setOvlprice(Integer ovlprice) {
        this.ovlprice = ovlprice;
    }

    
}
