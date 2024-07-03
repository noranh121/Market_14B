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
    private String username; //this should maybe be the object itself (User user)

    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="store",referencedColumnName = "storeID")
    private Integer storeID;

    @Column(name = "products")
    private List<ProductEntity> products = new ArrayList<>(); //<product,quantity>

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

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

}
