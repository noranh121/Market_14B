package org.market.DataAccessLayer.Entity;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name="Users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements java.io.Serializable{

    @Id
    @Column(name="username")
    private String username;
    
    @Column(name="password")
    private String password;

    @Column(name="age")
    private double age;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Basket> baskets ;
    @Column(name="LoggedIn")
    private Boolean LoggedIn;

    @Column(name="suspended")
    private Boolean suspended;

    @Column(name = "systemManager")
    private Boolean flag;
    
    public Boolean getSuspended() {
        return suspended;
    }
    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }
    public Boolean getLoggedIn() {
        return LoggedIn;
    }
    public void setLoggedIn(Boolean loggedIn) {
        LoggedIn = loggedIn;
    }
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

    public List<Basket> getBaskets() {
        return baskets;
    }
    public void setBaskets(List<Basket> baskets) {
        this.baskets = baskets;
    }
    public Boolean getFlag() {
        return flag;
    }
    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
    

}
