package DomainLayer.backend.StorePackage;

public class HiddenDiscount implements Discount {

    @Override
    public double calculateDiscount(double discountPercentage,double price) {
        throw new UnsupportedOperationException("Unimplemented method 'calculateDiscount'");
    }
}
