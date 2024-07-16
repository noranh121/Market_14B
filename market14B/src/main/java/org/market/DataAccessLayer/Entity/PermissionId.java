package org.market.DataAccessLayer.Entity;

import java.io.Serializable;
import java.util.Objects;

// import javax.persistence.Embeddable;
// import javax.persistence.JoinColumn;

import jakarta.persistence.*;

@Embeddable
public class PermissionId implements Serializable {

    @JoinColumn(name = "username", referencedColumnName = "username")
    private String username;
    @JoinColumn(name = "storeID", referencedColumnName = "storeID")
    private Integer storeID;

    public PermissionId(){}

    public PermissionId(User username,Store store){
        this.username=username.getUsername();
        this.storeID=store.getStoreID();
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getStoreID() {
        return storeID;
    }
    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

}
