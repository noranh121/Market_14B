package org.market.DomainLayer.backend.StorePackage.Purchase;

import java.util.Map;

public class UserPurchase extends PurchasePolicy {

    public UserPurchase(PurchaseMethod purchaseMethod, double age,int id) {
        super(purchaseMethod,id);
    }

    @Override
    public Boolean purchase(Map<Integer, double[]> products, double age) throws Exception {
        Boolean output = true;
        for (Map.Entry<Integer, double[]> entry : products.entrySet()) {
            int productId = (int) entry.getKey();
            output &= purchaseMethod.purchase(productId, (int) products.get(productId)[0], products.get(productId)[1], products.get(productId)[2], age);
        }
        return true;
    }

    @Override
    public void addComposite(CompositePurchasePolicy compositePurchasePolicy, int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("not purchase controller");
    }

    @Override
    public void removeComposite(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("not purchase controller");
    }

}
