package DomainLayer.backend.StorePackage;

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
