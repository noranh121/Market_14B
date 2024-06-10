package ServiceLayer;

import java.util.logging.Logger;

import DomainLayer.backend.StorePackage.Store;
import DomainLayer.backend.StorePackage.StoreController;

public class StoresService {
    private static final Logger LOGGER = Logger.getLogger(StoresService.class.getName());

    private StoreController storeController = StoreController.getInstance();



    public String addProduct(int productId, int storeId, double price, int quantity) throws Exception {
        try {
            String result =  storeController.addProduct(productId, storeId, price, quantity);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String removeProduct(int productId, int storeId)  {
        try {
            String result =  storeController.removeProduct(productId, storeId);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    // public String EditProductName(int productId, int storeId, String newName) {
    //     return null;
    // }


    public String EditProducPrice(int productId, int storeId, Double newPrice) throws Exception {
        try {
            String result =  storeController.EditProducPrice(productId, storeId, newPrice);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public String EditProductQuantity(int productId, int storeId, int newQuantity) throws Exception {
        try {
            String result =  storeController.EditProductQuantity(productId, storeId, newQuantity);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    public String closeStore(int storeId) {
        try {
            String result =  storeController.closeStore(storeId);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String openStore(int storeId) {
        try {
            String result =  storeController.openStore(storeId);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getInfo(int storeId){
        try {
            String result =  storeController.getInfo(storeId);
            LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
}
