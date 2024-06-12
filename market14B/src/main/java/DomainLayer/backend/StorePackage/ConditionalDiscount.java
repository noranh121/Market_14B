package DomainLayer.backend.StorePackage;

public class ConditionalDiscount implements Discount{

    private double conditionalPrice;
    public ConditionalDiscount(double d, double conditionalPrice){
        this.conditionalPrice=conditionalPrice;
    }
    @Override
    public double calculateDiscount(double discountPercentage,double price) {
        if(discountPercentage<0 || discountPercentage>1)
            return 0;
        else{
            if(conditionalPrice<=price){
                return price*discountPercentage;
            }
            else{
                return price;
            }
        }
    }

}
