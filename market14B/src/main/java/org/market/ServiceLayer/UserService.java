package org.market.ServiceLayer;

import java.util.ArrayList;
import java.util.List;

import org.market.DomainLayer.backend.Market;
import org.market.DomainLayer.backend.StorePackage.Store;
import org.market.DomainLayer.backend.UserPackage.UserController;
import org.market.Web.DTOS.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserService {


    @Autowired
    private Market market;// = Market.getInstance();

    public String EnterAsGuest(double age) {
        try {
            String result = market.EnterAsGuest(age);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String GuestExit(String username) {
        try {
            String result = market.GuestExit(username);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String Login(String guest, String username, String password) {
        try {
            String result = market.Login(guest, username, password);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String Logout(String username) {
        try {
            String result = market.Logout(username);
            UserController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Response<String> Register(String username, String password,double age) {
        try {
            String result = market.Register(username, password,age);
            UserController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String> Buy(String username,String currency,String card_number,int month,int year,String ccv,
    String address, String city, String country, int zip) {
        try {
            double sum = market.Buy(username, currency, card_number, month, year, ccv, address, city, country, zip);
            // result = payment method TODO
            // LOGGER.info(result);
            // return result;
            return Response.successRes("Buying process completed successfully!, " + "sum: " + sum);
        } catch (Exception e) {
            return  Response.failRes(e.getMessage());
        }
    }

    public Response<String> addToCart(String username, Integer product, int storeId, int quantity) {
        try {
            String result = market.addToCart(username, product, storeId, quantity);
            UserController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String> inspectCart(String username) {
        try {
            String result = market.inspectCart(username);
            UserController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String> removeCartItem(String username, int storeId, int product) {
        try {
            String result = market.removeCartItem(username, storeId, product);
            UserController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String> EditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner,
            Boolean storeManager, Boolean[] pType) throws Exception {
        try {
            String result = market.EditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
            UserController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String> AssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
        try {
            String result = market.AssignStoreManager(storeId, ownerUserName, username, pType);
            UserController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String> AssignStoreOnwer(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
        try {
            String result = market.AssignStoreOwner(storeId, ownerUserName, username, pType);
            UserController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String> unassignUser(int storeID, String ownerUserName, String userName) throws Exception {
        try {
            String result = market.unassignUser(storeID, ownerUserName, userName);
            UserController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String> resign(int storeID, String username) throws Exception {
        try {
            String result = market.resign(storeID, username);
            UserController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
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
}
