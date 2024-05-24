package DomainLayer.backend;

import DomainLayer.backend.UserPackage.UserController;

public class PurchaseHistory {

    private static PurchaseHistory instance;

    public static synchronized PurchaseHistory getInstance() {
        if(instance == null){
            instance = new PurchaseHistory();
        }
        return instance;
    }
}
