package DataAccessLayer.Entity;
import javax.persistence.*;

@Entity
@Table(name="Notification",catalog = "Market")
public class Notification {

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user",referencedColumnName = "username")
    private String username;

    @Column(name = "message")
    private String message;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
}
