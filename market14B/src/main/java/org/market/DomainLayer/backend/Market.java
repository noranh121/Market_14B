package org.market.DomainLayer.backend;

import org.market.DataAccessLayer.DataController;
import org.market.DomainLayer.SearchEngine;

import org.market.DomainLayer.SearchEngine;
import org.market.DomainLayer.backend.API.PaymentExternalService.PaymentService;
import org.market.DomainLayer.backend.API.SupplyExternalService.SupplyService;
import org.market.DomainLayer.backend.NotificationPackage.DelayedNotifierDecorator;
import org.market.DomainLayer.backend.NotificationPackage.ImmediateNotifierDecorator;
import org.market.DomainLayer.backend.ProductPackage.Category;
import org.market.DomainLayer.backend.ProductPackage.CategoryController;
import org.market.DomainLayer.backend.ProductPackage.Product;
import org.market.DomainLayer.backend.ProductPackage.ProductController;
import org.market.DomainLayer.backend.StorePackage.Discount.*;
import org.market.DomainLayer.backend.StorePackage.Discount.Logical.ANDDiscountRule;
import org.market.DomainLayer.backend.StorePackage.Discount.Logical.ORDiscountRule;
import org.market.DomainLayer.backend.StorePackage.Discount.Logical.XORDiscountRule;
import org.market.DomainLayer.backend.StorePackage.Discount.Numerical.ADDDiscountRule;
import org.market.DomainLayer.backend.StorePackage.Discount.Numerical.AT_MOSTDiscountRule;
import org.market.DomainLayer.backend.StorePackage.Purchase.*;
import org.market.DomainLayer.backend.StorePackage.Store;
import org.market.DomainLayer.backend.StorePackage.StoreController;
import org.market.DomainLayer.backend.UserPackage.ShoppingCart;
import org.market.DomainLayer.backend.UserPackage.User;
import org.market.DomainLayer.backend.UserPackage.UserController;
import org.market.ServiceLayer.Response;
import org.market.ServiceLayer.SuspendedException;
import org.market.Web.DTOS.CartItemDTO;
import org.market.Web.DTOS.OfferDTO;
import org.market.Web.DTOS.PermissionDTO;
import org.market.Web.DTOS.ProductDTO;
import org.market.Web.Requests.SearchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Component
public class Market {
    public static final Logger LOGGER = Logger.getLogger(Market.class.getName());

    // List<Users> SystemManagers;
    // // boolean ON/OFF
    // private UserController userController; // = UserController.getInstance();
    // private StoreController storeController; // = StoreController.getInstance();
    // private Permissions permissions; // = Permissions.getInstance();
    // private PurchaseHistory purchaseHistory; // = PurchaseHistory.getInstance();
    // private ProductController productController; // = ProductController.getInstance();
    // private CategoryController categoryController; // = CategoryController.getinstance();
    // private PaymentService paymentService; // =PaymentService.getInstance();
    // private SupplyService supplyService; //=SupplyService.getInstance();


    private static DataController dataController;
    private static StoreController storeController;
    private static  UserController userController;
    private static Permissions permissions;
    private static PurchaseHistory purchaseHistory;
    private static ProductController productController;
    private static CategoryController categoryController;
    private static PaymentService paymentService;
    private static SupplyService supplyService;
    private static ImmediateNotifierDecorator immediateNotifierDecorator;
    private static DelayedNotifierDecorator delayedNotifierDecorator;
    private static SearchEngine searchEngine;
    private FileHandler fileHandler;

    private static Boolean Online = false;
    private static List<String> systemManagers = Collections.synchronizedList(new ArrayList<>());
    private static final Lock systemManagersLock = new ReentrantLock();


    public static ImmediateNotifierDecorator getIND(){
        return immediateNotifierDecorator;
    }
    public static DelayedNotifierDecorator getDND(){
        return delayedNotifierDecorator;
    }
    public static StoreController getSC() {
        return storeController;
    }

    public static UserController getUC() {
        return userController;
    }

    public static Permissions getP() {
        return permissions;
    }

    public static PurchaseHistory getPH() {
        return purchaseHistory;
    }

    public static ProductController getPC() {
        return productController;
    }

    public static CategoryController getCC() {
        return categoryController;
    }

    public static PaymentService getPS() {
        return paymentService;
    }

    public static SupplyService getSS() {
        return supplyService;
    }
    public static DataController getDC(){
        return dataController;
    }

    //this is for tesing
    public static void clear(){
        systemManagers.clear();
        Online=false;
        storeController.clear();
        userController.clear();
        permissions.clear();
        purchaseHistory.clear();
        productController.clear();
        categoryController.clear();
    }

