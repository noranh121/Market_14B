package org.market.DomainLayer.backend.StorePackage.Discount.Logical;

import java.util.Map;

import org.market.DomainLayer.backend.StorePackage.Discount.CompositeDiscountPolicy;
import org.market.DomainLayer.backend.StorePackage.Discount.DiscountPolicyController;

public class ORDiscountRule extends DiscountPolicyController {

    public ORDiscountRule(int id) {
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
