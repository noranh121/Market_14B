package main.Web.Requests;

public class PermissionReq {
    private int storeID;
    private String OwnerUserName;
    private String username;
    private boolean storeOwner;
    private boolean storeManager;
    private boolean[] pType;

    public PermissionReq(int storeID, String OwnerUserName, String username, boolean storeOwner, boolean storeManager, boolean[] pType) {
        this.storeID = storeID;
        this.OwnerUserName = OwnerUserName;
        this.username = username;
        this.storeOwner = storeOwner;
        this.storeManager = storeManager;
        this.pType = pType;
    }

    public int getStoreID() {
        return storeID;
    }
    
    public String getOwnerUserName() {
        return OwnerUserName;
    }
    
    public String getUsername() {
        return username;
    }
    
    public boolean isStoreOwner() {
        return storeOwner;
    }
    
    public boolean isStoreManager() {
        return storeManager;
    }
    
    public boolean[] getPType() {
        return pType;
    }
    
}
