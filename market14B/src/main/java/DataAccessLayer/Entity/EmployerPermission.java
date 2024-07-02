package DataAccessLayer.Entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
public class EmployerPermission implements Serializable{

    @Id
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User username;

    @Column(name="storeOwner")
    private Boolean storeOwner;

    @Column(name="storeManager")
    private Boolean storeManager;

    @Column(name="editProducts")
    private Boolean editProducts;

    @Column(name="addOrEditPurchaseHistory")
    private Boolean addOrEditPurchaseHistory;
    
    @Column(name="addOrEditDiscountHistory")
    private Boolean addOrEditDiscountHistory;

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public Boolean getStoreOwner() {
        return storeOwner;
    }

    public void setStoreOwner(Boolean storeOwner) {
        this.storeOwner = storeOwner;
    }

    public Boolean getStoreManager() {
        return storeManager;
    }

    public void setStoreManager(Boolean storeManager) {
        this.storeManager = storeManager;
    }

    public Boolean getEditProducts() {
        return editProducts;
    }

    public void setEditProducts(Boolean editProducts) {
        this.editProducts = editProducts;
    }

    public Boolean getAddOrEditPurchaseHistory() {
        return addOrEditPurchaseHistory;
    }

    public void setAddOrEditPurchaseHistory(Boolean addOrEditPurchaseHistory) {
        this.addOrEditPurchaseHistory = addOrEditPurchaseHistory;
    }

    public Boolean getAddOrEditDiscountHistory() {
        return addOrEditDiscountHistory;
    }

    public void setAddOrEditDiscountHistory(Boolean addOrEditDiscountHistory) {
        this.addOrEditDiscountHistory = addOrEditDiscountHistory;
    }

}
