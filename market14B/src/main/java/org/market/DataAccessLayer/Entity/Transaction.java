package org.market.DataAccessLayer.Entity;
import javax.persistence.*;

@Entity
public class Transaction {
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="username",referencedColumnName = "username")
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
