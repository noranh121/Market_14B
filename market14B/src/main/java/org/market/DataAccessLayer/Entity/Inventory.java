package org.market.DataAccessLayer.Entity;

import java.io.Serializable;
import java.util.*;

// import javax.persistence.*;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
//@Table(name="Inventory",catalog = "Market")
@Table(name="Inventory")
@NoArgsConstructor
@AllArgsConstructor
public class Inventory implements Serializable{

    @Id
    @JoinColumn(name="storeID",referencedColumnName = "storeID")
    // @JoinColumn(name="storeID")
    private Store storeID;

    @Column(name = "products")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductEntity> products;

    public Store getStoreID() {
        return storeID;
    }

    public void setStoreID(Store storeID) {
        this.storeID = storeID;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    public void addProduct(Product product, double price, int quantity) {
        ProductEntity pe = new ProductEntity();
        pe.setPrice(price);
        pe.setProductID(product);
        pe.setQuantity(quantity);
        this.products.add(pe);
    }

    public void removeProduct(Product product) {
        products.remove(products.get(product.getProductID()));
    }

    public void editPrice(Product product, Double newPrice) {
        for (ProductEntity pe : products) {
            if (pe.getProductID().getProductID() == product.getProductID()) {
                pe.setPrice(newPrice);
            }
        }
    }

    public void editQuantity(Product product, Integer quantity) {
        for (ProductEntity pe : products) {
            if (pe.getProductID().getProductID() == product.getProductID()) {
                pe.setQuantity(quantity);
            }
        }
    }
}
