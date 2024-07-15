package org.market.DataAccessLayer.Entity;

import java.util.ArrayList;
import java.util.List;

// import javax.persistence.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
//@Table(name="market",catalog = "Market")
@Table(name="market")
@NoArgsConstructor
@AllArgsConstructor
public class Market implements java.io.Serializable{
    @Id
    @Column(name="marketID")
    private Integer marketID=0;

    @Column(name="online")
    private Boolean online;

    @OneToMany(cascade = CascadeType.ALL, fetch =FetchType.LAZY)
    private List<User> systemManagers = new ArrayList<>();

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
