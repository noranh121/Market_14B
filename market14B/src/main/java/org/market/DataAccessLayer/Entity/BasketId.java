package org.market.DataAccessLayer.Entity;
import java.io.Serializable;
import java.util.Objects;

public class BasketId implements Serializable {

    private String username;
    private Integer storeID;

    public BasketId() {
    }

    public BasketId(String username, Integer storeID) {
        this.username = username;
        this.storeID = storeID;
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
