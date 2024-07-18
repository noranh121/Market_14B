package org.market.DomainLayer.backend.StorePackage.Discount;

import java.util.Map;

public class StoreDiscount extends DiscountPolicy{

    public StoreDiscount(Discount discountType, double discountPercentage,int id) {
        super(discountType, discountPercentage,id);
    }

    @Override
    public Map<Integer, double[]> calculateDiscount(Map<Integer, double[]> products) {
        for(Map.Entry<Integer, double[]> entry : products.entrySet()){
            double[] arr=new double[3];
            arr[0]=entry.getValue()[0];
            arr[1]=discountType.calculateDiscount(discountPercentage, entry.getValue()[1],entry.getValue()[0]);
            arr[2]=entry.getValue()[2];
            products.put(entry.getKey(),arr);
        }
        return products;
    }

    @Override
    public void addComposite(CompositeDiscountPolicy composite, int id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("not discount policy controller");
    }

    @Override
    public void removeComposite(int id) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("not discount policy controller");
    }

}