    @Autowired
    public void setDependencies(DataController dataController,StoreController storeController,UserController userController,Permissions permissions,PurchaseHistory purchaseHistory
            ,ProductController productController,CategoryController categoryController,PaymentService paymentService,SupplyService supplyService, ImmediateNotifierDecorator immediateNotifierDecorator,
             DelayedNotifierDecorator delayedNotifierDecorator,SearchEngine searchEngine){
        this.userController = userController;
        this.storeController = storeController;
        this.permissions = permissions;
        this.purchaseHistory = purchaseHistory;
        this.productController = productController;
        this.categoryController = categoryController;
        this.paymentService = paymentService;
        this.supplyService = supplyService;
        this.immediateNotifierDecorator = immediateNotifierDecorator;
        this.delayedNotifierDecorator = delayedNotifierDecorator;
        this.dataController = dataController;
        this.searchEngine = searchEngine;
        try {
            systemManagers=dataController.getSystemManagers(0);
            Online=dataController.getOnline();
            fileHandler= new FileHandler("Market.log",true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set up logger handler.", e);
        }
    }

    // private Market() {
    //     try {
    //         systemManagers=dataController.getSystemManagers();
    //         Online=dataController.getOnline();
    //         fileHandler= new FileHandler("Market.log",true);
    //         fileHandler.setFormatter(new SimpleFormatter());
    //         LOGGER.addHandler(fileHandler);
    //         LOGGER.setLevel(Level.ALL);
    //     } catch (Exception e) {
    //         LOGGER.log(Level.SEVERE, "Failed to set up logger handler.", e);
    //     }
    // }

    public String setMarketOnline(String username) throws Exception {
        if (!systemManagers.contains(username)) {
            LOGGER.severe("only system managers can change market's activity");
            throw new Exception("only system managers can change market's activity");
        }
        LOGGER.info("market is Online");
        Online = true;
        dataController.setMarketOnline();
        return "market is Online";
    }

    public String setMarketOFFLINE(String username) throws Exception {
        if (!systemManagers.contains(username)) {
            LOGGER.severe("only system managers can change market's activity");
            throw new Exception("only system managers can change market's activity");
        }
        LOGGER.info("market is OFFLINE");
        Online = false;
        dataController.setMarketOFFLINE();
        return "market is OFFLINE";
    }

    public List<String> getSystemManagers() {
        return systemManagers;
    }

    public static void addToSystemManagers(String admin) {
        systemManagers.add(admin);
        dataController.addSystemManager(admin);
    }

    public boolean getOnline() {
        return Online;
    }

    // this is for testing
//    public void setToNull() {
//        instance = null;
//        permissions.setToNull();
//        userController.setToNull();
//        storeController.setToNull();
//        systemManagers = Collections.synchronizedList(new ArrayList<>());
//    }

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
            if (systemManagers.contains(username)) {
                String response = userController.Login(guest, username, password);
                setMarketOnline(username);
                return response;
            }
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
        return response;
    }

