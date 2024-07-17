package org.market.DataAccessLayer.Entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "DiscountPolicy")
@NoArgsConstructor
@AllArgsConstructor
public class DiscountPolicy implements java.io.Serializable {
    
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer discountId;

    @Column(name="type")
    private String type;

    @Column
    private Boolean standard;
    
    @Column
    private double conditionalPrice;
    
    @Column
    private double conditionalQuantity;
    
    @Column
    private double discountPercentage;
    
    @JoinColumn(name = "storeID", referencedColumnName = "storeID")
    private Integer storeId;
    
    @Column
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column
    private Integer categoryId;

    @Column
    private Integer productId;

    @ManyToOne
    @JoinColumn(name = "parentDiscountID")
    private DiscountPolicy parentDiscount;

    @OneToMany(mappedBy = "parentDiscount", cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private List<DiscountPolicy> subDiscounts;

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getStandard() {
        return standard;
    }

    public void setStandard(Boolean standard) {
        this.standard = standard;
    }

    public double getConditionalPrice() {
        return conditionalPrice;
    }

    public void setConditionalPrice(double conditionalPrice) {
        this.conditionalPrice = conditionalPrice;
    }

    public double getConditionalQuantity() {
        return conditionalQuantity;
    }

    public void setConditionalQuantity(double conditionalQuantity) {
        this.conditionalQuantity = conditionalQuantity;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Store storeId) {
        this.storeId = storeId.getStoreID();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public DiscountPolicy getParentDiscount() {
        return parentDiscount;
    }

    public void setParentDiscount(DiscountPolicy parentDiscount) {
        this.parentDiscount = parentDiscount;
    }

    public List<DiscountPolicy> getSubDiscounts() {
        return subDiscounts;
    }

    public void setSubDiscounts(List<DiscountPolicy> subDiscounts) {
        this.subDiscounts = subDiscounts;
    }

        
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }


}
