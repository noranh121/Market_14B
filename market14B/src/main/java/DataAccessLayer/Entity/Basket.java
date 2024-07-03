package DataAccessLayer.Entity;

import java.util.*;
import javax.persistence.*;


@Entity
@Table(name="Basket",catalog = "Market")
@IdClass(BasketId.class)
public class Basket implements java.io.Serializable{

    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user",referencedColumnName = "username")
    private User username; //this should maybe be the object itself (User user)

    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="store",referencedColumnName = "storeID")
    private Store storeID;

    @Column(name = "products")
    private List<ProductEntity> products = new ArrayList<>(); //<product,quantity>

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Basket basket = (Basket) o;
        return Objects.equals(username, basket.username) &&
                Objects.equals(storeID, basket.storeID);
    }

}