    public double Buy(String username,String currency,String card_number,String month,String year,String ccv,
                      String address, String city, String country, String zip,String id) throws Exception {
        LOGGER.info("username: "+username+" currency: "+currency+" card_number: "+card_number+" month: "+month+" year: "+year+" ccv: "+ccv+" address: "+address+
                " city: "+city+" country: "+country+" zip: "+zip);
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't buy user is suspended");
        }
        double total=userController.Buy(username);
        // paymentServiceProccess(username, currency, card_number, month, year, ccv, String.valueOf(total),id);
        // supplyServiceProccess(address,city,country,zip,username);
        userController.getUser(username).cleanShoppingCart();
        dataController.cleanShoppingCart(username);
        return total;
    }

    private void supplyServiceProccess(String address, String city, String country, String zip,String username) throws Exception {
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

    private void paymentServiceProccess(String username,String currency,String card_number,String month,String year,String ccv,String amount,String id) throws Exception{
        Response<Boolean> handshake=paymentService.handshake();
        if(!handshake.isError()){
            Response<Integer> transaction_id=paymentService.pay(amount, currency, card_number, month, year, card_number, ccv,id);
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
            throw new SuspendedException("can't add to cart user is suspended");
        }
        String response= userController.addToCart(username, product, storeId, quantity);
        dataController.addToCart(username, storeId, product, quantity);
        return response;
    }

    public String inspectCart(String username) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't inspect cart user is suspended");
        }
        String response= userController.inspectCart(username);
        // I used DataController in ShoppingCart class
        return response;
    }

    public String removeCartItem(String username, int storeId, int product) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't remove cart item user is suspended");
        }
        String response= userController.removeCartItem(username, storeId, product);
        dataController.removeCartItem(username, storeId, product);
        return response;
    }

    // Permissions
    public String EditPermissions(int storeID, String ownerUserName, String userName, Boolean storeOwner,
                                  Boolean storeManager, Boolean[] pType) throws Exception {
        if (permissions.isSuspended(ownerUserName)) {
            throw new SuspendedException("can't edit permissions, user is suspended");
        }
        String response= userController.EditPermissions(storeID, ownerUserName, userName, storeOwner, storeManager, pType);
        dataController.EditPermissions(storeID, userName, storeOwner, storeManager,pType[0],pType[1],pType[2]);
        return response;
    }

    public String AssignStoreManager(int storeId, String ownerUserName, String username, Boolean[] pType) throws Exception {
        while (!systemManagersLock.tryLock()) {
            Thread.sleep(1000);
        }
        try{
            if (permissions.isSuspended(ownerUserName)) {
                throw new SuspendedException("can't assign store manager, user is suspended");
            }
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
            if (permissions.isSuspended(ownerUserName)) {
                throw new SuspendedException("can't assign store owner, user is suspended");
            }
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
            if (permissions.isSuspended(ownerUserName)) {
                throw new SuspendedException("can't unassign permissions, user is suspended");
            }
            String response= permissions.deletePermission(storeID, ownerUserName, userName);
            dataController.unassignUser(storeID, userName);
            return response;
        }finally{
            systemManagersLock.unlock();
        }

    }

    public String resign(int storeID, String username) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't resign, user is suspended");
        }
        String response= permissions.deleteStoreOwner(storeID, username);
        dataController.resign(storeID, username);
        return response;
    }

    public String suspendUser(String systemManager, String username) throws Exception {
        systemManagersLock.lock();
        try{
            if (systemManagers.contains(systemManager)) {
                if(userController.getUser(username) != null) {
                    String response = permissions.suspendUser(username);
                dataController.suspendUser(username);
                    return response;
                }
                else{
                    throw new Exception(username + " not found");
                }
            } else {
                throw new Exception(systemManager + " not a system manager");
            }
        }finally{
            systemManagersLock.unlock();
        }
    }

    public String suspendUserSeconds(String systemManager, String username, int duration) throws Exception {
        systemManagersLock.lock();
        try {
            if (systemManagers.contains(systemManager)) {
                if (userController.getUser(username) != null) {
                    return permissions.suspendUserSeconds(username, duration);
                } else {
                    throw new Exception(username + " not found");
                }
            } else{
                throw new Exception(systemManager + " not a system manager");
            }
        }finally{
            systemManagersLock.unlock();
        }
    }

    public String resumeUser(String systemManager, String username) throws Exception {
        if (systemManagers.contains(systemManager)) {
            if(userController.getUser(username) != null) {
                String response = permissions.resumeUser(username);
            dataController.resumeUser(username);
                return response;
            }
            else{
                throw new Exception(username + " not found");
            }
        } else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    public String resumeUserSeconds(String systemManager, String username, int duration) throws Exception {
        if (systemManagers.contains(systemManager)) {
            if(userController.getUser(username) != null) {
                return permissions.resumeTemporarily(username, duration);
            }
            else{
                throw new Exception(username + " not found");
            }
        } else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    public String viewSuspended(String systemManager) throws Exception {
        if (systemManagers.contains(systemManager)) {
            // I used DataController in permissions class
            return permissions.viewSuspended();
        } else {
            throw new Exception(systemManager + " not a system manager");
        }
    }

    // Category
    public String addCatagory(int storeId, String catagory, String username) throws Exception {
        LOGGER.info("storeId: " + storeId + ", category: " + catagory + ", username: " + username);
        if (systemManagers.contains(username)) {
            int categoryId = categoryController.addCategory(catagory);
            dataController.addCategory(catagory,categoryId);
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
    public int initProduct(String username, String productName, int categoryId, String description, String brand,double weight) throws Exception {
        LOGGER.info("username: " + username + ",productName : " + productName + ", categoryId: " + categoryId
                + ", description: " + description + ", brand: " + brand);
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't add product, user is suspended");
        }
        if (systemManagers.contains(username)) {
            Category category = categoryController.getCategory(categoryId);
            if(category == null){
                int catId = categoryController.addCategory("None");
                category = categoryController.getCategory(catId);
            }
            // I used datacontroller in productController class
            return productController.addProduct(productName, category, description, brand,weight);
        }
        LOGGER.severe(username + " is not system manager");
        throw new Exception(username + " is not system manager");
        
    }

    // Store
    /*
     * new store in the system
     */
    public String initStore(String userName, String name, String Description) throws Exception {
        LOGGER.info("userName: " + userName + ", Description: " + Description);
        if (permissions.isSuspended(userName)) {
            throw new SuspendedException("can't add store, user is suspended");
        }
        if (userController.isRegistered(userName)) {
            int storeID = storeController.initStore(userName, name, Description);
            String resposnse = permissions.initStore(storeID, userName);
            dataController.initStore(userName,Description,storeID);
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
            if (permissions.isSuspended(username)) {
                throw new SuspendedException("can't add product, user is suspended");
            }
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
            if (permissions.isSuspended(username)) {
                throw new SuspendedException("can't remove product, user is suspended");
            }
            String response = storeController.removeProduct(productId, storeId);
            String res = productController.removeProduct(productId);
            dataController.removeProduct(storeId, productId);
            return response;
        } else {
            LOGGER.severe(username + " has no permission to edit products");
            throw new Exception(username + " has no permission to edit products");
        }
    }

    public String addCategoryDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int categoryId,int storeId,String username,int id) throws Exception{
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't add discount policy, user is suspended");
        }
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        Discount discountType=initDiscount(standard, conditionalPrice, conditionalQuantity);
        CategoryDiscount categoryDiscount=new CategoryDiscount(discountType, discountPercentage, categoryId, -1);
        int selfid=storeController.getStore(storeId).addDiscountComposite(categoryDiscount,id);
        dataController.addDiscountPolicy(standard,conditionalPrice,conditionalQuantity,discountPercentage,categoryId,-1,storeId,username,id,"category",selfid);
        LOGGER.info("category discount policy added");
        return "category discount policy added";
    }

    public static void loudCategoryDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int categoryId,int storeId,String username,int id) throws Exception{
        Discount discountType=initDiscount(standard, conditionalPrice, conditionalQuantity);
        CategoryDiscount categoryDiscount=new CategoryDiscount(discountType, discountPercentage, categoryId, -1);
        storeController.getStore(storeId).addDiscountComposite(categoryDiscount,id);
    }

    public String addProductDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int productId,int storeId,String username,int id) throws Exception{
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't add discount policy, user is suspended");
        }
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        Discount discountType=initDiscount(standard, conditionalPrice, conditionalQuantity);
        ProductDiscount productDiscount=new ProductDiscount(discountType, discountPercentage,productId,-1);
        int selfid=storeController.getStore(storeId).addDiscountComposite(productDiscount,id);
        dataController.addDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, -1,id, storeId, username, id,"product",selfid);
        LOGGER.info("product discount policy added");
        return "product discount policy added";
    }

    public static void loudProductDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int productId,int storeId,String username,int id) throws Exception{
        Discount discountType=initDiscount(standard, conditionalPrice, conditionalQuantity);
        ProductDiscount productDiscount=new ProductDiscount(discountType, discountPercentage,productId,-1);
        storeController.getStore(storeId).addDiscountComposite(productDiscount,id);
    }

    public String addStoreDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int storeId,String username,int id) throws Exception{
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't add discount policy, user is suspended");
        }
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        if(storeController.getStore(storeId) == null){
            throw new Exception("Store does not exist");
        }
        Discount discountType=initDiscount(standard, conditionalPrice, conditionalQuantity);
        StoreDiscount storeDiscount=new StoreDiscount(discountType, discountPercentage,-1);
        int selfid=storeController.getStore(storeId).addDiscountComposite(storeDiscount,id);
        dataController.addDiscountPolicy(standard, conditionalPrice, conditionalQuantity, discountPercentage, -1, -1, storeId, username, id, "store",selfid);
        LOGGER.info("store discount policy added");
        return "store discount policy added";
    }

    public static void loudStoreDiscountPolicy(Boolean standard,double conditionalPrice,double conditionalQuantity,double discountPercentage,int productId,int storeId,String username,int id) throws Exception{
        Discount discountType=initDiscount(standard, conditionalPrice, conditionalQuantity);
        StoreDiscount storeDiscount=new StoreDiscount(discountType, discountPercentage,-1);
        storeController.getStore(storeId).addDiscountComposite(storeDiscount,id);
    }

    public static Discount initDiscount(Boolean standard,double conditionalPrice,double conditionalQuantity){
        if(standard){
            return new StandardDiscount();
        }
        else{
            return new ConditionalDiscount(conditionalPrice, conditionalQuantity);
        }
    }

    public String addNmericalDiscount(String username,int storeId,Boolean ADD,int id) throws Exception{
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't add numerical discount, user is suspended");
        }
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        if(ADD){
            ADDDiscountRule addDiscountRule=new ADDDiscountRule(-1);
            int selfid=storeController.getStore(storeId).addDiscountComposite(addDiscountRule,id);
            dataController.addDiscountPolicy(false, -1, -1, -1, -1, -1, storeId, username, id, "add",selfid);
            LOGGER.info("ADD discount policy added");
            return "ADD discount policy added";
        }
        else{
            AT_MOSTDiscountRule at_MOSTDiscountRule=new AT_MOSTDiscountRule(-1);
            int selfid=storeController.getStore(storeId).addDiscountComposite(at_MOSTDiscountRule,id);
            dataController.addDiscountPolicy(false, -1, -1, -1, -1, -1, storeId, username, id, "atmost",selfid);
            LOGGER.info("AT_MOST discount policy added");
            return "AT_MOST discount policy added";
        }
    }

    public static void loudNmericalDiscount(String username,int storeId,Boolean ADD,int id) throws Exception{
        if(ADD){
            ADDDiscountRule addDiscountRule=new ADDDiscountRule(-1);
            storeController.getStore(storeId).addDiscountComposite(addDiscountRule,id);
        }
        else{
            AT_MOSTDiscountRule at_MOSTDiscountRule=new AT_MOSTDiscountRule(-1);
            storeController.getStore(storeId).addDiscountComposite(at_MOSTDiscountRule,id);
        }
    }

    public String addLogicalDiscount(String username, int storeId, String logicalRule,int id) throws Exception{
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't add logical discount, user is suspended");
        }
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        DiscountPolicyController.LogicalRule logicalRuleENUM=DiscountPolicyController.LogicalRule.AND;
        switch (logicalRule) {
            case "And":
                logicalRuleENUM=DiscountPolicyController.LogicalRule.AND;
                break;
            case "Xor":
                logicalRuleENUM=DiscountPolicyController.LogicalRule.XOR;
                break;

            case "Or":
                logicalRuleENUM=DiscountPolicyController.LogicalRule.OR;
                break;
        
            default:
                break;
        }
        switch (logicalRuleENUM) {
            case AND:
                ANDDiscountRule andDiscountRule=new ANDDiscountRule(-1);
                int selfid1=storeController.getStore(storeId).addDiscountComposite(andDiscountRule,id);
                dataController.addDiscountPolicy(false, -1, -1, -1, -1, -1, storeId, username, id, "and",selfid1);
                LOGGER.info("AND discount policy added");
                return "AND discount policy added";
            case OR:
                ORDiscountRule orDiscountRule=new ORDiscountRule(-1);
                int selfid2=storeController.getStore(storeId).addDiscountComposite(orDiscountRule,id);
                dataController.addDiscountPolicy(false, -1, -1, -1, -1, -1, storeId, username, id, "or",selfid2);
                LOGGER.info("OR discount policy added");
                return "OR discount policy added";
            case XOR:
                XORDiscountRule xorDiscountRule=new XORDiscountRule(-1);
                int selfid3=storeController.getStore(storeId).addDiscountComposite(xorDiscountRule,id);
                dataController.addDiscountPolicy(false, -1, -1, -1, -1, -1, storeId, username, id, "xor",selfid3);
                LOGGER.info("XOR discount policy added");
                return "XOR discount policy added";
            default:
                LOGGER.severe("invaled logical rule");
                throw new Exception("invaled logical rule");
        }
    }

    public static void loudLogicalDiscount(String username, int storeId, String logicalRule,int id) throws Exception{
        DiscountPolicyController.LogicalRule logicalRuleENUM=DiscountPolicyController.LogicalRule.AND;
        switch (logicalRule) {
            case "And":
                logicalRuleENUM=DiscountPolicyController.LogicalRule.AND;
                break;
            case "Xor":
                logicalRuleENUM=DiscountPolicyController.LogicalRule.XOR;
                break;

            case "Or":
                logicalRuleENUM=DiscountPolicyController.LogicalRule.OR;
                break;
        
            default:
                break;
        }
        switch (logicalRuleENUM) {
            case AND:
                ANDDiscountRule andDiscountRule=new ANDDiscountRule(-1);
                storeController.getStore(storeId).addDiscountComposite(andDiscountRule,id);
                break;
            case OR:
                ORDiscountRule orDiscountRule=new ORDiscountRule(-1);
                storeController.getStore(storeId).addDiscountComposite(orDiscountRule,id);
                break;
            case XOR:
                XORDiscountRule xorDiscountRule=new XORDiscountRule(-1);
                storeController.getStore(storeId).addDiscountComposite(xorDiscountRule,id);
                break;
            default:
                break;
        }
    }

    public static PurchaseMethod initPurchaseMethod(Boolean immediate, int quantity, double price, LocalDate date, int atLeast, double weight, double age, String username, int storeId){
        if(immediate){
            return new ImmediatePurchase(quantity, price, date, atLeast, weight, age);
        }
        else{
            return new OfferMethod(quantity, price, date, atLeast, weight, age,storeId,username);
        }
    }

    public String addCategoryPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int categoryId,String username,int storeId,Boolean immediate,int id) throws Exception{
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't add purchase policy, user is suspended");
        }
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        PurchaseMethod purchaseMethod=initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
        CategoryPurchase categoryPurchase=new CategoryPurchase(purchaseMethod, categoryId, -1);
        int selfid=storeController.getStore(storeId).addPurchaseComposite(categoryPurchase,id);
        dataController.addPurchasePolicy(quantity,price,date,atLeast,weight,age,-1,categoryId,-1,username,storeId,immediate,id,"category",selfid);
        LOGGER.info("category purchase policy added");
        return "category purchase policy added";
    }

    public static void loudCategoryPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int categoryId,String username,int storeId,Boolean immediate,int id) throws Exception{
        PurchaseMethod purchaseMethod=initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
        CategoryPurchase categoryPurchase=new CategoryPurchase(purchaseMethod, categoryId, -1);
        storeController.getStore(storeId).addPurchaseComposite(categoryPurchase,id);
    }

    public String addProductPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int productId,String username,int storeId,Boolean immediate,int id) throws Exception{
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't add purchase policy, user is suspended");
        }
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        PurchaseMethod purchaseMethod=initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
        ProductPurchase productPurchase=new ProductPurchase(purchaseMethod, productId, storeId);
        int selfid=storeController.getStore(storeId).addPurchaseComposite(productPurchase,id);
        dataController.addPurchasePolicy(quantity, price, date, atLeast, weight, age,-1, -1,productId, username, storeId, immediate, id,"product",selfid);
        LOGGER.info("product purchase policy added");
        return "product purchase policy added";
    }

    public static void loudProductPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int productId,String username,int storeId,Boolean immediate,int id) throws Exception{
        PurchaseMethod purchaseMethod=initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
        ProductPurchase productPurchase=new ProductPurchase(purchaseMethod, productId, storeId);
        storeController.getStore(storeId).addPurchaseComposite(productPurchase,id);
    }

    public String addShoppingCartPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,String username,int storeId,Boolean immediate,int id) throws Exception{
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't add purchase policy, user is suspended");
        }
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        PurchaseMethod purchaseMethod=initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
        ShoppingCartPurchase ShoppingCartPurchase=new ShoppingCartPurchase(purchaseMethod, -1);
        int selfid=storeController.getStore(storeId).addPurchaseComposite(ShoppingCartPurchase,id);
        dataController.addPurchasePolicy(quantity, price, date, atLeast, weight, age,-1, -1, -1, username, storeId, immediate, id,"shoppingcart",selfid);
        LOGGER.info("shopping cart purchase policy added");
        return "shopping cart purchase policy added";
    }

    public static void loudShoppingCartPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,int productId,String username,int storeId,Boolean immediate,int id) throws Exception{
        PurchaseMethod purchaseMethod=initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
        ShoppingCartPurchase ShoppingCartPurchase=new ShoppingCartPurchase(purchaseMethod, -1);
        storeController.getStore(storeId).addPurchaseComposite(ShoppingCartPurchase,id);
    }

    public String addUserPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,double userAge,String username,int storeId,Boolean immediate,int id) throws Exception{
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't add purchase policy, user is suspended");
        }
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        PurchaseMethod purchaseMethod=initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
        UserPurchase userPurchase=new UserPurchase(purchaseMethod, userAge, storeId);
        int selfid=storeController.getStore(storeId).addPurchaseComposite(userPurchase,id);
        dataController.addPurchasePolicy(quantity, price, date, atLeast, weight, age,userAge, -1, -1, username, storeId, immediate, id,"user",selfid);
        LOGGER.info("user purchase policy added");
        return "user purchase policy added";
    }

    public static void loudUserPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,double userAge,String username,int storeId,Boolean immediate,int id) throws Exception{
        PurchaseMethod purchaseMethod=initPurchaseMethod(immediate, quantity, price, date, atLeast, weight, age, username, storeId);
        UserPurchase userPurchase=new UserPurchase(purchaseMethod, userAge, storeId);
        storeController.getStore(storeId).addPurchaseComposite(userPurchase,id);
    }

    public String addLogicalPurchase(String username, int storeId, String logicalRule,int id) throws Exception{
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't add logical purchase, user is suspended");
        }
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        DiscountPolicyController.LogicalRule logicalRuleENUM=DiscountPolicyController.LogicalRule.AND;
        switch (logicalRule) {
            case "And":
                logicalRuleENUM=DiscountPolicyController.LogicalRule.AND;
                break;
            case "Or":
            logicalRuleENUM=DiscountPolicyController.LogicalRule.OR;
            break;

            case "If-Then":
            logicalRuleENUM=DiscountPolicyController.LogicalRule.IF_THEN;
            break;
        
            default:
                break;
        }
        switch (logicalRuleENUM) {
            case AND:
                ANDPurchaseRule andPurchaseRule=new ANDPurchaseRule(-1);
                int selfid1=storeController.getStore(storeId).addPurchaseComposite(andPurchaseRule,id);
                dataController.addPurchasePolicy(0, 0, null, 0, 0, 0, 0,0, 0, username, storeId, false, id, logicalRule,selfid1);
                LOGGER.info("AND purchase policy added");
                return "AND purchase policy added";
            case OR:
                ORPurchaseRule orPurchaseRule=new ORPurchaseRule(-1);
                int selfid2=storeController.getStore(storeId).addPurchaseComposite(orPurchaseRule,id);
                dataController.addPurchasePolicy(0, 0, null, 0, 0, 0, 0,0, 0, username, storeId, false, id, logicalRule,selfid2);
                LOGGER.info("OR purchase policy added");
                return "OR purchase policy added";
            case IF_THEN:
                IF_THENPurchaseRule if_THENDiscountRule=new IF_THENPurchaseRule(-1);
                int selfid3=storeController.getStore(storeId).addPurchaseComposite(if_THENDiscountRule,id);
                dataController.addPurchasePolicy(0, 0, null, 0, 0, 0, 0,0, 0, username, storeId, false, id, logicalRule,selfid3);
                LOGGER.info("IF_THEN discount policy added");
                return "IF_THEN discount policy added";
            default:
                LOGGER.severe("invaled logical rule");
                throw new Exception("invaled logical rule");
        }
    }

    public static void loudLogicalPurchase(String username, int storeId, String logicalRule,int id) throws Exception{
        DiscountPolicyController.LogicalRule logicalRuleENUM=DiscountPolicyController.LogicalRule.AND;
        switch (logicalRule) {
            case "And":
                logicalRuleENUM=DiscountPolicyController.LogicalRule.AND;
                break;
            case "Or":
            logicalRuleENUM=DiscountPolicyController.LogicalRule.OR;
            break;

            case "If-Then":
            logicalRuleENUM=DiscountPolicyController.LogicalRule.IF_THEN;
            break;
        
            default:
                break;
        }
        switch (logicalRuleENUM) {
            case AND:
                ANDPurchaseRule andPurchaseRule=new ANDPurchaseRule(-1);
                storeController.getStore(storeId).addPurchaseComposite(andPurchaseRule,id);
                break;
            case OR:
                ORPurchaseRule orPurchaseRule=new ORPurchaseRule(-1);
                storeController.getStore(storeId).addPurchaseComposite(orPurchaseRule,id);
                break;
            case IF_THEN:
                IF_THENPurchaseRule if_THENDiscountRule=new IF_THENPurchaseRule(-1);
                storeController.getStore(storeId).addPurchaseComposite(if_THENDiscountRule,id);
                break;
            default:
                break;
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
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't edit product price, user is suspended");
        }
        if (permissions.getPermission(storeId, username).getPType()[Permission.permissionType.editProducts.index]) {
            return storeController.EditProducPrice(productId, storeId, newPrice);
        } else {
            LOGGER.severe(username + " has no permission to edit products");
            throw new Exception(username + " has no permission to edit products");
        }
    }

    public String EditProductQuantity(int productId, int storeId, int newQuantity, String username) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't edit product inventory, user is suspended");
        }
        if (permissions.getPermission(storeId, username).getPType()[Permission.permissionType.editProducts.index]) {
            return storeController.EditProductQuantity(productId, storeId, newQuantity);
        } else {
            LOGGER.severe(username + " has no permission to edit products");
            throw new Exception(username + " has no permission to edit products");
        }
    }

    public String CloseStore(int storeId, String username) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't close store, user is suspended");
        }
        if (permissions.getPermission(storeId, username).getStoreOwner()) {
            permissions.updateStoreOwners(storeId, storeId + " store was closed");
            return storeController.closeStore(storeId);
        } else {
            LOGGER.severe(username + " has no permission to close the store");
            throw new Exception(username + " has no permission to close the store");
        }
    }

    public String OpenStore(int storeId, String username) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't open store, user is suspended");
        }
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
            return purchaseHistory.viewPurchaseHistory();
        }finally{
            systemManagersLock.unlock();
        }
    }

