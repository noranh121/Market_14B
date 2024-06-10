package DomainLayer.backend.API;

import DomainLayer.backend.UserPackage.ShoppingCart;

public interface SupplyAPI {
    String processOrder(String buyerAddress, ShoppingCart cart);
}
