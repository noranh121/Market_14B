package DomainLayer.backend;

import DomainLayer.backend.ProductPackage.Category;
import DomainLayer.backend.ProductPackage.CategoryController;
import DomainLayer.backend.ProductPackage.ProductController;
import DomainLayer.backend.StorePackage.CategoryDiscount;
import DomainLayer.backend.StorePackage.ConditionalDiscount;
import DomainLayer.backend.StorePackage.Discount;
import DomainLayer.backend.StorePackage.ProductDiscount;
import DomainLayer.backend.StorePackage.StandardDiscount;
// import DomainLayer.backend.ProductPackage.CategoryController;
// import DomainLayer.backend.ProductPackage.ProductController;
import DomainLayer.backend.StorePackage.StoreController;
import DomainLayer.backend.StorePackage.StoreDiscount;
import DomainLayer.backend.UserPackage.UserController;

import java.util.HashSet;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Market {
    public static final Logger LOGGER = Logger.getLogger(Market.class.getName());

    // List<Users> SystemManagers;
    // boolean ON/OFF
    private UserController userController = UserController.getInstance();
    private StoreController storeController = StoreController.getInstance();
    private Permissions permissions = Permissions.getInstance();
    private PurchaseHistory purchaseHistory = PurchaseHistory.getInstance();
    private ProductController productController=ProductController.getInstance();
    private CategoryController categoryController=CategoryController.getinstance();

    private Boolean Online = false;
    private HashSet<String> systemManagers = new HashSet<>();
    private static Market instance;

    public static Market getInstance() {
        if (instance == null)
            instance = new Market();
        return instance;
    }

    private Market() {
        try {
            FileHandler fileHandler = new FileHandler("Market", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set up logger handler.", e);
        }
    }

    public void setMarketOnline(String username) throws Exception {
        if (!systemManagers.contains(username)) {
            LOGGER.severe("only system managers can change market's activity");
            throw new Exception("only system managers can change market's activity");
        }
        LOGGER.info("market is Online");
        Online = true;
    }

    public void setMarketOFFLINE(String username) throws Exception {
        if (!systemManagers.contains(username)) {
            LOGGER.severe("only system managers can change market's activity");
            throw new Exception("only system managers can change market's activity");
        }
        LOGGER.info("market is OFFLINE");
        Online = false;
    }

    public HashSet<String> getSystemManagers() {
        return systemManagers;
    }

    public boolean getOnline() {
        return Online;
    }

    // this is for testing
    public void setToNull() {
        instance = null;
        permissions.setToNull();
        userController.setToNull();
        storeController.setToNull();
        systemManagers = new HashSet<>();
    }

    // while OFFLINE only the login function is reachable, if SystemManager ==>
    // start market + add to list SystemManagers
    // if not ==> "Market IS OFFLINE"
    // checkAtLeastOne ==> checks if there is atleast one system manager in the
    // system ===> list.size() >= 1;

    // User
    public String EnterAsGuest() throws Exception {
        return userController.EnterAsGuest();
    }

    public String GuestExit(String username) throws Exception {
        return userController.GuestExit(username);
    }

    public String Login(String guest, String username, String password) throws Exception {
        if (!Online) {
            if (systemManagers.contains(username))
                return userController.Login(guest, username, password);
            else {
                LOGGER.severe("Market IS OFFLINE");
                throw new Exception("Market IS OFFLINE");
            }
        }
        return userController.Login(guest, username, password);
    }

    public String Logout(String username) throws Exception {
        return userController.Logout(username);
    }

    public String Register(String username, String password) throws Exception {
        return userController.Register(username, password);
    }

    public double Buy(String username) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new Exception("can't buy user is suspended");
        }
        return userController.Buy(username);
    }

    public String addToCart(String username, Integer product, int storeId, int quantity) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new Exception("can't add to cart user is suspended");
        }
        return userController.addToCart(username, product, storeId, quantity);
    }

    public String inspectCart(String username) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new Exception("can't inspect cart user is suspended");
        }
        return userController.inspectCart(username);
    }

    public String removeCartItem(String username, int storeId, int product) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new Exception("can't remove cart item user is suspended");
        }
        return userController.removeCartItem(username, storeId, product);
    }


    // Permissions
    public String EditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner,
            Boolean storeManager, Boolean[] pType) throws Exception {
        return userController.EditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
    }

    public String AssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
        return userController.AssignStoreManager(storeId, ownerUserName, username, pType);
    }

    public String AssignStoreOwner(int storeId, String ownerUserName, String username, Boolean[] pType)
            throws Exception {
        return userController.AssignStoreOwner(storeId, ownerUserName, username, pType);
    }

    public String unassignUser(int storeID, String ownerUserName, String userName) throws Exception {
        return permissions.deletePermission(storeID, ownerUserName, userName);
    }

    public String resign(int storeID, String username) throws Exception {
        return permissions.deleteStoreOwner(storeID, username);
    }

    public String suspendUser(String systemManager, String username) throws Exception {
        if (systemManagers.contains(systemManager)) {
            return permissions.suspendUser(username);
        }
        else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    public String suspendUserSeconds(String systemManager, String username, int duration) throws Exception {
        if (systemManagers.contains(systemManager)) {
            return permissions.suspendUserSeconds(username,duration);
        }
        else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    public String resumeUser(String systemManager, String username) throws Exception {
        if (systemManagers.contains(systemManager)) {
            return permissions.resumeUser(username);
        }
        else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    public String resumeUserSeconds(String systemManager, String username, int duration) throws Exception {
        if (systemManagers.contains(systemManager)) {
            return permissions.resumeTemporarily(username,duration);
        }
        else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    public String viewSuspended(String systemManager) throws Exception {
        if (systemManagers.contains(systemManager)) {
            return permissions.viewSuspended();
        }
        else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    // Category
    public String addCatagory(int storeId, String catagory, String username) throws Exception{
        LOGGER.info("storeId: "+storeId+", category: "+catagory+", username: "+username);
        if (systemManagers.contains(username)) {
            categoryController.addCategory(catagory);
            LOGGER.info("category added successfully");
            return "category added successfully";
        } else {
            LOGGER.severe(username + " is not system manager");
            throw new Exception(username + " is not system manager");
        }
    }

    // Product
    public String initProduct(String username,String productName, int categoryId, String description, String brand) throws Exception{
        LOGGER.info("username: "+username+",productName : " + productName + ", categoryId: " + categoryId+", description: "+description+", brand: "+brand);
        if(systemManagers.contains(username)){
            Category category=categoryController.getCategory(categoryId);
            return productController.addProduct(productName, category, description, brand);
        }
        else{
            LOGGER.severe(username + " is not system manager");
            throw new Exception(username + " is not system manager");
        }
    }

    // Store
    public String setProductDiscountPolicy(int storeId ,String username,Boolean discountType,double conditionalprice,double conditionalQuantity, double discountPercentage,int productId) throws Exception{
        LOGGER.info("storeId: "+storeId+", userName: " + username + ", discountType: " +discountType+", discountPercentage: "+discountPercentage+", productId: "+productId);
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        Discount type=null;
        if(discountType){
            type=new StandardDiscount();
        }
        else{
            type=new ConditionalDiscount(conditionalprice,conditionalQuantity);
        }
        ProductDiscount discountPolicy=new ProductDiscount(type,discountPercentage,productId);
        storeController.getStore(storeId).setDiscountPolicy(discountPolicy);
        LOGGER.info("discount policy updated");
        return "discount policy updated";
    }

    public String setCategoryDiscountPolicy(int storeId ,String username,Boolean discountType,double conditionalprice,double conditionalQuantity, double discountPercentage,int categoryId) throws Exception{
        LOGGER.info("storeId: "+storeId+", userName: " + username + ", discountType: " +discountType+", discountPercentage: "+discountPercentage+", categoryId: "+categoryId);
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        Discount type=null;
        if(discountType){
            type=new StandardDiscount();
        }
        else{
            type=new ConditionalDiscount(conditionalprice,conditionalQuantity);
        }
        CategoryDiscount discountPolicy=new CategoryDiscount(type,discountPercentage,categoryId);
        storeController.getStore(storeId).setDiscountPolicy(discountPolicy);
        LOGGER.info("discount policy updated");
        return "discount policy updated";
    }

    public String setStoreDiscountPolicy(int storeId ,String username,Boolean discountType,double conditionalprice,double conditionalQuantity, double discountPercentage) throws Exception{
        LOGGER.info("storeId: "+storeId+", userName: " + username + ", discountType: " +discountType+", discountPercentage: "+discountPercentage);
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        Discount type=null;
        if(discountType){
            type=new StandardDiscount();
        }
        else{
            type=new ConditionalDiscount(conditionalprice,conditionalQuantity);
        }
        StoreDiscount discountPolicy=new StoreDiscount(type, discountPercentage);
        storeController.getStore(storeId).setDiscountPolicy(discountPolicy);
        LOGGER.info("discount policy updated");
        return "discount policy updated";
    }

    public String initStore(String userName, String Description) throws Exception {
        LOGGER.info("userName: " + userName + ", Description: " + Description);
        if (userController.isRegistered(userName)) {
            int storeID = storeController.initStore(userName, Description);
            return permissions.initStore(storeID, userName);
        } else {
            LOGGER.severe(userName + " is not registered");
            throw new Exception(userName + " is not registered");
        }
    }

    public String addProduct(int productId, int storeId, double price, int quantity, String username) throws Exception {
        if (permissions.getPermission(storeId, username).getPType()[Permission.permissionType.editProducts.index]) {
            return storeController.addProduct(productId, storeId, price, quantity);
        } else {
            LOGGER.severe(username + " has no permission to add products");
            throw new Exception(username + " has no permission to add products");
        }
    }

    public String RemoveProduct(int productId, int storeId, String username) throws Exception {
        if (permissions.getPermission(storeId, username).getPType()[Permission.permissionType.editProducts.index]) {
            return storeController.removeProduct(productId, storeId);
        } else {
            LOGGER.severe(username + " has no permission to edit products");
            throw new Exception(username + " has no permission to edit products");
        }
    }

    // public String EditProductName(int productId,int storeId,String newName,String
    // username) throws Exception {
    // if(permissions.getPermission(storeId,username).getPType()[Permission.permissionType.editProducts.index]){
    // return storeController.EditProductName(productId,storeId,newName);
    // }
    // else{
    // LOGGER.severe(username+" has no permission to edit products");
    // throw new Exception(username+" has no permission to edit products");
    // }
    // }

    public String EditProductPrice(int productId, int storeId, Double newPrice, String username) throws Exception {
        if (permissions.getPermission(storeId, username).getPType()[Permission.permissionType.editProducts.index]) {
            return storeController.EditProducPrice(productId, storeId, newPrice);
        } else {
            LOGGER.severe(username + " has no permission to edit products");
            throw new Exception(username + " has no permission to edit products");
        }
    }

    public String EditProductQuantity(int productId, int storeId, int newQuantity, String username) throws Exception {
        if (permissions.getPermission(storeId, username).getPType()[Permission.permissionType.editProducts.index]) {
            return storeController.EditProductQuantity(productId, storeId, newQuantity);
        } else {
            LOGGER.severe(username + " has no permission to edit products");
            throw new Exception(username + " has no permission to edit products");
        }
    }

    public String CloseStore(int storeId, String username) throws Exception {
        if (permissions.getPermission(storeId, username).getStoreOwner()) {
            return storeController.closeStore(storeId);
        } else {
            LOGGER.severe(username + " has no permission to close the store");
            throw new Exception(username + " has no permission to close the store");
        }
    }

    public String OpenStore(int storeId, String username) throws Exception {
        if (permissions.getPermission(storeId, username).getStoreOwner()) {
            return storeController.openStore(storeId);
        } else {
            LOGGER.severe(username + " has no permission to open the store");
            throw new Exception(username + " has no permission to open the store");
        }
    }

    public String getInfo(int storeId, String username) throws Exception {
        if (userController.isRegistered(username)) {
            return storeController.getInfo(storeId);
        } else {
            LOGGER.severe(username + " has no permission to open the store");
            throw new Exception(username + " has no permission to open the store");
        }
    }

    // Purchase History
    public String viewsystemPurchaseHistory(String username) throws Exception {
        if (!systemManagers.contains(username)) {
            LOGGER.severe("only system managers can view purchase history");
            throw new Exception("only system managers can view purchase history");
        }
        return PurchaseHistory.getInstance().viewPurchaseHistory();
    }

    // public String addPurchase(int storeId, int userId, Purchase purchase) {
    //     purchaseHistory.addPurchase(storeId, userId, purchase);
    //     LOGGER.info("purcahse added");
    //     return "purchase added";
    // }

    public String getStorePurchaseHistory(int storeId) {
        List<Purchase> purchases = purchaseHistory.getStorePurchaseHistory(storeId);
        String info = "";
        for (Purchase purchase : purchases) {
            info += purchase.FetchInfo();
        }
        return info;
    }

    public String getUserPurchaseHistory(String userId) {
        List<Purchase> purchases = purchaseHistory.getUserPurchaseHistory(userId);
        String info = "";
        for (Purchase purchase : purchases) {
            info += purchase.FetchInfo();
        }
        return info;
    }

    public synchronized String removePurchaseFromStore(int storeId, int purchaseId) throws Exception {
        return purchaseHistory.removePurchaseFromStore(storeId, purchaseId);
    }

    public synchronized String removePurchaseFromUser(String userId, int purchaseId) throws Exception {
        return purchaseHistory.removePurchaseFromUser(userId, purchaseId);
    }

}
