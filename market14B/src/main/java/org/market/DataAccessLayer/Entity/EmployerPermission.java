package org.market.DataAccessLayer.Entity;

import java.io.Serializable;
import java.util.List;
// import javax.persistence.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EmployerPermission implements Serializable{

    @Id
    @JoinColumn(name="storeID",referencedColumnName = "storeID")
    private Store storeID;

    public Store getStoreID() {
        return storeID;
    }

    public void setStoreID(Store storeID) {
        this.storeID = storeID;
    }

    @JoinColumn(name = "username", referencedColumnName = "username")
    private User username;

    @JoinColumn(name = "username", referencedColumnName = "username")
    private User parentusername;

    public User getParentusername() {
        return parentusername;
    }

    public void setParentusername(User parentusername) {
        this.parentusername = parentusername;
    }

    @OneToMany(mappedBy = "username", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmployerPermission> employees;

    public List<EmployerPermission> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployerPermission> employees) {
        this.employees = employees;
    }

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

    public EmployerPermission findEmployee(String username) {
        if(this.username.getUsername().equals(username)){
            return this;
        }
        for(EmployerPermission emplyee : employees){
            EmployerPermission found=emplyee.findEmployee(username);
            if(found!=null){
                return found;
            }
        }
        return null;
    }

    public Boolean deleteEmployee(String username) {
        for (int i = 0; i < employees.size(); i++) {
            EmployerPermission child = employees.get(i);
            if (child.getUsername().getUsername().equals(username)) {
                employees.remove(i);
                return true;
            }
        }
        for (EmployerPermission child : employees) {
            if (child.deleteEmployee(username)) {
                return true;
            }
        }
        return false;
    }

}
