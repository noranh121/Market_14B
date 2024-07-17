package org.market.ServiceLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.market.DomainLayer.backend.StorePackage.Discount.DiscountPolicyController;
import org.market.DomainLayer.backend.StorePackage.Purchase.PurchaseMethod;
import org.market.DomainLayer.backend.StorePackage.Purchase.PurchasePolicyController;
import org.market.Web.DTOS.PermissionDTO;
import org.market.Web.DTOS.ProductDTO;
import org.market.Web.DTOS.StoreDTO;
import org.market.DomainLayer.backend.Market;
import org.market.DomainLayer.backend.ProductPackage.Product;
import org.market.DomainLayer.backend.StorePackage.Discount.DiscountPolicyController;
import org.market.DomainLayer.backend.StorePackage.Purchase.PurchasePolicyController;
import org.market.Web.DTOS.*;
import org.market.Web.Requests.SearchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceFactory {
    // private MarketService marketService = new MarketService();
    // private UserService userService = new UserService();
    // private StoresService storesService = new StoresService();
    private MarketService marketService;
    private UserService userService;
    private StoresService storesService;


    @Autowired
    public ServiceFactory(MarketService marketService, UserService userService,StoresService storesService){
        this.marketService = marketService;
        this.userService= userService;
        this.storesService = storesService;
        // try {
        //     Register("admin", "password", 24);
        // } catch (Exception e) {
        //     throw new RuntimeException(e);
        // }
        // addToSystemManagers("admin");
    }

    // MarketService
    public String setMarketOnline(String username) throws Exception {
        return marketService.setMarketOnline(username);
    }

    public String setMarketOFFLINE(String username) throws Exception {
        return marketService.setMarketOFFLINE(username);
    }

    public List<String> getSystemManagers(){
        return marketService.getSystemManagers();
    }

    public String addCatagory(int storeId, String catagory, String username) throws Exception {
        return marketService.addCatagory(storeId, catagory, username);
    }

    public String suspendUserIndefinitely(String systemManager, String username) throws Exception {
        return marketService.suspendUserIndefinitely(systemManager,username);
    }

    public String suspendUserTemporarily(String systemManager, String username, int durationInSeconds) throws Exception{
        return marketService.suspendUserTemporarily(systemManager,username,durationInSeconds);
    }

    public String resumeUserIndefinitely(String systemManager, String username) throws Exception {
        return marketService.resumeUserIndefinitely(systemManager,username);
    }

    public String resumeUser(String systemManager, String username, int duration) throws Exception {
        return marketService.resumeUserTemporarily(systemManager,username,duration);
    }

    public String viewSuspended(String systemManager) throws Exception {
        return marketService.viewSuspended(systemManager);
    }

    public void addToSystemManagers(String admin) {
        marketService.addToSystemManagers(admin);
    }

    public boolean isSystemManager(String username) throws Exception {
        return marketService.isSystemManager(username);
    }
    
    public String viewSystemPurchaseHistory(String username){
        return marketService.viewSystemPurchaseHistory(username);
    }

    // UserService
    public String EnterAsGuest(double age) throws Exception {
        return userService.EnterAsGuest(age);
    }

    public String GuestExit(String username) throws Exception {
        return userService.GuestExit(username);
    }

    public String Login(String guest, String username, String password) throws Exception {
        return userService.Login(guest, username, password);
    }

    public String Logout(String username) throws Exception {
        return userService.Logout(username);
    }

    public String Register(String username, String password,double age) throws Exception {
        return userService.Register(username, password,age);
    }

    public String Buy(String username,String currency,String card_number,int month,int year,String ccv,
                                String address, String city, String country, int zip) throws Exception {
        return userService.Buy(username,currency,card_number,month,year,ccv,
                address, city, country, zip,"20444444");
    }

    public String addToCart(String username, Integer product, int storeId, int quantity) throws Exception {
        return userService.addToCart(username, product, storeId, quantity);
    }

    public String inspectCart(String username) throws Exception {
        return userService.inspectCart(username);
    }

    public String removeCartItem(String username, int storeId, int product) throws Exception{
        return userService.removeCartItem(username, storeId, product);
    }

    public String EditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner,
            Boolean storeManager, Boolean[] pType) throws Exception {
        return userService.EditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
    }

    public String AssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
        return userService.AssignStoreManager(storeId, ownerUserName, username, pType);
    }

    public String AssignStoreOnwer(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
        return userService.AssignStoreOnwer(storeId, ownerUserName, username, pType);
    }

    public String unassignUser(int storeID, String ownerUserName, String userName) throws Exception {
        return userService.unassignUser(storeID, ownerUserName, userName);
    }

    public String resign(int storeID, String username) throws Exception {
        return userService.resign(storeID, username);
    }

    // StoreService
    public String initStore(String userName, String name, String Description) throws Exception {
        return storesService.initStore(userName, name, Description);
    }

    public String addProduct(int productId, int storeId, double price, int quantity, String username,double weight) throws Exception {
        return storesService.addProduct(productId, storeId, price, quantity, username,weight);
    }//@

    public String removeProduct(int productId, int storeId, String username) throws Exception {
        return storesService.removeProduct(productId, storeId, username);
    }

    public String EditProducPrice(int productId, int storeId, Double newPrice, String username) throws Exception {
        return storesService.EditProducPrice(productId, storeId, newPrice, username);
    }

    public String  EditProductQuantity(int productId, int storeId, int newQuantity, String username) throws Exception {
        return storesService.EditProductQuantity(productId, storeId, newQuantity, username);
    }

    public String closeStore(int storeId, String username) throws Exception{
        return storesService.closeStore(storeId, username);
    }

    public String openStore(int storeId, String username) throws Exception{
        return storesService.openStore(storeId, username);
    }

    public String getInfo(int storeId, String username) throws Exception {
        return storesService.getInfo(storeId, username);
    }

    public Integer initProduct(String username,String productName, int categoryId, String description, String brand,double weight) throws Exception {
        return storesService.initProduct(username, productName, categoryId, description, brand,weight);
    }

    public ArrayList<StoreDTO> getAllStores() {
        return storesService.getAllStores();
    }

    public List<ProductDTO> getAllProducts() {
        return storesService.getAllProducts();
    }

    public List<StoreDTO> user_stores(String username) throws Exception {
        return userService.user_stores(username);
    }

    public List<ProductDTO> getStoreProducts(int store_id) {
        return storesService.getStoreProducts(store_id);
    }

    public StoreDTO getStore(int store_id) {
        return storesService.getStore(store_id);
    }

    public ProductDTO getProductInfo(int product_id) throws Exception {
        return storesService.getProductInfo(product_id);
    }

    public List<PermissionDTO> getPermissions(String username) throws Exception {
        return storesService.getPermissions(username);
    }

    public List<String> getPurchaseHistory(String username) throws Exception {
        return userService.getPurchaseHistory(username);
    }

