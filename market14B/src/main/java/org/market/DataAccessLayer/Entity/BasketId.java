package org.market.DataAccessLayer.Entity;
import java.io.Serializable;
import java.util.Objects;

// import javax.persistence.Embeddable;
// import javax.persistence.JoinColumn;

import jakarta.persistence.*;


@Embeddable
public class BasketId implements Serializable {

    @JoinColumn(name = "username", referencedColumnName = "username")
    private String username;
    @JoinColumn(name = "storeID", referencedColumnName = "storeID")
    private Integer storeID;

    public BasketId() {
    }

    public BasketId(User username, Store storeID) {
        this.username = username.getUsername();
        this.storeID = storeID.getStoreID();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username.getUsername();
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Store storeID) {
        this.storeID = storeID.getStoreID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketId basketId = (BasketId) o;
        return Objects.equals(username, basketId.username) &&
               Objects.equals(storeID, basketId.storeID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, storeID);
    }

}
