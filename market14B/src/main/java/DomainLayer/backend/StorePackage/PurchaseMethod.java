package DomainLayer.backend.StorePackage;

public interface PurchaseMethod {
    Boolean purchase(int productId, int quantity, double price, double weight, double age);
}
