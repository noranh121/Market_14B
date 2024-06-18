package DomainLayer.backend.StorePackage.Purchase;

import java.util.Map;

import DomainLayer.backend.ProductPackage.ProductController;

public class CategoryPurchase extends PurchasePolicy {

    private int categoryId;

    public CategoryPurchase(PurchaseMethod purchaseMethod, int categoryId,int id) {
        super(purchaseMethod,id);
        this.categoryId = categoryId;
    }

    @Override
    public Boolean purchase(Map<Integer, double[]> products, double age) {
        Boolean output = true;
        for (Map.Entry<Integer, double[]> entry : products.entrySet()) {
            int productId = (int) entry.getKey();
            if (ProductController.getInstance().getProductCategory(productId) == categoryId) {
                output &= purchaseMethod.purchase(productId, (int) products.get(productId)[0], products.get(productId)[1], products.get(productId)[2], age);
            }
        }
        return output;
    }

}
