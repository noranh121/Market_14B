package org.market.DataAccessLayer.Entity;
import javax.persistence.*;

@Entity
//@Table(name="Notification",catalog = "Market")
@Table(name="Notification")
public class Notification {

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user")
    private User username;

    @Column(name = "message")
    private String message;

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
}
