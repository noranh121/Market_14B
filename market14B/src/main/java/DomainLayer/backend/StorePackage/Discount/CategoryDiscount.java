package DomainLayer.backend.StorePackage.Discount;

import java.util.Map;

import DomainLayer.backend.ProductPackage.ProductController;

public class CategoryDiscount extends DiscountPolicy{

    private int categoryId;

    public CategoryDiscount(Discount discountType, double discountPercentage,int categoryId,int id) {
        super(discountType, discountPercentage,id);
        this.categoryId=categoryId;
    }

    @Override
    public Map<Integer, double[]> calculateDiscount(Map<Integer, double[]> products) {
        double productId;
        for(Map.Entry<Integer, double[]> entry : products.entrySet()){
            double [] arr=new double[3];
            productId=entry.getKey();
            arr[0]=entry.getValue()[0];
            arr[2]=entry.getValue()[2];
            if(ProductController.getInstance().getProductCategory((int)productId)==categoryId){
                arr[1]=discountType.calculateDiscount(discountPercentage,entry.getValue()[1],entry.getValue()[0]);
            }
            else{
                arr[1]=entry.getValue()[1];
            }
            products.put(entry.getKey(),arr);
        }
        return products;
    }

}
