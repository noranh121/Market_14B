package org.market.DomainLayer.backend.StorePackage.Discount;

public abstract class DiscountPolicy extends CompositeDiscountPolicy {
    protected Discount discountType;
    protected double discountPercentage;

    public DiscountPolicy(Discount discountType,double discountPercentage,int id){
        super(id);
        this.discountType=discountType;
        this.discountPercentage=discountPercentage;
    }
}
