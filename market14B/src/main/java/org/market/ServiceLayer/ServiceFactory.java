package org.market.ServiceLayer;

import org.market.Web.DTOS.CartItemDTO;
import org.market.Web.DTOS.PermissionDTO;
import org.market.Web.DTOS.ProductDTO;
import org.market.Web.DTOS.StoreDTO;
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
        try {
            Register("admin", "password", 24);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        addToSystemManagers("admin");
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

    public void suspendUserIndefinitely(String systemManager, String username) throws Exception {
        marketService.suspendUserIndefinitely(systemManager,username);
    }

    public void suspendUserTemporarily(String systemManager, String username, int durationInSeconds) throws Exception {
        marketService.suspendUserTemporarily(systemManager,username,durationInSeconds);
    }

    public void resumeUserIndefinitely(String systemManager, String username) throws Exception {
        marketService.resumeUserIndefinitely(systemManager,username);
    }

    public void resumeUser(String systemManager, String username, int duration) throws Exception {
        marketService.resumeUserTemporarily(systemManager,username,duration);
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
    }//@

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
}
