package org.market.DomainLayer.backend.StorePackage.Purchase;

import java.util.Map;

public class ProductPurchase extends PurchasePolicy {

    private int productId;

    public ProductPurchase(PurchaseMethod purchaseMethod, int productId,int id) {
        super(purchaseMethod,id);
        this.productId = productId;
    }

    @Override
    public Boolean purchase(Map<Integer, double[]> products, double age) throws Exception {
        if (products.containsKey(productId))
            return purchaseMethod.purchase(productId, (int) products.get(productId)[0], products.get(productId)[1],
                    products.get(productId)[2], age);
        else
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
