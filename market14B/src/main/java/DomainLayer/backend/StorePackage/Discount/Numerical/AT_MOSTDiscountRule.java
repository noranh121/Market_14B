package DomainLayer.backend.StorePackage.Discount.Numerical;

import java.util.Map;

import DomainLayer.backend.StorePackage.Discount.CompositeDiscountPolicy;
import DomainLayer.backend.StorePackage.Discount.DiscountPolicyController;

public class AT_MOSTDiscountRule extends DiscountPolicyController{

    public AT_MOSTDiscountRule(int id) {
        super(id);
    }

    @Override
    public Map<Integer, double[]> calculateDiscount(Map<Integer, double[]> products) {
        double min=calculateTotal(products);
        for(CompositeDiscountPolicy compositeDiscountPolicy : compositeDiscountPolicies){
            if(min>calculateTotal(compositeDiscountPolicy.calculateDiscount(products))){
                products=compositeDiscountPolicy.calculateDiscount(products);
                min=calculateTotal(compositeDiscountPolicy.calculateDiscount(products));
            }
        }
        return products;
    }

}
