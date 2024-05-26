package DomainLayer.backend;

import DomainLayer.backend.ProductPackage.Product;
import DomainLayer.backend.UserPackage.UserController;

public class Market {

    private UserController userController = UserController.getInstance();
    private static Market instance;
    public static Market getInstance() {
        if (instance == null)
            instance = new Market();
        return instance;
    }

    public String EnterAsGuest() throws Exception {
        return userController.EnterAsGuest();
    }

    public String GuestExit(String username) throws Exception {
        return userController.GuestExit(username);
    }

    public String Login(String guest, String username, String password) throws Exception {
        return userController.Login(guest, username, password);
    }

    public String Logout(String username) throws Exception {
        return userController.Logout(username);
    }

    public String Register(String username, String password) throws Exception {
        return userController.Register(username, password);
    }
    public double Buy(String username) throws Exception {
        return userController.Buy(username);
    }

    public String addToCart(String username, Product product, int storeId, int quantity) throws Exception {
        return userController.addToCart(username,product, storeId, quantity);
    }

    public String inspectCart(String username) throws Exception {
        return userController.inspectCart(username);
    }

    public String removeCartItem(String username, int storeId, Product product) throws Exception {
        return userController.removeCartItem(username,storeId,product);
    }

    public String EditPermissions(int storeID,String ownerUserName, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception {
        return userController.EditPermissions(storeID,ownerUserName,userName,storeOwner,storeManager,pType);
    }

    public String AssignStoreManager(int storeId,String ownerUserName,String username,Boolean[] pType) throws Exception {
        return userController.AssignStoreManager(storeId,ownerUserName,username,pType);
    }

    public String AssignStoreOwner(int storeId,String ownerUserName,String username,Boolean[] pType) throws Exception {
        return userController.AssignStoreOwner(storeId,ownerUserName,username,pType);
    }
}
