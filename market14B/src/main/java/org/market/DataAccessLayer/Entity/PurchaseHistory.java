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

    @Column(name = "products")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductScreenShot> products;

    @JoinColumn(name="storeID")
    private Integer storeID;

    @JoinColumn(name="username")
    private String username;


    @Column(name = "ovlprice")
    private Integer ovlprice;

    public Integer getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(Integer purchaseID) {
        this.purchaseID = purchaseID;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getOvlprice() {
        return ovlprice;
    }

    public void setOvlprice(Integer ovlprice) {
        this.ovlprice = ovlprice;
    }

    public List<ProductScreenShot> getProducts() {
        return products;
    }

    public void setProducts(List<ProductScreenShot> products) {
        this.products = products;
    }    
}
