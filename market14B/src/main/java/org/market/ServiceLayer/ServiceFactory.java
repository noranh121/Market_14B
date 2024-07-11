package org.market.ServiceLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.market.DomainLayer.backend.StorePackage.Discount.DiscountPolicyController;
import org.market.DomainLayer.backend.StorePackage.Purchase.PurchasePolicyController;
import org.market.Web.DTOS.PermissionDTO;
import org.market.Web.DTOS.ProductDTO;
import org.market.Web.DTOS.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceFactory {
    // private MarketService marketService = new MarketService();
    // private UserService userService = new UserService();
    // private StoresService storesService = new StoresService();
    private MarketService marketService;
    private UserService userService;
    private StoresService storesService;

    //private static ServiceFactory instance;

    // public static synchronized ServiceFactory getInstance() {
    //     if (instance == null) {
    //         instance = new ServiceFactory();
    //     }
    //     return instance;
    // }


    @Autowired
    public ServiceFactory(MarketService marketService, UserService userService,StoresService storesService){
        this.marketService = marketService;
        this.userService= userService;
        this.storesService = storesService;
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

    public String suspendUserIndefinitely(String systemManager, String username) {
        return marketService.suspendUserIndefinitely(systemManager,username);
    }

    public String suspendUserTemporarily(String systemManager, String username, int durationInSeconds){
        return marketService.suspendUserTemporarily(systemManager,username,durationInSeconds);
    }

    public String resumeUserIndefinitely(String systemManager, String username) {
        return marketService.resumeUserIndefinitely(systemManager,username);
    }

    public String resumeUser(String systemManager, String username, int duration) {
        return marketService.resumeUserTemporarily(systemManager,username,duration);
    }

    public String viewSuspended(String systemManager) {
        return marketService.viewSuspended(systemManager);
    }

    public void addToSystemManagers(String admin) {
        marketService.addToSystemManagers(admin);
    }

    public String viewSystemPurchaseHistory(String username){
        return marketService.viewSystemPurchaseHistory(username);
    }

    // UserService
    public String EnterAsGuest(double age) {
        return userService.EnterAsGuest(age);
    }

    public String GuestExit(String username) {
        return userService.GuestExit(username);
    }

    public String Login(String guest, String username, String password) {
        return userService.Login(guest, username, password);
    }

    public String Logout(String username) {
        return userService.Logout(username);
    }

    public Response<String> Register(String username, String password,double age) {
        return userService.Register(username, password,age);
    }

    public Response<String> Buy(String username,String currency,String card_number,int month,int year,String ccv,
                                String address, String city, String country, int zip) {
        return userService.Buy(username,currency,card_number,month,year,ccv,
                address, city, country, zip);
    }

    public Response<String> addToCart(String username, Integer product, int storeId, int quantity) {
        return userService.addToCart(username, product, storeId, quantity);
    }

    public Response<String> inspectCart(String username) {
        return userService.inspectCart(username);
    }

    public Response<String> removeCartItem(String username, int storeId, int product) {
        return userService.removeCartItem(username, storeId, product);
    }

    public Response<String> EditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner,
            Boolean storeManager, Boolean[] pType) throws Exception {
        return userService.EditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
    }

    public Response<String> AssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
        return userService.AssignStoreManager(storeId, ownerUserName, username, pType);
    }

    public Response<String> AssignStoreOnwer(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
        return userService.AssignStoreOnwer(storeId, ownerUserName, username, pType);
    }

    public Response<String> unassignUser(int storeID, String ownerUserName, String userName) throws Exception {
        return userService.unassignUser(storeID, ownerUserName, userName);
    }

    public String resign(int storeID, String username) throws Exception {
        return userService.resign(storeID, username);
    }

    // StoreService
    public Response<String> initStore(String userName, String Description) {
        return storesService.initStore(userName, Description);
    }

    public String addProduct(int productId, int storeId, double price, int quantity, String username,double weight) throws Exception {
        return storesService.addProduct(productId, storeId, price, quantity, username,weight);
    }//@

    public Response<String> removeProduct(int productId, int storeId, String username) {
        return storesService.removeProduct(productId, storeId, username);
    }

    public Response<String> EditProducPrice(int productId, int storeId, Double newPrice, String username) throws Exception {
        return storesService.EditProducPrice(productId, storeId, newPrice, username);
    }

    public Response<String>  EditProductQuantity(int productId, int storeId, int newQuantity, String username) throws Exception {
        return storesService.EditProductQuantity(productId, storeId, newQuantity, username);
    }

    public Response<String> closeStore(int storeId, String username) {
        return storesService.closeStore(storeId, username);
    }

    public Response<String> openStore(int storeId, String username) {
        return storesService.openStore(storeId, username);
    }

    public Response<String> getInfo(int storeId, String username) {
        return storesService.getInfo(storeId, username);
    }

    public Response<String> initProduct(String username,String productName, int categoryId, String description, String brand,double weight){
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

    public String addCategoryDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int categoryId,int storeId,String username,int id){
        return storesService.addCategoryDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, categoryId, storeId, username, id);
    }

    public String addProductDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int productId,int storeId,String username,int id){
        return storesService.addProductDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, productId, storeId, username, id);
    }

    public String addStoreDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int storeId,String username,int id){
        return storesService.addStoreDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, storeId, username, id);
    }    

    public String addNmericalDiscount(String username,int storeId,Boolean ADD,int id){
        return storesService.addNmericalDiscount(username, storeId, ADD, id);
    }

    public String addLogicalDiscount(String username,int storeId,DiscountPolicyController.LogicalRule logicalRule,int id){
        return storesService.addLogicalDiscount(username, storeId, logicalRule, id);
    }

    public String addCategoryPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int categoryId,String username,int storeId,Boolean immediate,int id){
        return storesService.addCategoryPurchasePolicy(quantity, price, date, atLeast, weight, age, categoryId, username, storeId, immediate, id);
    }

    public String addProductPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int productId,String username,int storeId,Boolean immediate,int id){
        return storesService.addProductPurchasePolicy(quantity, price, date, atLeast, weight, age, productId, username, storeId, immediate, id);
    }

    public String addShoppingCartPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,String username,int storeId,Boolean immediate,int id){
        return storesService.addCategoryPurchasePolicy(quantity, price, date, atLeast, weight, age, id, username, storeId, immediate, id);
    }

    public String addUserPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,double userAge,String username,int storeId,Boolean immediate,int id){
        return storesService.addUserPurchasePolicy(quantity, price, date, atLeast, weight, age, userAge, username, storeId, immediate, id);
    }

    public String addLogicalPurchase(String username,int storeId,PurchasePolicyController.LogicalRule logicalRule,int id){
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
}
