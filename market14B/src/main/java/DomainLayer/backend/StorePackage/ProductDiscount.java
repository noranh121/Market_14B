package DomainLayer.backend.StorePackage;

import java.util.Map;

public class ProductDiscount extends DiscountPolicy {

    private int productId;

    public ProductDiscount(Discount discountType,double discountPercentage,int productId) {
        super(discountType, discountPercentage);
        this.productId=productId;
    }

    @Override
    public double calculateDiscount(Map<Integer, double[]> products) {
        double total=0,quantity,price;
        for(Map.Entry<Integer, double[]> entry : products.entrySet()){
            quantity=entry.getValue()[0];
            price=entry.getValue()[1];
            if(entry.getKey()==productId){
                total=total+discountType.calculateDiscount(discountPercentage, price,quantity);
            }
            else{
                total=total+price*quantity;
            }
        }
        return total;
    }

}
