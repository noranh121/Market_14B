package DomainLayer.backend.StorePackage.Purchase;

public abstract class PurchasePolicy extends CompositePurchasePolicy{
    public enum PurchasePolicyType{
        PRODUCT,
        CATEGORY,
        USER,
        SHOPPINGCART
    }
    protected PurchaseMethod purchaseMethod;

    public PurchasePolicy(PurchaseMethod purchaseMethod,int id) {
        super(id);
        this.purchaseMethod = purchaseMethod;
    }
}