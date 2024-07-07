package org.market.DomainLayer.backend.StorePackage.Discount;

import java.util.Map;

public abstract class CompositeDiscountPolicy {
    private int id;

    public CompositeDiscountPolicy(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public double calculateTotal(Map<Integer, double[]> products){
        double total=0;
        for(Map.Entry<Integer, double[]> entry : products.entrySet()){
            total+=entry.getValue()[1]*entry.getValue()[0];
        }
        return total;
    }

    // products.entry = <productId,[quantity,price,weight]>
    public abstract Map<Integer, double[]> calculateDiscount(Map<Integer, double[]> products);
}
