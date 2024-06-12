package ServiceLayer;

import DomainLayer.backend.Market;
import DomainLayer.backend.StorePackage.StoreController;

public class StoresService {

    private Market market = Market.getInstance();

    public Response<String> initStore(String userName, String Description) throws Exception {
        try {
            String result = market.initStore(userName, Description);
            StoreController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String> addProduct(int productId, int storeId, double price, int quantity, String username) throws Exception {
        try {
            String result = market.addProduct(productId, storeId, price, quantity, username);
            StoreController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String> removeProduct(int productId, int storeId, String username) {
        try {
            String result = market.RemoveProduct(productId, storeId, username);
            StoreController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }
    // public String EditProductName(int productId, int storeId, String newName) {
    // return null;
    // }

    public Response<String> EditProducPrice(int productId, int storeId, Double newPrice, String username) throws Exception {
        try {
            String result = market.EditProductPrice(productId, storeId, newPrice, username);
            StoreController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String>  EditProductQuantity(int productId, int storeId, int newQuantity, String username) throws Exception {
        try {
            String result = market.EditProductQuantity(productId, storeId, newQuantity, username);
            StoreController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String>  closeStore(int storeId, String username) {
        try {
            String result = market.CloseStore(storeId, username);
            StoreController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String> openStore(int storeId, String username) {
        try {
            String result = market.OpenStore(storeId, username);
            StoreController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

    public Response<String> getInfo(int storeId, String username) {
        try {
            String result = market.getInfo(storeId, username);
            StoreController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
        }
    }

}
