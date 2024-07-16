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
import org.market.Web.Requests.SearchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                address, city, country, zip);
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

    public List<ProductDTO> search(SearchEntity entity){
        return marketService.search(entity);
}
    public String addCategoryDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int categoryId,int storeId,String username,int id) throws Exception{
        return storesService.addCategoryDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, categoryId, storeId, username, id);
    }

    public String addProductDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int productId,int storeId,String username,int id) throws Exception{
        return storesService.addProductDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, productId, storeId, username, id);
    }

    public String addStoreDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int storeId,String username,int id) throws Exception{
        return storesService.addStoreDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, storeId, username, id);
    }    

    public String addNmericalDiscount(String username,int storeId,Boolean ADD,int id) throws Exception{
        return storesService.addNmericalDiscount(username, storeId, ADD, id);
    }

    public String addLogicalDiscount(String username,int storeId,String logicalRule,int id){
        return storesService.addLogicalDiscount(username, storeId, logicalRule, id);
    }

    public String addCategoryPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int categoryId,String username,int storeId,Boolean immediate,int id){
        return storesService.addCategoryPurchasePolicy(quantity, price, date, atLeast, weight, age, categoryId, username, storeId, immediate, id);
    }

    public String addProductPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int productId,String username,int storeId,Boolean immediate,int id){
        return storesService.addProductPurchasePolicy(quantity, price, date, atLeast, weight, age, productId, username, storeId, immediate, id);
    }

    public String addShoppingCartPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,String username,int storeId,Boolean immediate,int id){
        return storesService.addShoppingCartPurchasePolicy(quantity, price, date, atLeast, weight, age, username, storeId, immediate, id);
    }

    public String addUserPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,double userAge,String username,int storeId,Boolean immediate,int id){
        return storesService.addUserPurchasePolicy(quantity, price, date, atLeast, weight, age, userAge, username, storeId, immediate, id);
    }

    public String addLogicalPurchase(String username,int storeId,String logicalRule,int id){
        return storesService.addLogicalPurchase(username, storeId, logicalRule, id);
    }   

    public String getStorePurchaseHistory(int storeId){
        return storesService.getStorePurchaseHistory(storeId);
    }

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
}
