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

    public void addComposite(CompositeDiscountPolicy composite){
        compositeDiscountPolicies.add(composite);
    }

    public void removeComposite(int compositeDiscountPolicyId) throws Exception{
        for(CompositeDiscountPolicy compositeDiscountPolicy : compositeDiscountPolicies){
            if(compositeDiscountPolicy.getId()==compositeDiscountPolicyId)
                compositeDiscountPolicies.remove(compositeDiscountPolicy);
        }
        throw new Exception(compositeDiscountPolicyId+" is not existed");
    }

    
}
