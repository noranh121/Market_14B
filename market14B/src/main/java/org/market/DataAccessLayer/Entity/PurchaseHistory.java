package org.market.DataAccessLayer.Entity;
import javax.persistence.*;

@Entity
@Table(name="PurchaseHistory",catalog = "Market")
public class PurchaseHistory {

    @Id
    @Column(name = "purchaseID")
    private Integer purchaseID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product",referencedColumnName = "productID")
    private Integer productID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="store",referencedColumnName = "storeID")
    private Integer storeID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user",referencedColumnName = "username")
    private String username;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Integer price;

    @Column(name = "ovlprice")
    private Integer ovlprice;

    public Integer getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(Integer purchaseID) {
        this.purchaseID = purchaseID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getOvlprice() {
        return ovlprice;
    }

    public void setOvlprice(Integer ovlprice) {
        this.ovlprice = ovlprice;
    }

    
}
