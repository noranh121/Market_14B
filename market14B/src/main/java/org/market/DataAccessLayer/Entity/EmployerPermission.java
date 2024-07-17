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

    @EmbeddedId
    private PermissionId permissionId;

    @JoinColumn(name = "username")
    private String parentusername;

    @OneToMany(fetch=FetchType.EAGER)
    private List<EmployerPermission> employees;

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

    
    public PermissionId getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(PermissionId permissionId) {
        this.permissionId = permissionId;
    }

    public String getParentusername() {
        return parentusername;
    }

    public void setParentusername(String parentusername) {
        this.parentusername = parentusername;
    }

    public List<EmployerPermission> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployerPermission> employees) {
        this.employees = employees;
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

    public void deleteEmployees(){
        for(EmployerPermission employee : employees){
            employee.deleteEmployees();
            org.market.DomainLayer.backend.Market.getDC().getEmployerPermissionRepository().delete(employee);
        }
    }


}
