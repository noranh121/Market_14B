package org.market.DomainLayer.backend.StorePackage.Discount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DiscountPolicyController extends CompositeDiscountPolicy{

    public enum LogicalRule{
        AND,
        OR,
        XOR,
        IF_THEN
    }

    protected List<CompositeDiscountPolicy> compositeDiscountPolicies;

    public DiscountPolicyController(int id) {
        super(id);
        compositeDiscountPolicies=Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public void addComposite(CompositeDiscountPolicy composite, int id) {
        if(this.getId()==id){
            this.compositeDiscountPolicies.add(composite);
            return;
        }
        for(CompositeDiscountPolicy compositeDiscountPolicy : compositeDiscountPolicies){
            compositeDiscountPolicy.addComposite(composite,id);  
        }
    }

    @Override
    public void removeComposite(int compositeDiscountPolicyId){
        if(this.getId()==compositeDiscountPolicyId){
            this.compositeDiscountPolicies=Collections.synchronizedList(new ArrayList<>());
            return;
        }
        for(CompositeDiscountPolicy compositeDiscountPolicy : compositeDiscountPolicies){
            if(compositeDiscountPolicy.getId()==compositeDiscountPolicyId){
                compositeDiscountPolicies.remove(compositeDiscountPolicy);
                return;
            }
        }
        for(CompositeDiscountPolicy compositeDiscountPolicy : compositeDiscountPolicies){
            if(compositeDiscountPolicy.getId()==compositeDiscountPolicyId){
                compositeDiscountPolicy.removeComposite(compositeDiscountPolicyId);
            }
        }

    }

    
}
