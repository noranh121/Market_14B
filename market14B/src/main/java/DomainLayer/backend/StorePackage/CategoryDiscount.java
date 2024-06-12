package DomainLayer.backend.StorePackage;

import java.util.Map;

import DomainLayer.backend.ProductPackage.ProductController;

public class CategoryDiscount extends DiscountPolicy{

    private int categoryId;

    public CategoryDiscount(Discount discountType, double discountPercentage,int categoryId) {
        super(discountType, discountPercentage);
        this.categoryId=categoryId;
    }

    @Override
    public double calculateDiscount(Map<Integer, double[]> products) {
        double total=0,quantity,price,productId;
        for(Map.Entry<Integer, double[]> entry : products.entrySet()){
            productId=entry.getKey();
            quantity=entry.getValue()[0];
            price=entry.getValue()[1];
            if(ProductController.getInstance().getProductCategory((int)productId)==categoryId){
                total=total+discountType.calculateDiscount(discountPercentage, quantity*price);
            }
            else{
                total=total+price*quantity;
            }
        }
        return total;
    }

}
