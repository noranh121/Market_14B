package org.market.ServiceLayer;

import java.util.ArrayList;
import java.util.List;

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
    public void setMarketOnline(String username) throws Exception {
        marketService.setMarketOnline(username);
    }

    public void setMarketOFFLINE(String username) throws Exception {
        marketService.setMarketOFFLINE(username);
    }

    public void addCatagory(int storeId, String catagory, String username) throws Exception {
        marketService.addCatagory(storeId, catagory, username);
    }

    public void suspendUserIndefinitely(String systemManager, String username) {
        marketService.suspendUserIndefinitely(systemManager,username);
    }

    public void suspendUserTemporarily(String systemManager, String username, int durationInSeconds){
        marketService.suspendUserTemporarily(systemManager,username,durationInSeconds);
    }

    public void resumeUserIndefinitely(String systemManager, String username) {
        marketService.resumeUserIndefinitely(systemManager,username);
    }

    public void resumeUser(String systemManager, String username, int duration) {
        marketService.resumeUserTemporarily(systemManager,username,duration);
    }

    public String viewSuspended(String systemManager) {
        return marketService.viewSuspended(systemManager);
    }

    public void addToSystemManagers(String admin) {
        marketService.addToSystemManagers(admin);
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
    }//@

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

    public Response<Integer> initProduct(String username,String productName, int categoryId, String description, String brand,double weight){
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
}
