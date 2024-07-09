package org.market.DataAccessLayer.Entity;
import javax.persistence.*;

@Entity
@Table(name = "Transaction")
public class Transaction {
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="username",referencedColumnName = "username")
    // @JoinColumn(name="username")
    private User user;

    @Id
    @Column(name="transactionID")
    private Integer transactionID;

    public Integer getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Integer transactionID) {
        this.transactionID = transactionID;
    }

    public User getUser() {
        return user;
    }

    public void setUsername(User user) {
        this.user = user;
    }
}
