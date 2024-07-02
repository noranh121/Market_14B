package DataAccessLayer.Entity;
import javax.persistence.*;

@Entity
@Table(name="Category",catalog = "Market")
public class Category implements java.io.Serializable{

    @Id
    @Column(name="categoryID")
    private Integer categoryID;
    
    @Column(name="categoryName")
    private String categoryName;

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
