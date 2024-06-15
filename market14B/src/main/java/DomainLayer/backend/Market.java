package DomainLayer.backend;

import DomainLayer.backend.ProductPackage.Category;
import DomainLayer.backend.ProductPackage.CategoryController;
import DomainLayer.backend.ProductPackage.ProductController;
import DomainLayer.backend.StorePackage.StoreController;
import DomainLayer.backend.StorePackage.Discount.CategoryDiscount;
import DomainLayer.backend.StorePackage.Discount.ConditionalDiscount;
import DomainLayer.backend.StorePackage.Discount.Discount;
import DomainLayer.backend.StorePackage.Discount.ProductDiscount;
import DomainLayer.backend.StorePackage.Discount.StandardDiscount;
import DomainLayer.backend.StorePackage.Discount.StoreDiscount;
import DomainLayer.backend.StorePackage.Discount.DiscountPolicyController.LogicalRule;
import DomainLayer.backend.StorePackage.Discount.Logical.ANDDiscountRule;
import DomainLayer.backend.StorePackage.Discount.Logical.ORDiscountRule;
import DomainLayer.backend.StorePackage.Discount.Logical.XORDiscountRule;
import DomainLayer.backend.StorePackage.Discount.Numerical.ADDDiscountRule;
import DomainLayer.backend.StorePackage.Discount.Numerical.AT_MOSTDiscountRule;
import DomainLayer.backend.StorePackage.Purchase.ANDPurchaseRule;
import DomainLayer.backend.StorePackage.Purchase.CategoryPurchase;
import DomainLayer.backend.StorePackage.Purchase.IF_THENPurchaseRule;
import DomainLayer.backend.StorePackage.Purchase.ImmediatePurchase;
import DomainLayer.backend.StorePackage.Purchase.ORPurchaseRule;
import DomainLayer.backend.StorePackage.Purchase.ProductPurchase;
import DomainLayer.backend.StorePackage.Purchase.PurchaseMethod;
import DomainLayer.backend.StorePackage.Purchase.ShoppingCartPurchase;
import DomainLayer.backend.StorePackage.Purchase.UserPurchase;
import DomainLayer.backend.UserPackage.UserController;

