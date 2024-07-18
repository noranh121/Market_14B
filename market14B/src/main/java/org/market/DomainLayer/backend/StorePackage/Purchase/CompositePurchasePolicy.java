package org.market.DomainLayer.backend.StorePackage.Purchase;

import java.util.Map;

public abstract class CompositePurchasePolicy {

    private int id;

    public CompositePurchasePolicy(int id){
        this.id=id;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // products.entry = <productId,[quantity,price,weight]>
    public abstract Boolean purchase(Map<Integer, double[]> products, double age) throws Exception;

    public abstract void addComposite(CompositePurchasePolicy compositePurchasePolicy,int id);

    public abstract void removeComposite(int id);
}
