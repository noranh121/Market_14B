package org.market.ServiceLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.market.DomainLayer.backend.Market;
import org.market.DomainLayer.backend.Permission;
import org.market.DomainLayer.backend.ProductPackage.Product;
import org.market.DomainLayer.backend.StorePackage.Store;
import org.market.DomainLayer.backend.StorePackage.StoreController;
import org.market.DomainLayer.backend.StorePackage.Discount.DiscountPolicyController.LogicalRule;
import org.market.Web.DTOS.PermissionDTO;
import org.market.Web.DTOS.ProductDTO;
import org.market.Web.DTOS.StoreDTO;
import org.springframework.security.access.method.P;

public class StoresService {

    private Market market = Market.getInstance();

    public Response<String> initStore(String userName, String Description) {
        try {
            String result = market.initStore(userName, Description);
            StoreController.LOGGER.info(result);
            return Response.successRes(result);
        } catch (Exception e) {
            return Response.failRes(e.getMessage());
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
    }//@

    public Response<String> initProduct(String username,String productName, int categoryId, String description, String brand,double weight){
        try {
            String result = market.initProduct(username, productName, categoryId, description, brand,weight);
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

    public String addCategoryDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int categoryId,int storeId,String username,int id) throws Exception{
        try {
            String result = market.addCategoryDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, categoryId, storeId, username,id);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    public String addProductDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int productId,int storeId,String username,int id) throws Exception{
        try {
            String result = market.addProductDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, productId, storeId, username,id);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addStoreDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int storeId,String username,int id) throws Exception{
        try {
            String result = market.addStoreDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, storeId, username,id);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addNmericalDiscount(String username,int storeId,Boolean ADD,int id) throws Exception{
        try {
            String result = market.addNmericalDiscount(username, storeId, ADD,id);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addLogicalDiscount(String username,int storeId,LogicalRule logicalRule,int id) throws Exception{
        try {
            String result = market.addLogicalDiscount(username, storeId, logicalRule,id);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addCategoryPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int categoryId,String username,int storeId,Boolean immediate,int id) throws Exception{
        try {
            String result = market.addCategoryPurchasePolicy(quantity, price, date, atLeast, weight, age, categoryId, username, storeId,immediate,id);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    public String addProductPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int productId,String username,int storeId,Boolean immediate,int id) throws Exception{
        try {
            String result = market.addProductPurchasePolicy(quantity, price, date, atLeast, weight, age, productId, username, storeId,immediate,id);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    
    public String addShoppingCartPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,String username,int storeId,Boolean immediate,int id) throws Exception{
        try {
            String result = market.addShoppingCartPurchasePolicy(quantity, price, date, atLeast, weight, age, username, storeId,immediate,id);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addUserPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,double userAge,String username,int storeId,Boolean immediate,int id) throws Exception{
        try {
            String result = market.addUserPurchasePolicy(quantity, price, date, atLeast, weight, age, userAge, username, storeId,immediate,id);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addLogicalPurchase(String username,int storeId,LogicalRule logicalRule,int id) throws Exception{
        try {
            String result = market.addLogicalPurchase(username, storeId, logicalRule,id);
            StoreController.LOGGER.info(result);
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public ArrayList<StoreDTO> getAllStores() {
        ArrayList<StoreDTO> dtoStrs = new ArrayList<>();
        List<Store> strs = market.getAllStores();
        for(Store s : strs){
            StoreDTO sdto = new StoreDTO(s);
            dtoStrs.add(sdto);
        }
        return dtoStrs;
    }

    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> psdto = new ArrayList<>();
        List<Product> prods = market.getAllProducts();
        //get price for product;
        for(Product p : prods){
            double [] price_store = market.findProdInfo(p);
            if(price_store[0] != -1){
                ProductDTO pdto = new ProductDTO(p, price_store[0], (int) price_store[1]);
                psdto.add(pdto);
            }
        }
        return psdto;
    }

    public List<ProductDTO> getStoreProducts(int store_id) {
        List<ProductDTO> prodsdto = market.getStoreProducts(store_id);
        return prodsdto;
    }

    public StoreDTO getStore(int store_id) {
        Store s = market.getStore(store_id);
        StoreDTO sdto = new StoreDTO(s);
        return sdto;
    }

    public ProductDTO getProductInfo(int product_id) throws Exception {
        Product p = market.getProduct(product_id);
        double [] price_store = market.findProdInfo(p);
        if(price_store[0] == -1){
            throw new Exception("Price is Out Of Stock");
        }
        ProductDTO pdto = new ProductDTO(p, price_store[0], (int) price_store[1]);
        return pdto;
    }

    public List<PermissionDTO> getPermissions(String username) throws Exception {
        return market.getPermissions(username);
    }
}
