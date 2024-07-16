package org.market.DataAccessLayer.Entity;

// import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;


@Entity
//@Table(name="Basket",catalog = "Market")
@Table(name = "Basket")
//@IdClass(BasketId.class)
@NoArgsConstructor
@AllArgsConstructor
public class Basket implements java.io.Serializable{

    // @Id
    // @ManyToOne(fetch=FetchType.LAZY)
    // @JoinColumn(name="user",referencedColumnName = "username")
    // //private User username; //this should maybe be the object itself (User user)

    // @Id
    // @ManyToOne(fetch=FetchType.LAZY)
    // @JoinColumn(name="store",referencedColumnName = "storeID")
    // private Store storeID;
    @EmbeddedId
    private BasketId basketId;


    @Column(name = "products")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductBasket> products = new ArrayList<>(); //<product,quantity>

    public BasketId getBasketId() {
        return basketId;
    }

    public void setBasketId(BasketId basketId) {
        this.basketId = basketId;
    }

    public String getUsername() {
        return basketId.getUsername();
    }

    public void setUsername(User username) {
        this.basketId.setUsername(username);
    }

    public Integer getStoreID() {
        return basketId.getStoreID();
    }

    public void setStoreID(Store storeID) {
        this.basketId.setStoreID(storeID);
    }

    public List<ProductBasket> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBasket> products) {
        this.products = products;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Basket basket = (Basket) o;
        return Objects.equals(basketId.getUsername() , basket.getUsername()) &&
                Objects.equals(basketId.getStoreID(), basket.getStoreID());
    }

}
