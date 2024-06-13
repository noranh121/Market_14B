package DomainLayer.backend.StorePackage;

import java.util.Map;

public abstract class PurchasePolicy {
    protected PurchaseMethod purchaseMethod;

    public PurchasePolicy(PurchaseMethod purchaseMethod) {
        this.purchaseMethod = purchaseMethod;
    }

    // products.entry = <productId,[quantity,price,weight]>
    public abstract Boolean purchase(Map<Integer, double[]> products, double age);
}
