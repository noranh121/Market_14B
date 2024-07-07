package org.market.DomainLayer.backend.StorePackage.Discount;

public class HiddenDiscount implements Discount {

    @Override
    public double calculateDiscount(double discountPercentage,double price,double quantity) {
        throw new UnsupportedOperationException("Unimplemented method 'calculateDiscount'");
    }
}
