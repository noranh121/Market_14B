package ServiceLayer;

import DomainLayer.backend.Market;
import DomainLayer.backend.ProductPackage.Product;
import DomainLayer.backend.UserPackage.User;
import DomainLayer.backend.UserPackage.UserController;

import java.util.logging.Logger;

public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    private Market market = Market.getInstance();

    public String EnterAsGuest() {
        try {
            String result =  market.EnterAsGuest();
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String GuestExit() {
        try {
            String result =  market.EnterAsGuest();
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String Login(String username, String password) {
        try {
            String result =  market.Login(username,password);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String Logout(String username) {
        try {
            String result =  market.Logout(username);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String Register(String username, String password) {
        try {
            String result =  market.Register(username, password);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String Buy(String username) {
        try {
            int sum =  market.Buy(username);
            //result = payment method TODO
            //LOGGER.info(result);
            //return result;
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addToCart(String username, Product product, int storeId, int quantity) {
        try {
            String result = market.addToCart(username,product,storeId, quantity);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String inspectCart(String username) {
        try {
            String result = market.inspectCart(username);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String removeCartItem(String username, int storeId, Product product) {
        try {
            String result = market.removeCartItem(username,storeId, product);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