import java.time.LocalDate;
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
    private ProductController productController = ProductController.getInstance();
    private CategoryController categoryController = CategoryController.getinstance();


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
    public String EnterAsGuest(double age) throws Exception {
        return userController.EnterAsGuest(age);
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

    public String Register(String username, String password,double age) throws Exception {
        return userController.Register(username, password,age);
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
        } else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    public String suspendUserSeconds(String systemManager, String username, int duration) throws Exception {
        if (systemManagers.contains(systemManager)) {
            return permissions.suspendUserSeconds(username, duration);
        } else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    public String resumeUser(String systemManager, String username) throws Exception {
        if (systemManagers.contains(systemManager)) {
            return permissions.resumeUser(username);
        } else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    public String resumeUserSeconds(String systemManager, String username, int duration) throws Exception {
        if (systemManagers.contains(systemManager)) {
            return permissions.resumeTemporarily(username, duration);
        } else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    public String viewSuspended(String systemManager) throws Exception {
        if (systemManagers.contains(systemManager)) {
            return permissions.viewSuspended();
        } else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    // Category
    public String addCatagory(int storeId, String catagory, String username) throws Exception {
        LOGGER.info("storeId: " + storeId + ", category: " + catagory + ", username: " + username);
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
    public String initProduct(String username, String productName, int categoryId, String description, String brand,double weight)
            throws Exception {
        LOGGER.info("username: " + username + ",productName : " + productName + ", categoryId: " + categoryId
                + ", description: " + description + ", brand: " + brand);
        if (systemManagers.contains(username)) {
            Category category = categoryController.getCategory(categoryId);
            return productController.addProduct(productName, category, description, brand,weight);
        } else {
            LOGGER.severe(username + " is not system manager");
            throw new Exception(username + " is not system manager");
        }
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

    public String addProduct(int productId, int storeId, double price, int quantity, String username,double weight) throws Exception {
        if (permissions.getPermission(storeId, username).getPType()[Permission.permissionType.editProducts.index]) {
            return storeController.addProduct(productId, storeId, price, quantity,weight);
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

    public String addCategoryDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int categoryId,int storeId,String username) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        Discount discountType=initDiscount(standard, conditionalPrice, conditionalQuantity);
        CategoryDiscount categoryDiscount=new CategoryDiscount(discountType, discountPercentage, categoryId, -1);
        storeController.getStore(storeId).addDiscountComposite(categoryDiscount);
        LOGGER.info("category discount policy added");
        return "category discount policy added";
    }

    public String addProductDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int productId,int storeId,String username) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        Discount discountType=initDiscount(standard, conditionalPrice, conditionalQuantity);
        ProductDiscount productDiscount=new ProductDiscount(discountType, discountPercentage,productId,-1);
        storeController.getStore(storeId).addDiscountComposite(productDiscount);
        LOGGER.info("product discount policy added");
        return "product discount policy added";
    }

    public String addStoreDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int storeId,String username) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        Discount discountType=initDiscount(standard, conditionalPrice, conditionalQuantity);
        StoreDiscount storeDiscount=new StoreDiscount(discountType, discountPercentage,-1);
        storeController.getStore(storeId).addDiscountComposite(storeDiscount);
        LOGGER.info("store discount policy added");
        return "store discount policy added";
    }

    public Discount initDiscount(Boolean standard,double conditionalPrice,double conditionalQuantity){
        if(standard){
            return new StandardDiscount();
        }
        else{
            return new ConditionalDiscount(conditionalPrice, conditionalQuantity);
        }
    }

    public String addNmericalDiscount(String username,int storeId,Boolean ADD) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        if(ADD){
            ADDDiscountRule addDiscountRule=new ADDDiscountRule(-1);
            storeController.getStore(storeId).addDiscountComposite(addDiscountRule);
            LOGGER.info("ADD discount policy added");
            return "ADD discount policy added";
        }
        else{
            AT_MOSTDiscountRule at_MOSTDiscountRule=new AT_MOSTDiscountRule(-1);
            storeController.getStore(storeId).addDiscountComposite(at_MOSTDiscountRule);
            LOGGER.info("AT_MOST discount policy added");
            return "AT_MOST discount policy added";
        }
    }

    public String addLogicalDiscount(String username,int storeId,LogicalRule logicalRule) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        switch (logicalRule) {
            case AND:
                ANDDiscountRule andDiscountRule=new ANDDiscountRule(-1);
                storeController.getStore(storeId).addDiscountComposite(andDiscountRule);
                LOGGER.info("AND discount policy added");
                return "AND discount policy added";
            case OR:
                ORDiscountRule orDiscountRule=new ORDiscountRule(-1);
                storeController.getStore(storeId).addDiscountComposite(orDiscountRule);
                LOGGER.info("OR discount policy added");
                return "OR discount policy added";
            case XOR:
                XORDiscountRule xorDiscountRule=new XORDiscountRule(-1);
                storeController.getStore(storeId).addDiscountComposite(xorDiscountRule);
                LOGGER.info("XOR discount policy added");
                return "XOR discount policy added";
            default:
                LOGGER.severe("invaled logical rule");
                throw new Exception("invaled logical rule");
        }
    }

    public String addCategoryPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int categoryId,String username,int storeId) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        PurchaseMethod purchaseMethod=new ImmediatePurchase(quantity, price, date, atLeast, weight, age);
        CategoryPurchase categoryPurchase=new CategoryPurchase(purchaseMethod, categoryId, -1);
        storeController.getStore(storeId).addPurchaseComposite(categoryPurchase);
        LOGGER.info("category purchase policy added");
        return "category purchase policy added";
    }

    public String addProductPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int productId,String username,int storeId) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        PurchaseMethod purchaseMethod=new ImmediatePurchase(quantity, price, date, atLeast, weight, age);
        ProductPurchase productPurchase=new ProductPurchase(purchaseMethod, productId, storeId);
        storeController.getStore(storeId).addPurchaseComposite(productPurchase);
        LOGGER.info("product purchase policy added");
        return "product purchase policy added";
    }

    public String addShoppingCartPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,String username,int storeId) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        PurchaseMethod purchaseMethod=new ImmediatePurchase(quantity, price, date, atLeast, weight, age);
        ShoppingCartPurchase ShoppingCartPurchase=new DomainLayer.backend.StorePackage.Purchase.ShoppingCartPurchase(purchaseMethod, -1);
        storeController.getStore(storeId).addPurchaseComposite(ShoppingCartPurchase);
        LOGGER.info("shopping cart purchase policy added");
        return "shopping cart purchase policy added";
    }

    public String addUserPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,double userAge,String username,int storeId) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        PurchaseMethod purchaseMethod=new ImmediatePurchase(quantity, price, date, atLeast, weight, age);
        UserPurchase userPurchase=new UserPurchase(purchaseMethod, userAge, storeId);
        storeController.getStore(storeId).addPurchaseComposite(userPurchase);
        LOGGER.info("user purchase policy added");
        return "user purchase policy added";
    }

    public String addLogicalPurchase(String username,int storeId,LogicalRule logicalRule) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        switch (logicalRule) {
            case AND:
                ANDPurchaseRule andPurchaseRule=new ANDPurchaseRule(-1);
                storeController.getStore(storeId).addPurchaseComposite(andPurchaseRule);
                LOGGER.info("AND purchase policy added");
                return "AND purchase policy added";
            case OR:
                ORPurchaseRule orPurchaseRule=new ORPurchaseRule(-1);
                storeController.getStore(storeId).addPurchaseComposite(orPurchaseRule);
                LOGGER.info("OR purchase policy added");
                return "OR purchase policy added";
            case IF_THEN:
                IF_THENPurchaseRule if_THENDiscountRule=new IF_THENPurchaseRule(-1);
                storeController.getStore(storeId).addPurchaseComposite(if_THENDiscountRule);
                LOGGER.info("IF_THEN discount policy added");
                return "IF_THEN discount policy added";
            default:
                LOGGER.severe("invaled logical rule");
                throw new Exception("invaled logical rule");
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
            permissions.updateStoreOwners(storeId, storeId + " store was closed");
            return storeController.closeStore(storeId);
        } else {
            LOGGER.severe(username + " has no permission to close the store");
            throw new Exception(username + " has no permission to close the store");
        }
    }

    public String OpenStore(int storeId, String username) throws Exception {
        if (permissions.getPermission(storeId, username).getStoreOwner()) {
            permissions.updateStoreOwners(storeId, storeId + " store was opened");
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
    // purchaseHistory.addPurchase(storeId, userId, purchase);
    // LOGGER.info("purcahse added");
    // return "purchase added";
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
