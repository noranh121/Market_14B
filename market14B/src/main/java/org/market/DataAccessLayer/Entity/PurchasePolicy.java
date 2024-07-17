package org.market.DataAccessLayer.Entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "PurchasePolicy")
@NoArgsConstructor
@AllArgsConstructor
public class PurchasePolicy implements java.io.Serializable{

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer purchaseId;

    @Column
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JoinColumn(name = "storeID", referencedColumnName = "storeID")
    private Integer storeId;
    
    @Column
    private int quantity;
    
    @Column
    private double price;
    
    @Column
    private String date;
    
    @Column
    private int atLeast;
    
    @Column
    private double weight;
    
    @Column
    private double age;

    @Column
    private double userAge;
    
    public double getUserAge() {
        return userAge;
    }

    public void setUserAge(double userAge) {
        this.userAge = userAge;
    }

    @Column
    private int categoryId;

    @Column
    private int productId;
    
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Column
    private String username;
    
    @Column
    private Boolean immediate;

    @ManyToOne
    @JoinColumn(name = "parentPurchaseID")
    private PurchasePolicy parentPurchase;

    @OneToMany(mappedBy = "parentPurchase", cascade = CascadeType.ALL)
    private List<PurchasePolicy> subPurchases;

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Store storeId) {
        this.storeId = storeId.getStoreID();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAtLeast() {
        return atLeast;
    }

    public void setAtLeast(int atLeast) {
        this.atLeast = atLeast;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getImmediate() {
        return immediate;
    }

    public void setImmediate(Boolean immediate) {
        this.immediate = immediate;
    }

    public PurchasePolicy getParentPurchase() {
        return parentPurchase;
    }

    public void setParentPurchase(PurchasePolicy parentPurchase) {
        this.parentPurchase = parentPurchase;
    }

    public List<PurchasePolicy> getSubPurchases() {
        return subPurchases;
    }

    public void setSubPurchases(List<PurchasePolicy> subPurchases) {
        this.subPurchases = subPurchases;
    }
}
