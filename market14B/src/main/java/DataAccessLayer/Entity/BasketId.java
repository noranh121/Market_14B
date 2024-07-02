package DataAccessLayer.Entity;
import java.io.Serializable;
import java.util.Objects;

public class BasketId implements Serializable {

    private String username;
    private Long storeID;

    public BasketId() {
    }

    public BasketId(String username, Long storeID) {
        this.username = username;
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
