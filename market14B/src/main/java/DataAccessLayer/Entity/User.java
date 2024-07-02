package DataAccessLayer.Entity;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Basket> baskets = new ArrayList<>();
    
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
