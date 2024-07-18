package org.market.DataAccessLayer.Entity;

import java.util.List;

// import javax.persistence.*;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Entity
//@Table(name="Category",catalog = "Market")
@Table(name="Category_")
@NoArgsConstructor
@AllArgsConstructor
public class Category implements java.io.Serializable{

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="categoryID")
    private Integer categoryID;
    
    @Column(name="categoryName")
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "parentCategoryID")
    private Category parentCategory;
    
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
    private List<Category> subCategories;

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
