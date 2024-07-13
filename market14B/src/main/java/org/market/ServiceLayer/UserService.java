package org.market.ServiceLayer;

import org.market.DomainLayer.backend.Market;
import org.market.DomainLayer.backend.StorePackage.Store;
import org.market.DomainLayer.backend.UserPackage.UserController;
import org.market.Web.DTOS.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class UserService {


    @Autowired
    private Market market;// = Market.getInstance();

    public String EnterAsGuest(double age) throws Exception {
            String result = market.EnterAsGuest(age);
            UserController.LOGGER.info(result);
            return result;
    }

    public String GuestExit(String username) throws Exception {
            String result = market.GuestExit(username);
            UserController.LOGGER.info(result);
            return result;
    }

    public String Login(String guest, String username, String password) throws Exception {
            String result = market.Login(guest, username, password);
            UserController.LOGGER.info(result);
            return result;
    }

    public String Logout(String username) throws Exception {
            String result = market.Logout(username);
            UserController.LOGGER.info(result);
            return result;
    }

    public String Register(String username, String password,double age) throws Exception {
            String result = market.Register(username, password,age);
            UserController.LOGGER.info(result);
            return result;
    }

    public String Buy(String username,String currency,String card_number,int month,int year,String ccv,
    String address, String city, String country, int zip) throws Exception{
            double sum = market.Buy(username, currency, card_number, month, year, ccv, address, city, country, zip);
            // result = payment method TODO
            // LOGGER.info(result);
            // return result;
        return "Buying process completed successfully!, " + "sum: " + sum;
    }

    public String addToCart(String username, Integer product, int storeId, int quantity) throws Exception {
            String result = market.addToCart(username, product, storeId, quantity);
            UserController.LOGGER.info(result);
            return result;
    }

    public String inspectCart(String username)throws Exception {
            String result = market.inspectCart(username);
            UserController.LOGGER.info(result);
            return result;
    }

    public String removeCartItem(String username, int storeId, int product)throws Exception {
            String result = market.removeCartItem(username, storeId, product);
            UserController.LOGGER.info(result);
            return result;
    }

    public String EditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner,
            Boolean storeManager, Boolean[] pType) throws Exception {
            String result = market.EditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
            UserController.LOGGER.info(result);
            return result;
    }

    public String AssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
            String result = market.AssignStoreManager(storeId, ownerUserName, username, pType);
            UserController.LOGGER.info(result);
            return result;
    }

    public String AssignStoreOnwer(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
            String result = market.AssignStoreOwner(storeId, ownerUserName, username, pType);
            UserController.LOGGER.info(result);
            return result;
    }

    public String unassignUser(int storeID, String ownerUserName, String userName) throws Exception {
            String result = market.unassignUser(storeID, ownerUserName, userName);
            UserController.LOGGER.info(result);
            return result;
    }

    public String resign(int storeID, String username) throws Exception {
            String result = market.resign(storeID, username);
            UserController.LOGGER.info(result);
            return result;
    }

    public List<StoreDTO> user_stores(String username) throws Exception {
        List<StoreDTO> sdtos = new ArrayList<>();
        List<Store> stores = market.getUserStores(username);
        for(Store s: stores){
            StoreDTO sdto = new StoreDTO(s);
            sdtos.add(sdto);
        }
        return sdtos;
    }

    public List<String> getPurchaseHistory(String username) throws Exception {
        return market.getPurchaseHistoryUser(username);
    }
}
