package ServiceLayer;

import java.time.LocalDate;

import DomainLayer.backend.Market;
import DomainLayer.backend.StorePackage.StoreController;
import DomainLayer.backend.StorePackage.Discount.DiscountPolicyController.LogicalRule;

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

    public String addProduct(int productId, int storeId, double price, int quantity, String username,double weight) throws Exception {
        try {
            String result = market.addProduct(productId, storeId, price, quantity, username,weight);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String initProduct(String username,String productName, int categoryId, String description, String brand,double weight){
        try {
            String result = market.initProduct(username, productName, categoryId, description, brand,weight);
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

    public String addCategoryDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int categoryId,int storeId,String username) throws Exception{
        try {
            String result = market.addCategoryDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, categoryId, storeId, username);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    public String addProductDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int productId,int storeId,String username) throws Exception{
        try {
            String result = market.addProductDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, productId, storeId, username);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addStoreDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int storeId,String username) throws Exception{
        try {
            String result = market.addStoreDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, storeId, username);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addNmericalDiscount(String username,int storeId,Boolean ADD) throws Exception{
        try {
            String result = market.addNmericalDiscount(username, storeId, ADD);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addLogicalDiscount(String username,int storeId,LogicalRule logicalRule) throws Exception{
        try {
            String result = market.addLogicalDiscount(username, storeId, logicalRule);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addCategoryPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int categoryId,String username,int storeId) throws Exception{
        try {
            String result = market.addCategoryPurchasePolicy(quantity, price, date, atLeast, weight, age, categoryId, username, storeId);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    public String addProductPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int productId,String username,int storeId) throws Exception{
        try {
            String result = market.addProductPurchasePolicy(quantity, price, date, atLeast, weight, age, productId, username, storeId);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    
    public String addShoppingCartPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,String username,int storeId) throws Exception{
        try {
            String result = market.addShoppingCartPurchasePolicy(quantity, price, date, atLeast, weight, age, username, storeId);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addUserPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,double userAge,String username,int storeId) throws Exception{
        try {
            String result = market.addUserPurchasePolicy(quantity, price, date, atLeast, weight, age, userAge, username, storeId);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addLogicalPurchase(String username,int storeId,LogicalRule logicalRule) throws Exception{
        try {
            String result = market.addLogicalPurchase(username, storeId, logicalRule);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
