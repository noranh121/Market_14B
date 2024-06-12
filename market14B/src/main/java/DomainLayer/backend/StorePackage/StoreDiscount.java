package DomainLayer.backend.StorePackage;

import java.util.Map;

public class StoreDiscount extends DiscountPolicy{

    double storeId;

    public StoreDiscount(Discount discountType, double discountPercentage,int storeId) {
        super(discountType, discountPercentage);
        this.storeId=storeId;
    }

    @Override
    public double calculateDiscount(Map<Integer, double[]> products) {
        double total=0,quantity,price,id;
        for(Map.Entry<Integer, double[]> entry : products.entrySet()){
            quantity=entry.getValue()[0];
            price=entry.getValue()[1];
            id=entry.getValue()[2];
            if(id==storeId){
                total=total+discountType.calculateDiscount(discountPercentage, price*quantity);
            }
            else{
                total=total+price*quantity;
            }
        }
        return total;
    }

}
