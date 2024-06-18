package Web.Requests;

public class PermissionReq {
    private int storeID;
    private String OwnerUserName;
    private String username;
    private Boolean storeOwner;
    private Boolean storeManager;
    private Boolean[] pType;

    public PermissionReq(int storeID, String OwnerUserName, String username, Boolean storeOwner, Boolean storeManager, Boolean[] pType) {
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
    
    public Boolean isStoreOwner() {
        return storeOwner;
    }
    
    public Boolean isStoreManager() {
        return storeManager;
    }
    
    public Boolean[] getPType() {
        return pType;
    }
    
}
