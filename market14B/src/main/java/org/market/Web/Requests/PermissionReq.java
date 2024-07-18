package org.market.Web.Requests;

public class PermissionReq {
    private int storeID;
    private String OwnerUserName;
    private String username;
    private Boolean isStoreOwner;

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getOwnerUserName() {
        return OwnerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        OwnerUserName = ownerUserName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getStoreOwner() {
        return isStoreOwner;
    }

    public void setStoreOwner(Boolean storeOwner) {
        isStoreOwner = storeOwner;
    }

    public Boolean getStoreManager() {
        return isStoreManager;
    }

    public void setStoreManager(Boolean storeManager) {
        isStoreManager = storeManager;
    }

    public Boolean[] getpType() {
        return pType;
    }

    public void setpType(Boolean[] pType) {
        this.pType = pType;
    }

    private Boolean isStoreManager;
    private Boolean[] pType;

    public PermissionReq(int storeID, String OwnerUserName, String username, Boolean storeOwner, Boolean storeManager, Boolean[] pType) {
        this.storeID = storeID;
        this.OwnerUserName = OwnerUserName;
        this.username = username;
        this.isStoreOwner = storeOwner;
        this.isStoreManager = storeManager;
        this.pType = pType;
    }

    public PermissionReq(){}



}