package org.market.DataAccessLayer.Entity;

import javax.persistence.*;

@Entity
//@Table(name="Product",catalog = "Market")
@Table(name="Product")
public class Product implements java.io.Serializable {
    
    @Id 
    @Column(name = "productID")
    private Integer productID;

    @Column(name = "productName")
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "brand")
    private String brand;

    @Column(name = "weight")
    private Double weight;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="catagory",referencedColumnName = "categoryID")
    private Integer catagoryID;

    public Integer getProductID() {
        return productID;
    }
    public void setProductID(Integer productID) {
        this.productID = productID;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public Double getWeight() {
        return weight;
    }
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    public Integer getCatagoryID() {
        return catagoryID;
    }
    public void setCatagoryID(Integer catagoryID) {
        this.catagoryID = catagoryID;
    }

}
