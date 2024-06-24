package DomainLayer.backend.StorePackage.Purchase;

public interface PurchaseMethod {
    Boolean purchase(int productId, int quantity, double price, double weight, double age) throws Exception;
}
