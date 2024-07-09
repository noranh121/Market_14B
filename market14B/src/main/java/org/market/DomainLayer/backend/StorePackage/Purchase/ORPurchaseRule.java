package org.market.DomainLayer.backend.StorePackage.Purchase;

import java.util.Map;

public class ORPurchaseRule extends PurchasePolicyController{

    public ORPurchaseRule(int id) {
        super(id);
    }

    @Override
    public Boolean purchase(Map<Integer, double[]> products, double age) throws Exception {
        for(CompositePurchasePolicy compositePurchasePolicy : compositePurchasePolicies){
            if(compositePurchasePolicy.purchase(products, age))
                return true;
        }
        return false;
    }

}