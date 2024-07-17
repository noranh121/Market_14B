package org.market.DataAccessLayer.Entity;

import java.io.Serializable;
import java.util.*;

import org.springframework.context.annotation.DependsOn;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Inventory_")
@NoArgsConstructor
@AllArgsConstructor
public class Inventory implements Serializable{

    @Id
    private Integer inventoryID;

    //@OneToOne
    //@MapsId
    @JoinColumn(name="storeID")
    private Integer storeID;
    // @Id
    // @JoinColumn(name="storeID",referencedColumnName = "storeID")
    // private Store storeID;

    @Column(name = "products")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductEntity> products;

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Store storeID) {
        this.storeID = storeID.getStoreID();
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    public void addProduct(Product product, double price, int quantity) {
        ProductEntity pe = new ProductEntity();
        pe.setPrice(price);
        pe.setProductID(product.getProductID());
        pe.setInventoryID(inventoryID);
        pe.setQuantity(quantity);
        if (this.products==null) {
            this.products=new ArrayList<>();
        }
        this.products.add(pe);
    }

    public double getProdPrice(Integer prodID){
        for(ProductEntity prod: products ){
            if(prod.getProductID() == prodID){
                return prod.getPrice();
            }
        }
        return -1;
    }

    public void removeProduct(Product product) {
        products.remove(products.get(product.getProductID()));
    }

    public void editPrice(Product product, Double newPrice) {
        for (ProductEntity pe : products) {
            if (pe.getProductID() == product.getProductID()) {
                pe.setPrice(newPrice);
            }
        }
    }

    public void editQuantity(Product product, Integer quantity) {
        for (ProductEntity pe : products) {
            if (pe.getProductID()== product.getProductID()) {
                pe.setQuantity(pe.getQuantity()-quantity);
            }
        }
    }

    public Integer getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(Integer inventoryID) {
        this.inventoryID = inventoryID;
    }
}