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
    private Integer permissionId;

    @JoinColumn(name="storeID")
    private Integer storeID;

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    @JoinColumn(name = "username")
    private String username;

    @JoinColumn(name = "username")
    private String parentusername;

    public String getParentusername() {
        return parentusername;
    }

    public void setParentusername(String parentusername) {
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

    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
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
        if(this.username.equals(username)){
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
            if (child.getUsername().equals(username)) {
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
