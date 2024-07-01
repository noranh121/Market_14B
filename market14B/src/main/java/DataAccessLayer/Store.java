package DataAccessLayer;
import javax.persistence.*;

@Entity
@Table(name="Store",catalog = "Market")
public class Store implements java.io.Serializable{

    @Id
    @Column(name="storeID")
    private int storeID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="firstOwnerName",referencedColumnName = "username")
    private int firstOwnerName;

}
