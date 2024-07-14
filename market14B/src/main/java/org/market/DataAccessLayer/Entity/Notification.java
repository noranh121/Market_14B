package org.market.DataAccessLayer.Entity;
// import javax.persistence.*;


import java.io.Serializable;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
//@Table(name="Notification",catalog = "Market")
@Table(name="Notifications")
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="notificationID")
    private Integer notificationID;

    @ManyToOne
    @JoinColumn(name="username", referencedColumnName = "username")
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
