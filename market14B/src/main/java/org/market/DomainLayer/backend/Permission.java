package org.market.DomainLayer.backend;

public class Permission {
    public static enum permissionType {
        editProducts(0),
        addOrEditPurchaseHistory(1),
        addOrEditDiscountHistory(2);

        public int index;

        permissionType(int index) {
            this.index = index;
        }
    }

    private String userName;
    private Boolean storeOwner;
    private Boolean storeManager;
    private Boolean[] PType;

    public Permission(String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) {
        this.userName = userName;
        this.storeOwner = storeOwner;
        this.storeManager = storeManager;
        this.PType = pType;
    }

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
}
