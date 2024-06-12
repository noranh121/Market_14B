package DomainLayer.backend.StorePackage;

import java.util.Map;

public class StoreDiscount extends DiscountPolicy{

    public StoreDiscount(Discount discountType, double discountPercentage) {
        super(discountType, discountPercentage);
    }

    @Override
    public double calculateDiscount(Map<Integer, double[]> products) {
        double total=0,quantity,price;
        for(Map.Entry<Integer, double[]> entry : products.entrySet()){
            quantity=entry.getValue()[0];
            price=entry.getValue()[1];
            total=total+discountType.calculateDiscount(discountPercentage, price,quantity);
        }
        return total;
    }

}
