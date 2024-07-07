package org.market.DomainLayer.backend.StorePackage.Discount;

public class ConditionalDiscount implements Discount{

    private double conditionalPrice;
    private double conditionalQuantity;
    public ConditionalDiscount(double conditionalPrice,double conditionalQuantity){
        this.conditionalPrice=conditionalPrice;
        this.conditionalQuantity=conditionalQuantity;
    }
    
    @Override
    public double calculateDiscount(double discountPercentage,double price,double quantity) {
        if(discountPercentage<0 || discountPercentage>1)
            return 0;
        else{
            if(conditionalPrice<=price && conditionalQuantity<=quantity){
                return price*(1-discountPercentage);
            }
            else{
                return price;
            }
        }
    }

}
