package DomainLayer.backend.StorePackage.Discount.Logical;

import java.util.Map;

import DomainLayer.backend.StorePackage.Discount.CompositeDiscountPolicy;
import DomainLayer.backend.StorePackage.Discount.DiscountPolicyController;

public class XORDiscountRule extends DiscountPolicyController{

    public XORDiscountRule(int id) {
        super(id);
    }

    @Override
    public Map<Integer, double[]> calculateDiscount(Map<Integer, double[]> products) {
        Map<Integer, double[]> temp=products;
        for(CompositeDiscountPolicy compositeDiscountPolicy : compositeDiscountPolicies){
            products=compositeDiscountPolicy.calculateDiscount(products);
            if(temp!=products){
                return products;
            }
        }
        return products;
    }

}
