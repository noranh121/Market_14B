package DomainLayer.backend;

import DomainLayer.backend.ProductPackage.Product;
import DomainLayer.backend.StorePackage.StoreController;
import DomainLayer.backend.UserPackage.UserController;

import java.util.logging.Logger;

public class Market {
    private static final Logger LOGGER = Logger.getLogger(Market.class.getName());

    //List<Users> SystemManagers;
    //boolean ON/OFF
    private UserController userController = UserController.getInstance();
    private StoreController storeController= StoreController.getInstance();
    private Permissions permissions = Permissions.getInstance();
    private static Market instance;
    public static Market getInstance() {
        if (instance == null)
            instance = new Market();
        return instance;
    }


    //while OFFLINE only the login function is reachable, if SystemManager ==> start market + add to list SystemManagers
    //if not ==> "Market IS OFFLINE"
    //checkAtLeastOne ==> checks if there is atleast one system manager in the system ===> list.size() >= 1;



    public String initStore(String userName, String Description) throws Exception {
        LOGGER.info("userName: " + userName+", Description: " + Description);
        if(userController.isRegistered(userName)){
            int storeID =  storeController.initStore(userName, Description);
            return permissions.initStore(storeID,userName);
        }
        else{
            LOGGER.severe(userName + " is not registered");
            throw new Exception(userName + " is not registered");
        }
    }
    //viewsystemPurchaseHistory(username){return info;} ----> ONLY System Manager

    public String EnterAsGuest() throws Exception {
        return userController.EnterAsGuest();
    }

    public String GuestExit(String username) throws Exception {
        return userController.GuestExit(username);
    }

    public String Login(String guest, String username, String password) throws Exception {
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
        return userController.addToCart(username,product, storeId, quantity);
    }

    public String inspectCart(String username) throws Exception {
        return userController.inspectCart(username);
    }

    public String removeCartItem(String username, int storeId, int product) throws Exception {
        return userController.removeCartItem(username,storeId,product);
    }

    public String EditPermissions(int storeID,String ownerUserName, String userName, Boolean storeOwner, Boolean storeManager, Boolean[] pType) throws Exception {
        return userController.EditPermissions(storeID,ownerUserName,userName,storeOwner,storeManager,pType);
    }

    public String AssignStoreManager(int storeId,String ownerUserName,String username,Boolean[] pType) throws Exception {
        return userController.AssignStoreManager(storeId,ownerUserName,username,pType);
    }

    public String AssignStoreOwner(int storeId,String ownerUserName,String username,Boolean[] pType) throws Exception {
        return userController.AssignStoreOwner(storeId,ownerUserName,username,pType);
    }


    public  String unassignUser(int storeID, String ownerUserName,String userName) throws Exception {
        return permissions.deletePermission(storeID, ownerUserName, userName);
    }


    public String addProduct(int productId,int storeId,double price,int quantity,String username) throws Exception {
        if(permissions.getPermission(storeId,username).getPType()[Permission.permissionType.editProducts.index]){
            return storeController.addProduct(productId,storeId,price,quantity);
        }
        else{
            LOGGER.severe(username+" has no permission to add products");
            throw new Exception(username+" has no permission to add products");
        }
    }

    public String RemoveProduct(int productId,int storeId,String username) throws Exception {
        if(permissions.getPermission(storeId,username).getPType()[Permission.permissionType.editProducts.index]){
            return storeController.removeProduct(productId,storeId);
        }
        else{
            LOGGER.severe(username+" has no permission to edit products");
            throw new Exception(username+" has no permission to edit products");
        }
    }

    public String EditProductName(int productId,int storeId,String newName,String username) throws Exception {
        if(permissions.getPermission(storeId,username).getPType()[Permission.permissionType.editProducts.index]){
            return storeController.EditProductName(productId,storeId,newName);
        }
        else{
            LOGGER.severe(username+" has no permission to edit products");
            throw new Exception(username+" has no permission to edit products");
        }
    }


    public String EditProductPrice(int productId,int storeId,Double newPrice,String username) throws Exception {
        if(permissions.getPermission(storeId,username).getPType()[Permission.permissionType.editProducts.index]){
            return storeController.EditProducPrice(productId,storeId,newPrice);
        }
        else{
            LOGGER.severe(username+" has no permission to edit products");
            throw new Exception(username+" has no permission to edit products");
        }
    }
    public String EditProductQuantity(int productId,int storeId,int newQuantity,String username) throws Exception {
        if(permissions.getPermission(storeId,username).getPType()[Permission.permissionType.editProducts.index]){
            return storeController.EditProductQuantity(productId,storeId,newQuantity);
        }
        else{
            LOGGER.severe(username+" has no permission to edit products");
            throw new Exception(username+" has no permission to edit products");
        }
    }
    public String CloseStore(int storeId,String username) throws Exception {
        if(permissions.getPermission(storeId,username).getStoreOwner()){
            return storeController.closeStore(storeId);
        }
        else{
            LOGGER.severe(username+" has no permission to close the store");
            throw new Exception(username+" has no permission to close the store");
        }
    }

    public String OpenStore(int storeId,String username) throws Exception {
        if(permissions.getPermission(storeId,username).getStoreOwner()){
            return storeController.openStore(storeId);
        }
        else{
            LOGGER.severe(username+" has no permission to open the store");
            throw new Exception(username+" has no permission to open the store");
        }
    }

    public String getInfo(int storeId,String username) throws Exception {
        if(userController.isRegistered(username)){
            return storeController.getInfo(storeId);
        }
        else{
            LOGGER.severe(username+" has no permission to open the store");
            throw new Exception(username+" has no permission to open the store");
        }
    }
}
