package DataAccessLayer.Entity;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;


@Entity
@Table(name="Basket",catalog = "Market")
@IdClass(BasketId.class)
public class Basket implements java.io.Serializable{

    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user",referencedColumnName = "username")
    private String username; //this should maybe be the object itself (User user)

    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="store",referencedColumnName = "storeID")
    private Integer storeID;

    @Column(name = "products")
    private Map<Product, Integer> products = new HashMap<>(); //<product,quantity>

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

}
