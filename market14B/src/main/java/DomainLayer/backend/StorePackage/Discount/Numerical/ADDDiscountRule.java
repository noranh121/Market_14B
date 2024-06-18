package DomainLayer.backend.StorePackage.Discount.Numerical;

import java.util.Map;

import DomainLayer.backend.StorePackage.Discount.CompositeDiscountPolicy;
import DomainLayer.backend.StorePackage.Discount.DiscountPolicyController;

public class ADDDiscountRule extends DiscountPolicyController{

    public ADDDiscountRule(int id) {
        super(id);
    }

    @Override
    public Map<Integer, double[]> calculateDiscount(Map<Integer, double[]> products) {
        for(CompositeDiscountPolicy compositeDiscountPolicy : compositeDiscountPolicies){
            products=compositeDiscountPolicy.calculateDiscount(products);
        }
        return products;
    }

}