//     public List<ProductDTO> search(SearchEntity entity){
//         return marketService.search(entity);
// }
    // public String addCategoryDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int categoryId,int storeId,String username,int id) throws Exception{
    //     return storesService.addCategoryDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, categoryId, storeId, username, id);
    // }

    // public String addProductDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int productId,int storeId,String username,int id) throws Exception{
    //     return storesService.addProductDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, productId, storeId, username, id);
    // }

    // public String addStoreDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int storeId,String username,int id) throws Exception{
    //     return storesService.addStoreDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, storeId, username, id);
    // }    

    public String addNmericalDiscount(String username,int storeId,Boolean ADD,int id) throws Exception{
        return storesService.addNmericalDiscount(username, storeId, ADD, id);
    }

    public String addLogicalDiscount(String username,int storeId,String logicalRule,int id){
        return storesService.addLogicalDiscount(username, storeId, logicalRule, id);
    }

    // public String addCategoryPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int categoryId,String username,int storeId,Boolean immediate,int id){
    //     return storesService.addCategoryPurchasePolicy(quantity, price, date, atLeast, weight, age, categoryId, username, storeId, immediate, id);
    // }

    // public String addProductPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int productId,String username,int storeId,Boolean immediate,int id){
    //     return storesService.addProductPurchasePolicy(quantity, price, date, atLeast, weight, age, productId, username, storeId, immediate, id);
    // }

    // public String addShoppingCartPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,String username,int storeId,Boolean immediate,int id){
    //     return storesService.addShoppingCartPurchasePolicy(quantity, price, date, atLeast, weight, age, username, storeId, immediate, id);
    // }

    // public String addUserPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,double userAge,String username,int storeId,Boolean immediate,int id){
    //     return storesService.addUserPurchasePolicy(quantity, price, date, atLeast, weight, age, userAge, username, storeId, immediate, id);
    // }

    public String addLogicalPurchase(String username,int storeId,String logicalRule,int id) throws Exception{
        return storesService.addLogicalPurchase(username, storeId, logicalRule, id);
    }   

    // public String getStorePurchaseHistory(int storeId){
    //     return storesService.getStorePurchaseHistory(storeId);
    // }

    public String getUserPurchaseHistory(String userId){
        return storesService.getUserPurchaseHistory(userId);
    }

    public String removePurchaseFromStore(int storeId, int purchaseId){
        return storesService.removePurchaseFromStore(storeId, purchaseId);
    }


    public String removePurchaseFromUser(String userId, int purchaseId){
        return storesService.removePurchaseFromUser(userId, purchaseId);
    }

    private PurchaseMethod initPurchaseMethod(Boolean immediate, int quantity, double price, LocalDate date, int atLeast, double weight, double age, String username, int storeId){
        return marketService.initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
    } 
    public List<String> getStorePurchaseHistory(int storeId) throws Exception {
        return storesService.getStorePurchaseHistory(storeId);
    }






    public String removePurchaseStore(Integer storeId, Integer purchaseId) throws Exception {
        return storesService.removePurchaseStore(storeId, purchaseId);
    }

    public String removePurchaseUser(String username, Integer purchaseId) throws Exception {
        return userService.removePurchaseUser(username, purchaseId);
    }

    public Integer getCategory(String name) {
        return marketService.getCategory(name);
    }

    public List<CartItemDTO> getCart(String username) throws Exception {
        return userService.getCart(username);
    }

    public String addCategoryDiscountPolicy(boolean b, int price, int quantity, double percentage, String categoryName, int storeId, String username, int i) throws Exception {
        if(marketService.hasCategory(categoryName)){
            int category = marketService.getCategory(categoryName);
            return storesService.addCategoryDiscountPolicy(b, price, quantity, percentage, category, storeId, username, i);
        }
        else{
            throw new Exception("category does not exist.");
        }
    }

    public String addProductDiscountPolicy(boolean b, int price, int quantity, double percentage, String productName, int storeId, String username, int i) throws Exception {
        int productId = -1;
        for(Product p : Market.getPC().getProducts()){
            if(p.getName().equals(productName)){
                productId = p.getId();
            }
        }
        if(productId == -1){
            throw new Exception("Product does not exist");
        }
        return storesService.addProductDiscountPolicy(b, price, quantity, percentage, productId, storeId, username, i);
    }

    public String addStoreDiscountPolicy(boolean b, int price, int quantity, double percentage, int storeId, String username, int i) throws Exception {
        return storesService.addStoreDiscountPolicy(b, price, quantity, percentage, storeId, username, i);
    }

    public String addLogicalDiscountPolicy(String username, int storeId, String logicalRule, int i) throws Exception {
        // DiscountPolicyController.LogicalRule rule = logicalRule.equals("AND") ? DiscountPolicyController.LogicalRule.AND :
        //         logicalRule.equals("OR") ? DiscountPolicyController.LogicalRule.OR :
        //                 logicalRule.equals("XOR") ? DiscountPolicyController.LogicalRule.XOR :
        //                 DiscountPolicyController.LogicalRule.IF_THEN;
        return storesService.addLogicalDiscount(username, storeId, logicalRule, i);
    }

    public String addCategoryPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age, String categoryName, String username, int storeId, boolean b, int i) throws Exception {
        if(marketService.hasCategory(categoryName)){
            int category = marketService.getCategory(categoryName);
            return storesService.addCategoryPurchasePolicy(quantity, price, date, atLeast, weight, age, category, username, storeId, b, i);
        }
        else{
            throw new Exception("category does not exist.");
        }
    }

    public String addProductPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age, String productName, String username, int storeId, boolean b, int i) throws Exception {
        int productId = -1;
        for(Product p : Market.getPC().getProducts()){
            if(p.getName().equals(productName)){
                productId = p.getId();
            }
        }
        if(productId == -1){
            throw new Exception("Product does not exist");
        }
        return storesService.addProductPurchasePolicy(quantity, price, date, atLeast, weight, age, productId, username, storeId,b, i);
    }

    public String addShoppingCartPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age, String username, int storeId, boolean b, int i) throws Exception {
        return storesService.addShoppingCartPurchasePolicy(quantity, price, date, atLeast, weight, age, username, storeId, b, i);
    }

    public String addUserPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age, String username, int storeId, boolean b, int i) throws Exception {
        return storesService.addUserPurchasePolicy(quantity, price, date, atLeast, weight, age, age, username, storeId, b, i);
    }

    public String addLogicalPurchasePolicy(String username, int storeId, String logicalRule, int i) throws Exception {
        // PurchasePolicyController.LogicalRule rule = logicalRule.equals("AND") ? PurchasePolicyController.LogicalRule.AND :
        //         logicalRule.equals("OR") ? PurchasePolicyController.LogicalRule.OR :
        //                 logicalRule.equals("XOR") ? PurchasePolicyController.LogicalRule.OR :
        //                         PurchasePolicyController.LogicalRule.IF_THEN;
        return storesService.addLogicalPurchase(username, storeId, logicalRule, i);
    }

    public List<OfferDTO> getOffers(int storeId, String username) {
        return storesService.getOffers(storeId,username);
    }

    public String approveOffer(String username, String offerName, int storeId, int productId) throws Exception {
        return storesService.approveOffer(username, offerName, storeId,productId);
    }

    public String rejectOffer(String username, String offerName, int storeId, int productId) throws Exception {
        return storesService.rejectOffer(username,offerName, storeId,productId);
    }

    public String sendOffer(String username, int storeId, int productId,Double price, Double offerPrice) {
        return storesService.sendOffer(username, storeId,productId,price, offerPrice);
    }

    public List<ProductDTO> search(SearchEntity entity){
        return marketService.search(entity);
    }

}
