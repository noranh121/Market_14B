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

    @Override
    public void addComposite(CompositePurchasePolicy composite,int id){
        if(this.getId()==id){
            this.compositePurchasePolicies.add(composite);
            return;
        }
        for(CompositePurchasePolicy compositePurchasePolicy : compositePurchasePolicies){
            compositePurchasePolicy.addComposite(composite,id);  
        }
    }

    @Override
    public void removeComposite(int compositePurchasePolicyId){
        if(this.getId()==compositePurchasePolicyId){
            this.compositePurchasePolicies=Collections.synchronizedList(new ArrayList<>());
            return;
        }
        for(CompositePurchasePolicy compositePurchasePolicy : compositePurchasePolicies){
            if(compositePurchasePolicy.getId()==compositePurchasePolicyId){
                compositePurchasePolicies.remove(compositePurchasePolicy);
                return;
            }
        }
        for(CompositePurchasePolicy compositePurchasePolicy : compositePurchasePolicies){
            if(compositePurchasePolicy.getId()==compositePurchasePolicyId){
                compositePurchasePolicy.removeComposite(compositePurchasePolicyId);
                return;
            }
        }
    }
}
