package DataAccessLayer;
import javax.persistence.*;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;

@Entity
@Table(name="SubCategory",catalog = "Market")
public class SubCategory implements java.io.Serializable{

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="categoryID",referencedColumnName = "categoryID")
    private Category category;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="subCategoryID",referencedColumnName = "categoryID")
    private Category subCategory;
}
