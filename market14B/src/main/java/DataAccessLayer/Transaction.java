package DataAccessLayer;
import javax.persistence.*;

@Entity
public class Transaction {
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="username",referencedColumnName = "username")
    @Column(name="username")
    private String username;

    @Id
    @Column(name="transactionID")
    private Integer transactionID;

    public Integer getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Integer transactionID) {
        this.transactionID = transactionID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