//     public String addPurchase(int storeId, int userId, Purchase purchase) {
//     purchaseHistory.addPurchase(storeId, userId, purchase);
//     LOGGER.info("purcahse added");
//     return "purchase added";
//     }

    public static Lock getSystemManagersLock(){
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

    public List<Store> getAllStores(){
        return storeController.getAllStores();
    }

    public List<Product> getAllProducts() {
        List<Product> prods = new ArrayList<>(productController.getProducts());
        return prods;
    }

    public static double[] findProdInfo(Product p) {
        int prodid = p.getId();
        return storeController.getProdInfo(prodid);
    }
    // public static double[] findProdInfo(Product p, int storeID) {
    //     int prodid = p.getId();
    //     return storeController.getProdInfo(prodid,storeID);
    // }

    public List<Store> getUserStores(String username) throws Exception {
        List<Store> stores = permissions.getUserStores(username);
        return stores;
    }

    public List<ProductDTO> getStoreProducts(int store_id) {
        List<ProductDTO> prods = storeController.getStoreProducts(store_id);
        return prods;
    }

    public Store getStore(int store_id) {
        return storeController.getStore(store_id);
    }

    public Product getProduct(int product_id) {
        return productController.getProductbyID(product_id);
    }

    public List<PermissionDTO> getPermissions(String username) throws Exception {
        List<PermissionDTO> pdtos = new ArrayList<>();
        for(Map.Entry<Integer, Permission> entry: permissions.getUserPermissions(username).entrySet()){
            PermissionDTO pdto = new PermissionDTO(entry.getValue(), entry.getKey());
            pdtos.add(pdto);
        }
        return pdtos;
    }

    public boolean isSystemManager(String username) {
        List<String> managers = getSystemManagers();
        return managers.stream().anyMatch(m -> m.equals(username));
    }

    public List<String> getPurchaseHistoryUser(String username) throws Exception {
        List<String> result = new ArrayList<>();
        if(userController.getUser(username) != null) {
            List<Purchase> purchases = purchaseHistory.getUserPurchaseHistory(username);
            for(Purchase p : purchases) {
                result.add(p.FetchInfo());
            }
            return result;
        }else{
            throw new Exception("User is not registered.");
        }
    }

    public List<String> getPurchaseHistoryStore(int storeId) throws Exception {
        List<String> result = new ArrayList<>();
        if(storeController.getStore(storeId) != null) {
            List<Purchase> purchases = purchaseHistory.getStorePurchaseHistory(storeId);
            for(Purchase p : purchases) {
                result.add(p.FetchInfo());
            }
            return result;
        }else{
            throw new Exception("Store does not exist.");
        }
    }

    public String removePurchaseStore(Integer storeId, Integer purchaseId) throws Exception {
        if(storeController.getStore(storeId) != null) {
            return purchaseHistory.removePurchaseFromStore(storeId, purchaseId);
        }else{
            throw new Exception("Store does not exist.");
        }
    }

    public String removePurchaseUser(String username, Integer purchaseId) throws Exception {
        if(userController.getUser(username) != null) {
            return purchaseHistory.removePurchaseFromUser(username, purchaseId);
        }else{
            throw new Exception("User is not registered.");
        }
    }

    public Integer getCategory(String name) {
        Category category = categoryController.getCategorybyName(name);
        if(category == null){
            return categoryController.addCategory(name);
        }
        else{
            return category.getId();
        }
    }

    public boolean hasCategory(String name){
        return categoryController.getCategorybyName(name) != null;
    }

    public List<CartItemDTO> getCart(String username) throws Exception {
        if (permissions.isSuspended(username)) {
            throw new SuspendedException("can't retrieve cart is suspended");
        }
        List<CartItemDTO> result = new ArrayList<>();
        User user = userController.getUser(username);
        if (user != null) {
            ShoppingCart cart = user.getShoppingCart();
            List<Basket> baskets = cart.getBaskets();
            for (Basket b : baskets) {
                for (Map.Entry<Integer, Integer> entry : b.getProducts().entrySet()) {
                    CartItemDTO item = new CartItemDTO();
                    item.setProductId(entry.getKey());
                    item.setQuantity(entry.getValue());
                    item.setUsername(username);
                    item.setStoreId(b.getStoreID());
                    item.setName(productController.getProductName(entry.getKey()));
                    double[] price_store = findProdInfo(getProduct(entry.getKey()));
                    if (price_store[0] == -1) {
                        throw new Exception("Price is Out Of Stock");
                    }
                    item.setPrice(price_store[0]);
                    result.add(item);
                }
            }
            return result;
        } else {
            throw new Exception("User does not exist.");
        }
    }

    public String approveOffer(String username,String offerName, int storeId, int productId) throws Exception {
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        int i = storeController.approveOffer(permissions.numOfStoreOwners(storeId),offerName, storeId, productId);
        String message = "";
        switch (i) {
            case 1:
                message = "your offer for " + productId +" was accepted!";
                break;
            case -1:
                message = "your offer for " + productId +" was rejected";
                break;
            default:
                break;
        }
        permissions.updateUser(offerName, message);
        return "approval sent";
    }

    public String rejectOffer(String username,String offerName, int storeId, int productId) throws Exception {
        if(!permissions.getPermission(storeId, username).getStoreOwner()){
            LOGGER.severe(username + " is not store owner");
            throw new Exception(username + " is not store owner");
        }
        int i = storeController.rejectOffer(permissions.numOfStoreOwners(storeId),offerName, storeId, productId);
        String message = "";
        switch (i) {
            case 1:
                message = "your offer for " + productId +" was accepted!";
                break;
            case -1:
                message = "your offer for " + productId +" was rejected";
                break;
            default:
                break;
        }
        permissions.updateUser(offerName, message); 
        return "rejection sent";
    }

    public String sendOffer(String username, int storeId, int productId, Double price, Double offerPrice) {
        String s = storeController.sendOffer(productId,  productController.getProductName(productId) ,username, price , offerPrice,storeId);
        permissions.updateStoreOwners(storeId,"a new offer was sent");
        return s;
    }

    public List<OfferDTO> getOffers(int storeId, String username) {
        List<OfferDTO> offers = storeController.getStoreOffers(storeId);
        return offers;
    }

    public static double[] findProdInfo(Product p, int storeID) {
        int prodid = p.getId();
        return storeController.getProdInfo(prodid,storeID);
    }

    public List<ProductDTO> search(SearchEntity entity){
        return this.searchEngine.HandleSearch(entity);
    }

    public static List<ProductDTO> convertProds(List<Product> prods){
        List<ProductDTO> psdto = new ArrayList<>();
        for(Product p : prods){
            double [] price_store = findProdInfo(p);
            if(price_store[0] != -1){
                if(storeController.getStore(((int)price_store[1])).isActive()) {
                    ProductDTO pdto = new ProductDTO(p, price_store[0], (int) price_store[1]);
                    psdto.add(pdto);
                }
            }
        }
        return psdto;
    }
    public static List<ProductDTO> convertProds(List<Product> result, int storeID) {
        List<ProductDTO> psdto = new ArrayList<>();
        for(Product p : result){
            double [] price_store = findProdInfo(p,storeID);
            if(price_store[0] != -1){
                if(storeController.getStore(((int)price_store[1])).isActive()){
                    ProductDTO pdto = new ProductDTO(p, price_store[0], (int) price_store[1]);
                    psdto.add(pdto);
                }
            }
        }
        return psdto;
    }
}
