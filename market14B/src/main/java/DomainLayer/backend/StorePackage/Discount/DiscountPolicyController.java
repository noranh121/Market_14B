package DomainLayer.backend.StorePackage.Discount;

import java.util.HashSet;

public abstract class DiscountPolicyController extends CompositeDiscountPolicy{

    public enum LogicalRule{
        AND,
        OR,
        XOR,
        IF_THEN
    }

    protected HashSet<CompositeDiscountPolicy> compositeDiscountPolicies;

    public DiscountPolicyController(int id) {
        super(id);
        compositeDiscountPolicies=new HashSet<>();
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
