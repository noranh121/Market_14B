package org.market.DataAccessLayer.Entity;
// import javax.persistence.*;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Entity
//@Table(name="Store",catalog = "Market")
@Table(name="Store")
@NoArgsConstructor
@AllArgsConstructor
public class Store implements java.io.Serializable{

    @Id
    @Column(name="storeID")
    private Integer storeID;

    @ManyToOne(fetch=FetchType.LAZY)
    //@JoinColumn(name="firstOwnerName",referencedColumnName = "username")
    @JoinColumn(name="username")
    private User firstOwner;

    @Column(name = "name")
    private String name;

    @Column(name="active")
    private boolean active;

    @Column(name="description")
    private String desciption;

    @Column(name="rating")
    private int rating;

    // @OneToOne(mappedBy = "Store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private Inventory inventory;

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public User getFirstOwner() {
        return firstOwner;
    }

    public String getName() {
        return name;
    }

    public void setFirstOwner(User firstOwner) {
        this.firstOwner = firstOwner;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    // public Inventory getInventory() {
    //     return inventory;
    // }

    // public void setInventory(Inventory inventory) {
    //     this.inventory = inventory;
    // }
}