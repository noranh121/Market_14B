package DomainLayer.backend.StorePackage;

import java.util.Map;

public abstract class DiscountPolicy {
    protected Discount discountType;
    protected double discountPercentage;

    public DiscountPolicy(Discount discountType,double discountPercentage){
        this.discountType=discountType;
        this.discountPercentage=discountPercentage;
    }

    // products.entry = <productId,[quantity,price]>
    public abstract double calculateDiscount(Map<Integer, double[]> products);
}
