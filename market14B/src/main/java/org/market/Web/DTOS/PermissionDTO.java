package org.market.Web.DTOS;

import org.market.DomainLayer.backend.Permission;

public class PermissionDTO{
    private String userName;
    private Boolean storeOwner;
    private Boolean storeManager;
    private Boolean[] PType;
    private int storeId;

    public PermissionDTO(Permission permission, int storeId){
        this.userName = permission.getUserName();
        this.storeOwner = permission.getStoreOwner();
        this.storeManager = permission.getStoreManager();
        this.PType = new Boolean[permission.getPType().length];
        for (int i = 0; i < permission.getPType().length; i++) {
            this.PType[i] = permission.getPType()[i];
        }
        this.storeId = storeId;
    }

    public PermissionDTO(){}

    public String getUserName() {
        return userName;
    }

    public Boolean getStoreOwner() {
        return storeOwner;
    }

    public Boolean getStoreManager() {
        return storeManager;
    }

    public Boolean[] getPType() {
        return PType;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStoreOwner(Boolean storeOwner) {
        this.storeOwner = storeOwner;
    }

    public void setStoreManager(Boolean storeManager) {
        this.storeManager = storeManager;
    }

    public void setPType(Boolean[] PType) {
        this.PType = PType;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeid) {
        this.storeId = storeid;
    }

}