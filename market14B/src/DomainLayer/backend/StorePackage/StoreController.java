package DomainLayer.backend.StorePackage;

public class StoreController {
    private static StoreController instance;

    public static synchronized StoreController getInstance() {
        if(instance == null){
            instance = new StoreController();
        }
        return instance;
    }

}
