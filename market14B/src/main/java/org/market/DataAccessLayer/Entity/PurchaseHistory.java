package org.market.DataAccessLayer.Entity;

import java.util.List;

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
    @Column
    private List<ProductEntity> products;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="storeID",referencedColumnName = "storeID")
    private Store storeID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="username",referencedColumnName = "username")
    private User username;


    @Column(name = "ovlprice")
    private Integer ovlprice;

    public Integer getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(Integer purchaseID) {
        this.purchaseID = purchaseID;
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

    public Integer getOvlprice() {
        return ovlprice;
    }

    public void setOvlprice(Integer ovlprice) {
        this.ovlprice = ovlprice;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }    
}
