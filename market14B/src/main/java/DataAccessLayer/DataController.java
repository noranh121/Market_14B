package DataAccessLayer;

import DataAccessLayer.Repository.*;

import java.util.*;
import java.util.logging.*;

import DataAccessLayer.Entity.*;

public class DataController {
    public static final Logger LOGGER = Logger.getLogger(DataController.class.getName());
    private BasketRepository basketRepository;
    private CategoryRepository categoryRepository;
    private InventoryRepository inventoryRepository;
    private NotificationRepository notificationRepository;
    private PermissionsRepository permissionsRepository;
    private ProductRepository productRepository;
    private PurchaseHistoryRepository purchaseHistoryRepository;
    private StoreRepository storeRepository;
    private TransactionRepository transactionRepository;
    private MarketRepository marketRepository;
    private UserRepository userRepository;
    
    private static DataController instance;
    private FileHandler fileHandler;

    public static synchronized DataController getinstance() {
        if (instance == null) {
            instance = new DataController();
        }
        return instance;
    }

    private DataController() {
        try {
            fileHandler= new FileHandler("DataController.log",true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set up logger handler.", e);
        }
    }

    public void setMarketOnline(){
        Market market=marketRepository.findById(0).get();
        market.setOnline(true);
        marketRepository.save(market);
        LOGGER.info("market updated to online at DataBase");
    }

    public void setMarketOFFLINE(){
        Market market=marketRepository.findById(0).get();
        market.setOnline(false);
        marketRepository.save(market);
        LOGGER.info("market updated to OFFLINE at DataBase");
    }

    public List<String> getSystemManagers(){
        Market market=marketRepository.findById(0).get();
        List<User> systemManagersEntity=market.getSystemManagers();
        List<String> systemManagers= Collections.synchronizedList(new ArrayList<>());
        for(User user : systemManagersEntity){
            systemManagers.add(user.getUsername());
        }
        return systemManagers;
    }

    public Boolean getOnline(){
        Market market=marketRepository.findById(0).get();
        return market.getOnline();
    }

    public void Login(String username){
        User user=userRepository.findById(username).get();
        user.setLoggedIn(true);
        userRepository.save(user);
        LOGGER.info("user is online at the DataBase");
    }
    
    public void Logout(String username){
        User user=userRepository.findById(username).get();
        user.setLoggedIn(false);
        userRepository.save(user);
        LOGGER.info("user is offline at the DataBase");
    }

    public void Register(String username,String password,double age) {
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

    public void cleanShoppingCart(String username){
        Optional<User> optionalUser=userRepository.findById(username);
        if(optionalUser.isPresent()){
            User user=optionalUser.get();
            List<Basket> baskets= Collections.synchronizedList(new ArrayList<>());
            user.setBaskets(baskets);
            userRepository.save(user);
            LOGGER.info("user's shoppingCart is clean at the DataBase");
        }
    }

    public void addToCart(String username,Integer storeID,Integer productID,Integer quantity){
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

    public void inspectCart(String username){
        // please implement this method
    }

    public void removeCartItem(String username,Integer storeID,Integer productID){
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

    public void EditPermissions(Integer storeID,String username,Boolean storeOwner,Boolean storeManager,
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

    public void AssignStoreManager(Integer storeID,String username){
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

    public void AssignStoreOwner(Integer storeID,String username){
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

    public void unassignUser(Integer storeID,String username){
        // get relevant permission for the store
        Permissions permissions=permissionsRepository.findById(storeID).get();
        
        // get the tree
        EmployerAndEmployeeEntity employerAndEmployeeEntity=permissions.getEmployer();
        
        // find the employee
        employerAndEmployeeEntity.deleteEmployee(username);
        permissionsRepository.save(permissions);

        LOGGER.info("user unassigned from the DataBase");

    }

    public void resign(int storeID, String username){
        // please implement this method
    }

    public void suspendUser(String username) {
        User user=userRepository.findById(username).get();
        user.setSuspended(true);
        userRepository.save(user);
        LOGGER.info("user is suspended at the DataBase");
    }

    public void resumeUser(String username) {
        User user=userRepository.findById(username).get();
        user.setSuspended(false);
        userRepository.save(user);
        LOGGER.info("user is not suspended at the DataBase");
    }

    public List<String> viewSuspended(){
        List<User> users=userRepository.findAll();
        List<User> suspendedUsers=users.stream().filter(user -> user.getSuspended()).toList();
        List<String> usernames=suspendedUsers.stream().map(user -> user.getUsername()).toList();
        return usernames;
    }

    public void addCategory(String categoryName){
        Category category=new Category();
        category.setCategoryName(categoryName);
        categoryRepository.save(category);
    }

}
