package DomainLayer.backend;

// import DomainLayer.backend.ProductPackage.CategoryController;
// import DomainLayer.backend.ProductPackage.ProductController;
import DomainLayer.backend.StorePackage.StoreController;
import DomainLayer.backend.UserPackage.UserController;

import java.util.HashSet;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Market {
    private static final Logger LOGGER = Logger.getLogger(Market.class.getName());

    // List<Users> SystemManagers;
    // boolean ON/OFF
    private UserController userController = UserController.getInstance();
    private StoreController storeController = StoreController.getInstance();
    private Permissions permissions = Permissions.getInstance();
    private PurchaseHistory purchaseHistory = PurchaseHistory.getInstance();
    // private ProductController productController=ProductController.getInstance();
    // private CategoryController
    // categoryController=CategoryController.getinstance();

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
        return userController.Buy(username);
    }

    public String addToCart(String username, Integer product, int storeId, int quantity) throws Exception {
        return userController.addToCart(username, product, storeId, quantity);
    }

    public String inspectCart(String username) throws Exception {
        return userController.inspectCart(username);
    }

    public String removeCartItem(String username, int storeId, int product) throws Exception {
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

    // Store
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

    public String addPurchase(int storeId, int userId, Purchase purchase) {
        purchaseHistory.addPurchase(storeId, userId, purchase);
        LOGGER.info("purcahse added");
        return "purchase added";
    }

    public String getStorePurchaseHistory(int storeId) {
        List<Purchase> purchases = purchaseHistory.getStorePurchaseHistory(storeId);
        String info = "";
        for (Purchase purchase : purchases) {
            info += purchase.FetchInfo();
        }
        return info;
    }

    public String getUserPurchaseHistory(int storeId) {
        List<Purchase> purchases = purchaseHistory.getUserPurchaseHistory(storeId);
        String info = "";
        for (Purchase purchase : purchases) {
            info += purchase.FetchInfo();
        }
        return info;
    }

    public synchronized String removePurchaseFromStore(int storeId, int purchaseId) throws Exception {
        return purchaseHistory.removePurchaseFromStore(storeId, purchaseId);
    }

    public synchronized String removePurchaseFromUser(int storeId, int purchaseId) throws Exception {
        return purchaseHistory.removePurchaseFromUser(storeId, purchaseId);
    }
}