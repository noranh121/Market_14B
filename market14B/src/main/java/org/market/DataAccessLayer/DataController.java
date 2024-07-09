package org.market.DataAccessLayer;

import org.market.DataAccessLayer.Entity.*;
import org.market.DataAccessLayer.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.springframework.data.jpa.repository.JpaRepository;

@Service
@Transactional
public class DataController {

    public static final Logger LOGGER = Logger.getLogger(DataController.class.getName());

    @Autowired
    private static BasketRepository basketRepository;
    @Autowired
    private static CategoryRepository categoryRepository;
    @Autowired
    private static InventoryRepository inventoryRepository;
    @Autowired
    private static NotificationRepository notificationRepository;
    @Autowired
    private static PermissionsRepository permissionsRepository;
    @Autowired
    private static ProductRepository productRepository;
    @Autowired
    private static PurchaseHistoryRepository purchaseHistoryRepository;
    @Autowired
    private static StoreRepository storeRepository;
    @Autowired
    private static TransactionRepository transactionRepository;
    @Autowired
    private static MarketRepository marketRepository;
    @Autowired
    private static UserRepository userRepository;
    
    private FileHandler fileHandler;

    public DataController() {
        super();
        try {
            fileHandler= new FileHandler("DataController.log",true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set up logger handler.", e);
        }
    }

    public static void setMarketOnline(){
        Market market=marketRepository.findById(0).get();
        market.setOnline(true);
        marketRepository.save(market);
        LOGGER.info("market updated to online at DataBase");
    }

    public static void setMarketOFFLINE(){
        Market market=marketRepository.findById(0).get();
        market.setOnline(false);
        marketRepository.save(market);
        LOGGER.info("market updated to OFFLINE at DataBase");
    }

    public static List<String> getSystemManagers(){
        Market market=marketRepository.findById(0).get();
        List<User> systemManagersEntity=market.getSystemManagers();
        List<String> systemManagers= Collections.synchronizedList(new ArrayList<>());
        for(User user : systemManagersEntity){
            systemManagers.add(user.getUsername());
        }
        return systemManagers;
    }

    public static Boolean getOnline(){
        Market market=marketRepository.findById(0).get();
        return market.getOnline();
    }

    public static void Login(String username){
        User user=userRepository.findById(username).get();
        user.setLoggedIn(true);
        userRepository.save(user);
        LOGGER.info("user is online at the DataBase");
    }
    
    public static void Logout(String username){
        User user=userRepository.findById(username).get();
        user.setLoggedIn(false);
        userRepository.save(user);
        LOGGER.info("user is offline at the DataBase");
    }

    public static void Register(String username,String password,double age) {
        User user=new User();
        user.setUsername(username);
        user.setAge(age);
        user.setPassword(password);
        List<Basket> baskets= Collections.synchronizedList(new ArrayList<>());
        user.setBaskets(baskets);
        user.setLoggedIn(false);
        userRepository.save(user);
        LOGGER.info("user entity added to DataBase");
    }

    public static void cleanShoppingCart(String username){
        Optional<User> optionalUser=userRepository.findById(username);
        if(optionalUser.isPresent()){
            User user=optionalUser.get();
            List<Basket> baskets= Collections.synchronizedList(new ArrayList<>());
            user.setBaskets(baskets);
            userRepository.save(user);
            LOGGER.info("user's shoppingCart is clean at the DataBase");
        }
    }

    public static void addToCart(String username,Integer storeID,Integer productID,Integer quantity){
        Optional<User> optionalUser=userRepository.findById(username);
        if(optionalUser.isPresent()){

            // get the user if registered
            User user=optionalUser.get(); 

            // init product entity to add it to the relevant basket
            ProductEntity productEntity=new ProductEntity();
            Product product=productRepository.findById(productID).get();
            productEntity.setProductID(product);
            productEntity.setQuantity(quantity);

            // init basket with the same id to find it in the list of baskets
            Basket newBasket=new Basket();
            newBasket.setUsername(userRepository.findById(username).get());
            newBasket.setStoreID(storeRepository.findById(storeID).get());

            // get the baskets from the user
            List<Basket> baskets= user.getBaskets();
            
            // get the basket with the same id (if existed)
            Optional<Basket> basket=baskets.stream().filter(b -> b.equals(newBasket)).findFirst();
            
            if(basket.isPresent()){
                // if existed add the product to the basket
                Basket existedBasket=basket.get();
                List<ProductEntity> products=existedBasket.getProducts();
                products.add(productEntity);
                existedBasket.setProducts(products);
                basketRepository.save(existedBasket);
            }
            else{
                // if not; init new basket and add it to the baskets
                List<ProductEntity> products=Collections.synchronizedList(new ArrayList<>());
                products.add(productEntity);
                newBasket.setProducts(products);
                basketRepository.save(newBasket);
            }
            LOGGER.info("product added to "+username+"'s cart at the DataBase");
        }
    }

    public static List<Basket> inspectCart(String username){
        Optional<User> optionalUser=userRepository.findById(username);
        if(optionalUser.isPresent()){

            // get the user if registered
            User user=optionalUser.get(); 

            // get the baskets from the user
            return user.getBaskets();
        }
        LOGGER.severe("user does not exist");
        return null;
    }

    public static void removeCartItem(String username,Integer storeID,Integer productID){
        Optional<User> optionalUser=userRepository.findById(username);
        if(optionalUser.isPresent()){

            // get the user if registered
            User user=optionalUser.get();

            // init basket with the same id to find it in the list of baskets
            Basket newBasket=new Basket();
            newBasket.setUsername(userRepository.findById(username).get());
            newBasket.setStoreID(storeRepository.findById(storeID).get());

            // get the baskets from the user
            List<Basket> baskets= user.getBaskets();
            
            // get the basket with the same id (if existed)
            Basket basket=baskets.stream().filter(b -> b.equals(newBasket)).findFirst().get();
            
            // get the products from the basket
            List<ProductEntity> products=basket.getProducts();

            // get the relevant product entity and remove it
            ProductEntity productEntity=products.stream().filter(p -> p.getProductID().getProductID()==productID).findFirst().get();
            products.remove(productEntity);

            // update the basket
            basket.setProducts(products);
            basketRepository.save(basket);

            LOGGER.info(productID+" removed from "+username+"'s shoppingCart at the DataBase");
        }
    }

    public static void EditPermissions(Integer storeID,String username,Boolean storeOwner,Boolean storeManager,
    Boolean editProducts,Boolean addOrEditPurchaseHistory,Boolean addOrEditDiscountHistory){
        // get relevant permission for the store
        Permissions permissions=permissionsRepository.findById(storeID).get();
        
        // get the tree
        EmployerAndEmployeeEntity employerAndEmployeeEntity=permissions.getEmployer();
        
        // find the employee
        EmployerPermission employee=employerAndEmployeeEntity.findEmployee(username);
        employee.setStoreOwner(storeOwner);
        employee.setStoreManager(storeManager);
        employee.setEditProducts(editProducts);
        employee.setAddOrEditPurchaseHistory(addOrEditPurchaseHistory);
        employee.setAddOrEditDiscountHistory(addOrEditDiscountHistory);

        permissionsRepository.save(permissions);

        LOGGER.info("permission updated at the DataBase");

    }

    public static void AssignStoreManager(Integer storeID,String username){
        // get relevant permission for the store
        Permissions permissions=permissionsRepository.findById(storeID).get();
        
        // get the tree
        EmployerAndEmployeeEntity employerAndEmployeeEntity=permissions.getEmployer();
        
        // find the employee
        EmployerPermission employee=employerAndEmployeeEntity.findEmployee(username);
        employee.setStoreManager(true);
        permissionsRepository.save(permissions);

        LOGGER.info("user assigned as store manager at the DataBase");

    }

    public static void AssignStoreOwner(Integer storeID,String username){
        // get relevant permission for the store
        Permissions permissions=permissionsRepository.findById(storeID).get();
        
        // get the tree
        EmployerAndEmployeeEntity employerAndEmployeeEntity=permissions.getEmployer();
        
        // find the employee
        EmployerPermission employee=employerAndEmployeeEntity.findEmployee(username);
        employee.setStoreOwner(true);
        permissionsRepository.save(permissions);

        LOGGER.info("user assigned as store owner at the DataBase");

    }

    public static void unassignUser(Integer storeID,String username){
        // get relevant permission for the store
        Permissions permissions=permissionsRepository.findById(storeID).get();
        
        // get the tree
        EmployerAndEmployeeEntity employerAndEmployeeEntity=permissions.getEmployer();
        
        // find the employee
        employerAndEmployeeEntity.deleteEmployee(username);
        permissionsRepository.save(permissions);

        LOGGER.info("user unassigned from the DataBase");

    }

    public static void resign(int storeID, String username){
        unassignUser(storeID, username);
    }

    public static void suspendUser(String username) {
        User user=userRepository.findById(username).get();
        user.setSuspended(true);
        userRepository.save(user);
        LOGGER.info("user is suspended at the DataBase");
    }

    public  static void resumeUser(String username) {
        User user=userRepository.findById(username).get();
        user.setSuspended(false);
        userRepository.save(user);
        LOGGER.info("user is not suspended at the DataBase");
    }

    public static List<String> viewSuspended(){
        List<User> users=userRepository.findAll();
        List<User> suspendedUsers=users.stream().filter(user -> user.getSuspended()).toList();
        List<String> usernames=suspendedUsers.stream().map(user -> user.getUsername()).toList();
        return usernames;
    }

    @Transactional
    public static void addCategory(String categoryName, int categoryId){
        Category category=new Category();
        category.setCategoryName(categoryName);
        category.setCategoryID(categoryId);
        categoryRepository.save(category);
        LOGGER.info("added category to db");
    }

    public static void initProduct(String productName, int productId, int categoryId, String description, String brand,double weight) {
        Product product = new Product();
        product.setBrand(brand);
        product.setProductID(productId);
        product.setCatagoryID(categoryId);
        product.setDescription(description);
        product.setProductName(productName);
        product.setWeight(weight);
        productRepository.save(product);
        LOGGER.info("added product to db");
    }

    public static void initStore(String username, String description) {
        Optional<User> optionalUser=userRepository.findById(username);
        if(optionalUser.isPresent()){

            // get the user if registered
            User user=optionalUser.get();
            Store store = new Store();
            store.setActive(false);
            store.setDesciption(description);
            store.setFirstOwner(user);         
            Inventory inv = new Inventory(); 
            inv.setStoreID(store);  
            store.setInventory(inventoryRepository.save(inv));
            store.setRating(0);
            storeRepository.save(store);
            LOGGER.info("added store to db");
        }
        LOGGER.severe("couldnt find user");
    }

    @Transactional
    public static void addProduct(int storeId, int productId, double price, int quantity) {
        Product product = productRepository.getReferenceById(productId);
        Store tempStore = storeRepository.getReferenceById(storeId);
        tempStore.getInventory().addProduct(product, price, quantity);
        LOGGER.info("added product to the store in db");
    }

    public static void removeProduct(int storeId, int productId) {
        Product product = productRepository.getReferenceById(productId);
        Store tempStore = storeRepository.getReferenceById(storeId);
        tempStore.getInventory().removeProduct(product);
        LOGGER.info("removed product from store in db");
    }

    public static void EditProductPrice(int productId, int storeId, Double newPrice) {
        Product product = productRepository.getReferenceById(productId);
        Store tempStore = storeRepository.getReferenceById(storeId);
        tempStore.getInventory().editPrice(product,newPrice);
        LOGGER.info("replaced price in db");
    }

    public static void EditProductQuantity(int productId, int storeId, int newQuantity) {
        Product product = productRepository.getReferenceById(productId);
        Store tempStore = storeRepository.getReferenceById(storeId);
        tempStore.getInventory().editQuantity(product, newQuantity);
        LOGGER.info("replaced quantity in db");
    }

    public static void openStore(int storeId) {
        Store store = storeRepository.getReferenceById(storeId);
        store.setActive(true);
    }

    public static void closeStore(int storeId) {
        Store store = storeRepository.getReferenceById(storeId);
        store.setActive(false);
    }

    public static Store getStore(int storeId) {
        return storeRepository.getReferenceById(storeId);
    }

    public static void removePurchaseHistory(int purchaseId) {
        purchaseHistoryRepository.deleteById(purchaseId);
        LOGGER.info("deleted " + purchaseId + " history from store");
    }

    public static List<PurchaseHistory> getPutchaseHistory() {
        return purchaseHistoryRepository.findAll();
    }

    public List<Store> getAllStores(){
        return new ArrayList<>();
    }

}
