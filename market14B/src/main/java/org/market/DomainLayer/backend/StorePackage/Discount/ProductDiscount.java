package org.market.DomainLayer.backend.StorePackage.Discount;

import java.util.Map;

public class ProductDiscount extends DiscountPolicy {

    private int productId;

    public ProductDiscount(Discount discountType,double discountPercentage,int productId,int id) {
        super(discountType, discountPercentage,id);
        this.productId=productId;
    }

    @Override
    public Map<Integer, double[]> calculateDiscount(Map<Integer, double[]> products) {
        for(Map.Entry<Integer, double[]> entry : products.entrySet()){
            double[] arr=new double[3];
            arr[0]=entry.getValue()[0];
            arr[2]=entry.getValue()[2];
            if(entry.getKey()==productId){
                arr[1]=discountType.calculateDiscount(discountPercentage, entry.getValue()[1],entry.getValue()[0]);
            }
            else{
                arr[1]=entry.getValue()[1];
            }
            products.put(entry.getKey(), arr);
        }
        return products;
    }

}
