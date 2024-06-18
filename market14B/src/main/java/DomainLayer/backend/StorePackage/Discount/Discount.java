package DomainLayer.backend.StorePackage.Discount;

public interface Discount {
    double calculateDiscount(double discountPercentage,double price,double quantity);
}
