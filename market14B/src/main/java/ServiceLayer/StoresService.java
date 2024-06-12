package ServiceLayer;

import DomainLayer.backend.Market;
import DomainLayer.backend.StorePackage.StoreController;

public class StoresService {

    private Market market = Market.getInstance();

    public String initStore(String userName, String Description) throws Exception {
        try {
            String result = market.initStore(userName, Description);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addProduct(int productId, int storeId, double price, int quantity, String username) throws Exception {
        try {
            String result = market.addProduct(productId, storeId, price, quantity, username);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String initProduct(String username,String productName, int categoryId, String description, String brand){
        try {
            String result = market.initProduct(username, productName, categoryId, description, brand);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String removeProduct(int productId, int storeId, String username) {
        try {
            String result = market.RemoveProduct(productId, storeId, username);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    // public String EditProductName(int productId, int storeId, String newName) {
    // return null;
    // }

    public String EditProducPrice(int productId, int storeId, Double newPrice, String username) throws Exception {
        try {
            String result = market.EditProductPrice(productId, storeId, newPrice, username);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String EditProductQuantity(int productId, int storeId, int newQuantity, String username) throws Exception {
        try {
            String result = market.EditProductQuantity(productId, storeId, newQuantity, username);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String closeStore(int storeId, String username) {
        try {
            String result = market.CloseStore(storeId, username);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String openStore(int storeId, String username) {
        try {
            String result = market.OpenStore(storeId, username);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getInfo(int storeId, String username) {
        try {
            String result = market.getInfo(storeId, username);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Discount policy
    public String setProductDiscountPolicy(int storeId ,String username,Boolean discountType,double conditionalprice,double conditionalQuantity, double discountPercentage,int productId) throws Exception{
        try {
            String result = market.setProductDiscountPolicy(storeId, username, discountType, conditionalprice,conditionalQuantity, discountPercentage, productId);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String setCategoryDiscountPolicy(int storeId ,String username,Boolean discountType,double conditionalprice,double conditionalQuantity, double discountPercentage,int categoryId) throws Exception{
        try {
            String result = market.setCategoryDiscountPolicy(storeId, username, discountType, conditionalprice,conditionalQuantity, discountPercentage, categoryId);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String setStoreDiscountPolicy(int storeId ,String username,Boolean discountType,double conditionalprice,double conditionalQuantity, double discountPercentage) throws Exception{
        try {
            String result = market.setStoreDiscountPolicy(storeId, username, discountType, conditionalprice,conditionalQuantity, discountPercentage);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
