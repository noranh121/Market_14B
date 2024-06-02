package ServiceLayer;

import DomainLayer.backend.Market;
import DomainLayer.backend.ProductPackage.Product;
import DomainLayer.backend.UserPackage.User;
import DomainLayer.backend.UserPackage.UserController;

import java.util.logging.Logger;

public class UserService {
    private Market market = Market.getInstance();

    public String EnterAsGuest() {
        try {
            String result =  market.EnterAsGuest();
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String GuestExit() {
        try {
            String result =  market.EnterAsGuest();
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String Login(String guest,String username, String password) {
        try {
            String result =  market.Login(guest,username,password);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String Logout(String username) {
        try {
            String result =  market.Logout(username);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String Register(String username, String password) {
        try {
            String result =  market.Register(username, password);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String Buy(String username) {
        try {
            double sum =  market.Buy(username);
            //result = payment method TODO
            //LOGGER.info(result);
            //return result;
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addToCart(String username, Integer product, int storeId, int quantity) {
        try {
            String result = market.addToCart(username,product,storeId, quantity);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String inspectCart(String username) {
        try {
            String result = market.inspectCart(username);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String removeCartItem(String username, int storeId, int product) {
        try {
            String result = market.removeCartItem(username,storeId, product);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String EditPermissions(int storeID,String ownerUserName, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception {
        try {
            String result = market.EditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String AssignStoreManager(int storeId,String ownerUserName,String username,Boolean[] pType) throws Exception {
        try {
            String result = market.AssignStoreManager(storeId, ownerUserName, username, pType);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String AssignStoreOnwer(int storeId,String ownerUserName,String username,Boolean[] pType) throws Exception {
        try {
            String result = market.AssignStoreOwner(storeId, ownerUserName, username, pType);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public  String unassignUser(int storeID, String ownerUserName,String userName) throws Exception {
        try {
            String result = market.unassignUser(storeID, ownerUserName, userName);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
