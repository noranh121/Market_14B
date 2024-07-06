package org.market.DomainLayer.backend.StorePackage.Purchase;

import java.util.Map;

public class ANDPurchaseRule extends PurchasePolicyController{

    public ANDPurchaseRule(int id) {
        super(id);
    }

    @Override
    public Boolean purchase(Map<Integer, double[]> products, double age) throws Exception{
        for(CompositePurchasePolicy compositePurchasePolicy : compositePurchasePolicies){
            if(!compositePurchasePolicy.purchase(products, age))
                return false;
        }
        return true;
    }

}
