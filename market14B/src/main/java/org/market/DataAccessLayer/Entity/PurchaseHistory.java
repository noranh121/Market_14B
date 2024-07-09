package org.market.DataAccessLayer.Entity;
import javax.persistence.*;

@Entity
//@Table(name="PurchaseHistory",catalog = "Market")
@Table(name="PurchaseHistory")
public class PurchaseHistory {

    @Id
    @Column(name = "purchaseID")
    private Integer purchaseID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="productID",referencedColumnName = "productID")
    // @JoinColumn(name = "productID")
    private Product productID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="storeID",referencedColumnName = "storeID")
    // @JoinColumn (name = "storeID")
    private Store storeID;

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

    public Product getProductID() {
        return productID;
    }

    public void setProductID(Product productID) {
        this.productID = productID;
    }

    public Store getStoreID() {
        return storeID;
    }

    public void setStoreID(Store storeID) {
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
