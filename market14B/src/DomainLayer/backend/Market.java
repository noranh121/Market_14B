package DomainLayer.backend;

import DomainLayer.backend.ProductPackage.Product;
import DomainLayer.backend.UserPackage.UserController;

public class Market {

    private UserController userController = UserController.getInstance();
    private static Market instance;
    public static Market getInstance() {
        return instance;
    }

    public String EnterAsGuest() throws Exception {
        return userController.EnterAsGuest();
    }

    public String GuestExit() throws Exception {
        return userController.GuestExit();
    }

    public String Login(String username, String password) throws Exception {
        return userController.Login(username, password);
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

}
