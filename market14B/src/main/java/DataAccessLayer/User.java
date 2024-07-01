package DataAccessLayer;
import javax.persistence.*;

@Entity
@Table(name="User",catalog = "Market")
public class User implements java.io.Serializable{

    @Id
    @Column(name="username")
    private String username;
    
    @Column(name="password")
    private String password;

    @Column(name="age")
    private double age;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public double getAge() {
        return age;
    }
    public void setAge(double age) {
        this.age = age;
    }


}
