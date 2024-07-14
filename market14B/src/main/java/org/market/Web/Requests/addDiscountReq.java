package org.market.Web.Requests;

public class addDiscountReq {
    String username;
    int storeId;
    String logicalRule;
    // int id;

    public addDiscountReq() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getLogicalRule() {
        return logicalRule;
    }

    public void setLogicalRule(String logicalRule) {
        this.logicalRule = logicalRule;
    }

    
}
