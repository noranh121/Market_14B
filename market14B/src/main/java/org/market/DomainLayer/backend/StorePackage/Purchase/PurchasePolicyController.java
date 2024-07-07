package org.market.DomainLayer.backend.StorePackage.Purchase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PurchasePolicyController extends CompositePurchasePolicy{

    protected List<CompositePurchasePolicy> compositePurchasePolicies;

    public PurchasePolicyController(int id){
        super(id);
        compositePurchasePolicies=Collections.synchronizedList(new ArrayList<>());
    }

    public void addComposite(CompositePurchasePolicy compositePurchasePolicy){
        compositePurchasePolicies.add(compositePurchasePolicy);
    }

    public void removeComposite(int compositePurchasePolicyId) throws Exception{
        for(CompositePurchasePolicy compositePurchasePolicy : compositePurchasePolicies){
            if(compositePurchasePolicy.getId()==compositePurchasePolicyId)
                compositePurchasePolicies.remove(compositePurchasePolicy);
        }
        throw new Exception(compositePurchasePolicyId+" is not existed");
    }
}
