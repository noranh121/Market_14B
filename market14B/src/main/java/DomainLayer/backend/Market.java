package DomainLayer.backend;

import DomainLayer.backend.API.PaymentExternalService.*;
import DomainLayer.backend.API.SupplyExternalService.*;
import DomainLayer.backend.ProductPackage.*;
import DomainLayer.backend.StorePackage.*;
import DomainLayer.backend.StorePackage.Discount.*;
import DomainLayer.backend.StorePackage.Discount.DiscountPolicyController.*;
import DomainLayer.backend.StorePackage.Discount.Logical.*;
import DomainLayer.backend.StorePackage.Discount.Numerical.*;
import DomainLayer.backend.StorePackage.Purchase.*;
import DomainLayer.backend.UserPackage.UserController;
import ServiceLayer.Response;

import java.time.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.logging.*;

import DataAccessLayer.DataController;

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
    private PaymentService paymentService=PaymentService.getInstance();
    private SupplyService supplyService=SupplyService.getInstance();
    private DataController dataController=DataController.getinstance();
    private FileHandler fileHandler;

    private Boolean Online = false;
    private List<String> systemManagers = Collections.synchronizedList(new ArrayList<>());
    private final Lock systemManagersLock = new ReentrantLock();
    private static Market instance;

    public static Market getInstance() {
        if (instance == null){
            instance = new Market();
        }
            
        return instance;
    }

    private Market() {
        try {
            systemManagers=dataController.getSystemManagers();
            Online=dataController.getOnline();
            fileHandler= new FileHandler("Market.log",true);
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
        dataController.setMarketOnline();
    }

    public void setMarketOFFLINE(String username) throws Exception {
        if (!systemManagers.contains(username)) {
            LOGGER.severe("only system managers can change market's activity");
            throw new Exception("only system mfanagers can change market's activity");
        }
        LOGGER.info("market is OFFLINE");
        Online = false;
        dataController.setMarketOFFLINE();
    }

    public List<String> getSystemManagers() {
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
        systemManagers = Collections.synchronizedList(new ArrayList<>());
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
        String response= userController.Login(guest, username, password);
        dataController.Login(username);
        return response;
    }

    public String Logout(String username) throws Exception {
        String response= userController.Logout(username);
        dataController.Logout(username);
        return response;
    }

    public String Register(String username, String password,double age) throws Exception {
        String response=userController.Register(username, password,age);
        dataController.Register(username, password, age);
        return response;
    }

    public double Buy(String username,String currency,String card_number,int month,int year,String ccv,
    String address, String city, String country, int zip) throws Exception {
        LOGGER.info("username: "+username+" currency: "+currency+" card_number: "+card_number+" month: "+month+" year: "+year+" ccv: "+ccv+" address: "+address+
        " city: "+city+" country: "+country+" zip: "+zip);
        if (permissions.isSuspended(username)) {
            throw new Exception("can't buy user is suspended");
        }
        double total=userController.Buy(username);
        paymentServiceProccess(username, currency, card_number, month, year, ccv, total);
        supplyServiceProccess(address,city,country,zip,username);
        userController.getUser(username).cleanShoppingCart();
        dataController.cleanShoppingCart(username);
        return total;
    }
    
    private void supplyServiceProccess(String address, String city, String country, int zip,String username) throws Exception {
        Response<Boolean> handshake=paymentService.handshake();
        if(!handshake.isError()){
            Response<Integer> transaction_id=supplyService.supply(username, address, city, country, zip);
            if(transaction_id.isError()){
                LOGGER.severe(transaction_id.getErrorMessage());
                throw new Exception(transaction_id.getErrorMessage());    
            }
        }
        else{
            LOGGER.severe(handshake.getErrorMessage());
            throw new Exception(handshake.getErrorMessage());
        }
    }

    private void paymentServiceProccess(String username,String currency,String card_number,int month,int year,String ccv,double amount) throws Exception{
        Response<Boolean> handshake=paymentService.handshake();
        if(!handshake.isError()){
            Response<Integer> transaction_id=paymentService.pay(amount, currency, card_number, month, year, card_number, ccv);
            if(transaction_id.isError()){
                LOGGER.severe(transaction_id.getErrorMessage());
                throw new Exception(transaction_id.getErrorMessage());
            }
            else{
                Boolean added=userController.getUser(username).addTransaction_id(transaction_id.getValue());
                if(!added){
                    Response<Integer> cancel_pay=paymentService.cancel_pay(transaction_id.getValue());
                    if(cancel_pay.isError()){
                        LOGGER.severe(cancel_pay.getErrorMessage());
                        throw new Exception(cancel_pay.getErrorMessage());
                    }
                }
            }
        }
        else{
            LOGGER.severe(handshake.getErrorMessage());
            throw new Exception(handshake.getErrorMessage());
        }
    }

    public String addToCart(String username, Integer product, int storeId, int quantity) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new Exception("can't add to cart user is suspended");
        }
        String response= userController.addToCart(username, product, storeId, quantity);
        dataController.addToCart(username, storeId, product, quantity);
        return response;
    }

    public String inspectCart(String username) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new Exception("can't inspect cart user is suspended");
        }
        String response= userController.inspectCart(username);
        // I used datacontroller in ShoppingCart class
        return response;
    }

    public String removeCartItem(String username, int storeId, int product) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new Exception("can't remove cart item user is suspended");
        }
        String response= userController.removeCartItem(username, storeId, product);
        dataController.removeCartItem(username, storeId, product);
        return response;
    }

    // Permissions
    public String EditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner,
            Boolean storeManager, Boolean[] pType) throws Exception {
        String response= userController.EditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
        dataController.EditPermissions(storeID, userName, storeOwner, storeManager,pType[0],pType[1],pType[2]);
        return response;
    }

    public String AssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
        while (!systemManagersLock.tryLock()) {
            Thread.sleep(1000);
        }
        try{
            String response= userController.AssignStoreManager(storeId, ownerUserName, username, pType);
            dataController.AssignStoreManager(storeId, username);
            return response;
        } finally{
            systemManagersLock.unlock();
        }
    }

    public String AssignStoreOwner(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
        while (!systemManagersLock.tryLock()) {
            Thread.sleep(1000);
        }
        try{
            String response=userController.AssignStoreOwner(storeId, ownerUserName, username, pType);
            dataController.AssignStoreOwner(storeId, username);
            return response;
        }finally{
            systemManagersLock.unlock();
        }
        
    }

    public String unassignUser(int storeID, String ownerUserName, String userName) throws Exception {
        while (!systemManagersLock.tryLock()) {
            Thread.sleep(1000);
        }
        try{
            String response= permissions.deletePermission(storeID, ownerUserName, userName);
            dataController.unassignUser(storeID, userName);
            return response;
        }finally{
            systemManagersLock.unlock();
        }
        
    }

    public String resign(int storeID, String username) throws Exception {
        String response= permissions.deleteStoreOwner(storeID, username);
        dataController.resign(storeID, username);
        return response;
    }

    public String suspendUser(String systemManager, String username) throws Exception {
        systemManagersLock.lock();
        try{
            if (systemManagers.contains(systemManager)) {
                String response= permissions.suspendUser(username);
                dataController.suspendUser(username);
                return response;
            } else {
                throw new Exception(systemManager + " not a system manager");
            }
        }finally{
            systemManagersLock.unlock();
        }
    }

    public String suspendUserSeconds(String systemManager, String username, int duration) throws Exception {
        systemManagersLock.lock();
        try{
            if (systemManagers.contains(systemManager)) {
                return permissions.suspendUserSeconds(username, duration);
            } else {
                throw new Exception(systemManager + " not a system manager");
            }
        }finally{
            systemManagersLock.unlock();
        }
    }

    public String resumeUser(String systemManager, String username) throws Exception {
        if (systemManagers.contains(systemManager)) {
            String response= permissions.resumeUser(username);
            dataController.resumeUser(username);
            return response;
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
            // I used datacontroller in permissions class
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
            dataController.addCategory(catagory);
            LOGGER.info("category added successfully");
            return "category added successfully";
        } else {
            LOGGER.severe(username + " is not system manager");
            throw new Exception(username + " is not system manager");
        }
    }

    // Product
    /*
     * new product in the system
     */
    public String initProduct(String username, String productName, int categoryId, String description, String brand,double weight)
            throws Exception {
        LOGGER.info("username: " + username + ",productName : " + productName + ", categoryId: " + categoryId
                + ", description: " + description + ", brand: " + brand);
        if (systemManagers.contains(username)) {
            Category category = categoryController.getCategory(categoryId);
            // I used datacontroller in productController class
            return productController.addProduct(productName, category, description, brand,weight);
        } else {
            LOGGER.severe(username + " is not system manager");
            throw new Exception(username + " is not system manager");
        }
    }

    // Store
    /*
     * new store in the system
     */
    public String initStore(String userName, String Description) throws Exception {
        LOGGER.info("userName: " + userName + ", Description: " + Description);
        if (userController.isRegistered(userName)) {
            int storeID = storeController.initStore(userName, Description);
            String resposnse = permissions.initStore(storeID, userName);
            dataController.initStore(userName,Description);
            return resposnse;
        } else {
            LOGGER.severe(userName + " is not registered");
            throw new Exception(userName + " is not registered");
        }
    }

    /*
     * adding a product to a certain store
     */
    public String addProduct(int productId, int storeId, double price, int quantity, String username,double weight) throws Exception {
        if (permissions.getPermission(storeId, username).getPType()[Permission.permissionType.editProducts.index]) {
            String response = storeController.addProduct(productId, storeId, price, quantity,weight);
            dataController.addProduct(storeId,productId,price,quantity);
            return response;
        } else {
            LOGGER.severe(username + " has no permission to add products");
            throw new Exception(username + " has no permission to add products");
        }
    }

    public String RemoveProduct(int productId, int storeId, String username) throws Exception {
        if (permissions.getPermission(storeId, username).getPType()[Permission.permissionType.editProducts.index]) {
            String response = storeController.removeProduct(productId, storeId);
            dataController.removeProduct(storeId, productId);
            return response;
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

    private PurchaseMethod initPurchaseMethod(Boolean immediate,int quantity, double price, LocalDate date, int atLeast, double weight, double age,String username,int storeId){
        if(immediate){
            return new ImmediatePurchase(quantity, price, date, atLeast, weight, age);
        }
        else{
            return new OfferMethod(quantity, price, date, atLeast, weight, age,storeId,username);
        }
    }

    public String addCategoryPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int categoryId,String username,int storeId,Boolean immediate) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        PurchaseMethod purchaseMethod=initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
        CategoryPurchase categoryPurchase=new CategoryPurchase(purchaseMethod, categoryId, -1);
        storeController.getStore(storeId).addPurchaseComposite(categoryPurchase);
        LOGGER.info("category purchase policy added");
        return "category purchase policy added";
    }

    public String addProductPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int productId,String username,int storeId,Boolean immediate) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        PurchaseMethod purchaseMethod=initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
        ProductPurchase productPurchase=new ProductPurchase(purchaseMethod, productId, storeId);
        storeController.getStore(storeId).addPurchaseComposite(productPurchase);
        LOGGER.info("product purchase policy added");
        return "product purchase policy added";
    }

    public String addShoppingCartPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,String username,int storeId,Boolean immediate) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        PurchaseMethod purchaseMethod=initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
        ShoppingCartPurchase ShoppingCartPurchase=new DomainLayer.backend.StorePackage.Purchase.ShoppingCartPurchase(purchaseMethod, -1);
        storeController.getStore(storeId).addPurchaseComposite(ShoppingCartPurchase);
        LOGGER.info("shopping cart purchase policy added");
        return "shopping cart purchase policy added";
    }

    public String addUserPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,double userAge,String username,int storeId,Boolean immediate) throws Exception{
        if(!Permissions.getInstance().getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        PurchaseMethod purchaseMethod=initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
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

    /*
     * returns store's invetory as a string
     */
    public String getInfo(int storeId, String username) throws Exception {
        if (userController.isRegistered(username)) {
            return storeController.getInfo(storeId);
        } else {
            // data controller used in getStore() in storeController
            LOGGER.severe(username + " has no permission to open the store");
            throw new Exception(username + " has no permission to open the store");
        }
    }

    // Purchase History
    public String viewSystemPurchaseHistory(String username) throws Exception {
        systemManagersLock.lock();
        try{
            if (!systemManagers.contains(username)) {
                LOGGER.severe("only system managers can view purchase history");
                throw new Exception("only system managers can view purchase history");
            }
            return PurchaseHistory.getInstance().viewPurchaseHistory();
        }finally{
            systemManagersLock.unlock();
        }
    }

    // public String addPurchase(int storeId, int userId, Purchase purchase) {
    // purchaseHistory.addPurchase(storeId, userId, purchase);
    // LOGGER.info("purcahse added");
    // return "purchase added";
    // }

    public Lock getSystemManagersLock(){
        return systemManagersLock;
    }

    public String getStorePurchaseHistory(int storeId) throws InterruptedException {
        List<Purchase> purchases = purchaseHistory.getStorePurchaseHistory(storeId);
        String info = "";
        for (Purchase purchase : purchases) {
            info += purchase.FetchInfo();
        }
        return info;
    }

    public String getUserPurchaseHistory(String userId) throws InterruptedException {
        List<Purchase> purchases = purchaseHistory.getUserPurchaseHistory(userId);
        String info = "";
        for (Purchase purchase : purchases) {
            info += purchase.FetchInfo();
        }
        return info;
    }

    public synchronized String removePurchaseFromStore(int storeId, int purchaseId) throws Exception {
        dataController.removePurchaseHistory(purchaseId);
        return purchaseHistory.removePurchaseFromStore(storeId, purchaseId);
    }

    public synchronized String removePurchaseFromUser(String userId, int purchaseId) throws Exception {
        dataController.removePurchaseHistory(purchaseId);
        return purchaseHistory.removePurchaseFromUser(userId, purchaseId);
    }


}
