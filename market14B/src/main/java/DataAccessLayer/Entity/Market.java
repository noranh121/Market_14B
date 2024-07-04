package DataAccessLayer.Entity;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="User",catalog = "Market")
public class Market {
    @Id
    @Column(name="marketID")
    private Integer marketID=0;

    @Column(name="online")
    private Boolean online;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Market")
    private List<User> systemManagers;

    public Integer getMarketID() {
        return marketID;
    }

    public List<User> getSystemManagers() {
        return systemManagers;
    }

    public void setSystemManagers(List<User> systemManagers) {
        this.systemManagers = systemManagers;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

}
