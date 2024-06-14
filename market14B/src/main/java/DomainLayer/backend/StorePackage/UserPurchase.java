package DomainLayer.backend.StorePackage;

import java.util.Map;

public class UserPurchase extends PurchasePolicy {

    public UserPurchase(PurchaseMethod purchaseMethod, double age,int id) {
        super(purchaseMethod,id);
    }

    @Override
    public Boolean purchase(Map<Integer, double[]> products, double age) {
        Boolean output = true;
        for (Map.Entry<Integer, double[]> entry : products.entrySet()) {
            int productId = (int) entry.getKey();
            output &= purchaseMethod.purchase(productId, (int) products.get(productId)[0], products.get(productId)[1], products.get(productId)[2], age);
        }
        return true;
    }

}
