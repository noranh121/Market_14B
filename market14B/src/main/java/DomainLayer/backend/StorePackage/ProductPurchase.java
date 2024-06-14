package DomainLayer.backend.StorePackage;

import java.util.Map;

public class ProductPurchase extends PurchasePolicy {

    private int productId;

    public ProductPurchase(PurchaseMethod purchaseMethod, int productId,int id) {
        super(purchaseMethod,id);
        this.productId = productId;
    }

    @Override
    public Boolean purchase(Map<Integer, double[]> products, double age) {
        if (products.containsKey(productId))
            return purchaseMethod.purchase(productId, (int) products.get(productId)[0], products.get(productId)[1],
                    products.get(productId)[2], age);
        else
            return true;
    }

}
