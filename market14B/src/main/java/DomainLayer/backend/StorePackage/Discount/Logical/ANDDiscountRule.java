package DomainLayer.backend.StorePackage.Discount.Logical;

import java.util.Map;

import DomainLayer.backend.StorePackage.Discount.CompositeDiscountPolicy;
import DomainLayer.backend.StorePackage.Discount.DiscountPolicyController;

public class ANDDiscountRule extends DiscountPolicyController{

    public ANDDiscountRule(int id) {
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
