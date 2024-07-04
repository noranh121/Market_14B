package DataAccessLayer.Entity;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="Category",catalog = "Market")
public class Category implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="categoryID")
    private Integer categoryID;
    
    @Column(name="categoryName")
    private String categoryName;

    // @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Category> subCategories;

    // @ManyToOne
    // @JoinColumn(name = "parentCategoryID")
    // private Category parentCategory;

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

    // public List<Category> getSubCategories() {
    //     return subCategories;
    // }

    // public void setSubCategories(List<Category> subCategories) {
    //     this.subCategories = subCategories;
    // }

    // public Category getParentCategory() {
    //     return parentCategory;
    // }

    // public void setParentCategory(Category parentCategory) {
    //     this.parentCategory = parentCategory;
    // }

}
