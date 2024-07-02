package DataAccessLayer.Entity;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name="Inventory",catalog = "Market")
public class Inventory implements Serializable{

    @Id
    @JoinColumn(name="storeID",referencedColumnName = "storeID")
    private Store storeID;

    @OneToMany(mappedBy = "Inventory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductEntity> products;

    public Store getStoreID() {
        return storeID;
    }

    public void setStoreID(Store storeID) {
        this.storeID = storeID;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
}
