package DomainLayer.backend.StorePackage;

public class StandardDiscount implements Discount {
    @Override
    public double calculateDiscount(double discountPercentage,double price,double quantity) {
        if(discountPercentage<0 || discountPercentage>1)
            return 0;
        else
            return price*quantity*discountPercentage;
    }
}
