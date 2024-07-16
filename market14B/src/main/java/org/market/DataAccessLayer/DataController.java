package org.market.DataAccessLayer;

import org.market.DataAccessLayer.Entity.*;
import org.market.DataAccessLayer.Repository.*;
import org.market.DomainLayer.backend.Purchase;
import org.market.DomainLayer.backend.StorePackage.Discount.CategoryDiscount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Service
public class DataController {

   public static final Logger LOGGER = Logger.getLogger(DataController.class.getName());

   @Autowired
   private  BasketRepository basketRepository;
   @Autowired
   private  CategoryRepository categoryRepository;
   @Autowired
   private  InventoryRepository inventoryRepository;
   @Autowired
   private  NotificationRepository notificationRepository;
   @Autowired
   private  PermissionsRepository permissionsRepository;
   @Autowired
   private  ProductRepository productRepository;
   @Autowired
   private  PurchaseHistoryRepository purchaseHistoryRepository;
   @Autowired
   private  StoreRepository storeRepository;
   @Autowired
   private  TransactionRepository transactionRepository;
   @Autowired
   private  MarketRepository marketRepository;
   @Autowired
   private  UserRepository userRepository;
   @Autowired
   private PurchaseRepository purchaseRepository;
   @Autowired
   private DiscountRepository discountRepository;
   @Autowired
   private EmployerPermissionRepository employerPermissionRepository;

   private FileHandler fileHandler;

   public DataController() {
       try {
           fileHandler= new FileHandler("DataController.log",true);
           fileHandler.setFormatter(new SimpleFormatter());
           LOGGER.addHandler(fileHandler);
           LOGGER.setLevel(Level.ALL);
       } catch (Exception e) {
           LOGGER.log(Level.SEVERE, "Failed to set up logger handler.", e);
       }
   }

   public void clearAll(){
    basketRepository.deleteAll();
    categoryRepository.deleteAll();
    inventoryRepository.deleteAll();
    notificationRepository.deleteAll();
    permissionsRepository.deleteAll();
    productRepository.deleteAll();
    purchaseHistoryRepository.deleteAll();
    storeRepository.deleteAll();
    transactionRepository.deleteAll();
    marketRepository.deleteAll();
    userRepository.deleteAll();
   }

   public void setMarketOnline(){
       Market market=marketRepository.findById(0).get();
       market.setOnline(true);
       marketRepository.save(market);
       LOGGER.info("market updated to online at DataBase");
   }

   public   void setMarketOFFLINE(){
       Market market=marketRepository.findById(0).get();
       market.setOnline(false);
       marketRepository.save(market);
       LOGGER.info("market updated to OFFLINE at DataBase");
   }

   public   List<String> getSystemManagers(int id){
       Market market=marketRepository.findById(id).get();
       List<User> systemManagersEntity=market.getSystemManagers();
       List<String> systemManagers= Collections.synchronizedList(new ArrayList<>());
       for(User user : systemManagersEntity){
           systemManagers.add(user.getUsername());
       }
       return systemManagers;
   }

   public void loadData() throws Exception{
        // system managers
        Market market=marketRepository.findById(0).get();
        List<User> systemManagersEntity=market.getSystemManagers();
        for(User user : systemManagersEntity){
            org.market.DomainLayer.backend.Market.addToSystemManagers(user.getUsername());
        }
        // usercontroller
        List<User> users=new ArrayList<>();
        users=userRepository.findAll();
        for(User user : users){
            org.market.DomainLayer.backend.Market.getUC().loudUser(user.getUsername(), user.getPassword(), user.getAge());
        }

        // store controller
        List<Store> stores=new ArrayList<>();
        stores=storeRepository.findAll();
        Integer maxId=-1;
        for(Store store : stores){
            if(maxId<store.getStoreID())
                maxId=store.getStoreID();
            org.market.DomainLayer.backend.Market.getSC().loudStore(store.getStoreID(),store.getFirstOwner().getUsername(),store.getName(),store.getDesciption());
        }
        org.market.DomainLayer.backend.Market.getSC().setCounterID(maxId);

        // category controller
        List<Category> categories=categoryRepository.findAll();
        for(Category category : categories){
            org.market.DomainLayer.backend.Market.getCC().addCategory(category.getCategoryName());
            for(Category subCategory : category.getSubCategories()){
                org.market.DomainLayer.backend.Market.getCC().addCategory(subCategory.getCategoryName(), category.getCategoryID());
            }
        }

        // store inventories
        List<Inventory> inventories=inventoryRepository.findAll();
        for(Inventory inventory : inventories){
            Integer storeId=inventory.getStoreID().getStoreID();
            List<ProductEntity> productEntities=inventory.getProducts();
            for(ProductEntity productEntity : productEntities){
                // int productId, int storeId, double price, int quantity,double weight
                int productId=productEntity.getProductID().getProductID();
                int storeID=inventory.getStoreID().getStoreID();
                double price=productEntity.getPrice();
                int quantity=productEntity.getQuantity();
                double weight=productRepository.findById(productEntity.getProductID().getProductID()).get().getWeight();
                org.market.DomainLayer.backend.Market.getSC().addProduct(productId, storeID, price, quantity, weight);
            }
        }

        // product controller
        List<Product> products=productRepository.findAll();
        for(Product product : products){
            org.market.DomainLayer.backend.ProductPackage.Category category=org.market.DomainLayer.backend.Market.getCC().getCategory(product.getCatagoryID().getCategoryID());
            org.market.DomainLayer.backend.Market.getPC().addProduct(product.getProductName(), category, product.getDescription(), product.getBrand(), product.getWeight());
        }

        // purchase history
        List<PurchaseHistory> purchases=purchaseHistoryRepository.findAll();
        for(PurchaseHistory purchaseEntity : purchases){
            Map<Integer, double[]> productsMap=new ConcurrentHashMap<>();
            for(ProductEntity productEntity : purchaseEntity.getProducts()){
                double[] QP={productEntity.getQuantity(),productEntity.getPrice()};
                productsMap.put(productEntity.getProductID().getProductID(),QP);
            }
            Purchase purchase=new Purchase(purchaseEntity.getUsername().getUsername(),purchaseEntity.getStoreID().getStoreID(), purchaseEntity.getOvlprice(), productsMap); 
            org.market.DomainLayer.backend.Market.getPH().addPurchase(purchaseEntity.getStoreID().getStoreID(), purchaseEntity.getUsername().getUsername(), purchase);
        }

        // permissions
        List<EmployerPermission> permissions=employerPermissionRepository.findAll();
        for(EmployerPermission permission : permissions){
            int storeId=permission.getStoreID().getStoreID();
            List<EmployerPermission> employees=permission.getEmployees();
            if(!org.market.DomainLayer.backend.Market.getP().storeExist(storeId)){
                org.market.DomainLayer.backend.Market.getP().initStore(storeId, permission.getUsername().getUsername());
            }
            else{
                Boolean[] pType={permission.getEditProducts(),permission.getAddOrEditPurchaseHistory(),permission.getAddOrEditDiscountHistory()};
                org.market.DomainLayer.backend.Market.getP().addPermission(storeId, permission.getParentusername().getUsername(), permission.getUsername().getUsername(), permission.getStoreOwner(), permission.getStoreManager(), pType);
            }
            for(EmployerPermission employee : employees){
                Boolean[] pType={employee.getEditProducts(),employee.getAddOrEditPurchaseHistory(),employee.getAddOrEditDiscountHistory()};
                org.market.DomainLayer.backend.Market.getP().addPermission(storeId, employee.getParentusername().getUsername(), employee.getUsername().getUsername(), employee.getStoreOwner(), employee.getStoreManager(), pType);
            }
        }

        // discount policy
        List<DiscountPolicy> discountPolicies=discountRepository.findAll();
        for(DiscountPolicy discountPolicy : discountPolicies){
            switch (discountPolicy.getType()) {
                case "category":
                    org.market.DomainLayer.backend.Market.loudCategoryDiscountPolicy(discountPolicy.getStandard(),discountPolicy.getConditionalPrice(),discountPolicy.getConditionalQuantity(),discountPolicy.getDiscountPercentage(),discountPolicy.getCategoryId(),discountPolicy.getStoreId().getStoreID(),discountPolicy.getUsername(),discountPolicy.getParentDiscount().getDiscountId());
                    break;
                case "product":
                    org.market.DomainLayer.backend.Market.loudProductDiscountPolicy(discountPolicy.getStandard(),discountPolicy.getConditionalPrice(),discountPolicy.getConditionalQuantity(),discountPolicy.getDiscountPercentage(),discountPolicy.getCategoryId(),discountPolicy.getStoreId().getStoreID(),discountPolicy.getUsername(),discountPolicy.getParentDiscount().getDiscountId());
                    break;
                case "store":
                    org.market.DomainLayer.backend.Market.loudStoreDiscountPolicy(discountPolicy.getStandard(),discountPolicy.getConditionalPrice(),discountPolicy.getConditionalQuantity(),discountPolicy.getDiscountPercentage(),discountPolicy.getCategoryId(),discountPolicy.getStoreId().getStoreID(),discountPolicy.getUsername(),discountPolicy.getParentDiscount().getDiscountId());
                    break;
                case "add":
                    org.market.DomainLayer.backend.Market.loudNmericalDiscount(discountPolicy.getUsername(),discountPolicy.getStoreId().getStoreID(),true,discountPolicy.getParentDiscount().getDiscountId());
                    break;
                
                default:
                    org.market.DomainLayer.backend.Market.loudLogicalDiscount(discountPolicy.getUsername(), discountPolicy.getStoreId().getStoreID(), discountPolicy.getType(), discountPolicy.getParentDiscount().getDiscountId());
                    break;
            }
        }

        // purchase policy
        List<PurchasePolicy> purchasePolicies=purchaseRepository.findAll();
        for(PurchasePolicy purchasePolicy : purchasePolicies){
            switch (purchasePolicy.getType()) {
                case "category":
                    org.market.DomainLayer.backend.Market.loudCategoryPurchasePolicy(purchasePolicy.getQuantity(), purchasePolicy.getPrice(), LocalDate.now(), purchasePolicy.getAtLeast(), purchasePolicy.getWeight(), purchasePolicy.getAge(), purchasePolicy.getCategoryId(), purchasePolicy.getUsername(), purchasePolicy.getStoreId().getStoreID(),purchasePolicy.getImmediate(), purchasePolicy.getParentPurchase().getPurchaseId());
                    break;
                case "product":
                    org.market.DomainLayer.backend.Market.loudProductPurchasePolicy(purchasePolicy.getQuantity(), purchasePolicy.getPrice(), LocalDate.now(), purchasePolicy.getAtLeast(), purchasePolicy.getWeight(), purchasePolicy.getAge(), purchasePolicy.getCategoryId(), purchasePolicy.getUsername(), purchasePolicy.getStoreId().getStoreID(),purchasePolicy.getImmediate(), purchasePolicy.getParentPurchase().getPurchaseId());
                    break;
                case "shoppingcart":
                    org.market.DomainLayer.backend.Market.loudShoppingCartPurchasePolicy(purchasePolicy.getQuantity(), purchasePolicy.getPrice(), LocalDate.now(), purchasePolicy.getAtLeast(), purchasePolicy.getWeight(), purchasePolicy.getAge(), purchasePolicy.getCategoryId(), purchasePolicy.getUsername(), purchasePolicy.getStoreId().getStoreID(),purchasePolicy.getImmediate(), purchasePolicy.getParentPurchase().getPurchaseId());
                    break;
                case "user":
                    org.market.DomainLayer.backend.Market.loudUserPurchasePolicy(purchasePolicy.getQuantity(), purchasePolicy.getPrice(), LocalDate.now(), purchasePolicy.getAtLeast(), purchasePolicy.getWeight(), purchasePolicy.getAge(), purchasePolicy.getCategoryId(), purchasePolicy.getUsername(), purchasePolicy.getStoreId().getStoreID(),purchasePolicy.getImmediate(), purchasePolicy.getParentPurchase().getPurchaseId());
                    break;
            
                default:
                    org.market.DomainLayer.backend.Market.loudLogicalPurchase(purchasePolicy.getUsername(),purchasePolicy.getStoreId().getStoreID(),purchasePolicy.getType(),purchasePolicy.getParentPurchase().getProductId());
                    break;
            }
        }
   }

   public   Boolean getOnline(){
       Market market=marketRepository.findById(0).get();
       return market.getOnline();
   }

   public   void Login(String username){
       User user=userRepository.findById(username).get();
       user.setLoggedIn(true);
       userRepository.save(user);
       LOGGER.info("user is online at the DataBase");
   }

   public   void Logout(String username){
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

   public   void cleanShoppingCart(String username){
       Optional<User> optionalUser=userRepository.findById(username);
       if(optionalUser.isPresent()){
           User user=optionalUser.get();
           List<Basket> baskets= Collections.synchronizedList(new ArrayList<>());
           user.setBaskets(baskets);
           userRepository.save(user);
           LOGGER.info("user's shoppingCart is clean at the DataBase");
       }
   }

   public   void addToCart(String username,Integer storeID,Integer productID,Integer quantity){
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

   public   List<Basket> inspectCart(String username){
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

   public   void removeCartItem(String username,Integer storeID,Integer productID){
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

   public   void EditPermissions(Integer storeID,String username,Boolean storeOwner,Boolean storeManager,
   Boolean editProducts,Boolean addOrEditPurchaseHistory,Boolean addOrEditDiscountHistory){
    Store store=storeRepository.findById(storeID).get();
       // get relevant permission for the store
       Permissions permissions=permissionsRepository.findById(store).get();

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

   public   void AssignStoreManager(Integer storeID,String username){
    Store store=storeRepository.findById(storeID).get();
       // get relevant permission for the store
       Permissions permissions=permissionsRepository.findById(store).get();

       // get the tree
       EmployerAndEmployeeEntity employerAndEmployeeEntity=permissions.getEmployer();

       // find the employee
       EmployerPermission employee=employerAndEmployeeEntity.findEmployee(username);
       employee.setStoreManager(true);
       permissionsRepository.save(permissions);

       LOGGER.info("user assigned as store manager at the DataBase");

   }

   public   void AssignStoreOwner(Integer storeID,String username){
        Store store=storeRepository.findById(storeID).get();
       // get relevant permission for the store
       Permissions permissions=permissionsRepository.findById(store).get();

       // get the tree
       EmployerAndEmployeeEntity employerAndEmployeeEntity=permissions.getEmployer();

       // find the employee
       EmployerPermission employee=employerAndEmployeeEntity.findEmployee(username);
       employee.setStoreOwner(true);
       permissionsRepository.save(permissions);

       LOGGER.info("user assigned as store owner at the DataBase");

   }

   public   void unassignUser(Integer storeID,String username){
    Store store=storeRepository.findById(storeID).get();
       // get relevant permission for the store
       Permissions permissions=permissionsRepository.findById(store).get();

       // get the tree
       EmployerAndEmployeeEntity employerAndEmployeeEntity=permissions.getEmployer();

       // find the employee
       employerAndEmployeeEntity.deleteEmployee(username);
       permissionsRepository.save(permissions);

       LOGGER.info("user unassigned from the DataBase");

   }

   public   void resign(int storeID, String username){
       unassignUser(storeID, username);
   }

   public   void suspendUser(String username) {
       User user=userRepository.findById(username).get();
       user.setSuspended(true);
       userRepository.save(user);
       LOGGER.info("user is suspended at the DataBase");
   }

   public   void resumeUser(String username) {
       User user=userRepository.findById(username).get();
       user.setSuspended(false);
       userRepository.save(user);
       LOGGER.info("user is not suspended at the DataBase");
   }

   public   List<String> viewSuspended(){
       List<User> users=userRepository.findAll();
       List<User> suspendedUsers=users.stream().filter(user -> user.getSuspended()).toList();
       List<String> usernames=suspendedUsers.stream().map(user -> user.getUsername()).toList();
       return usernames;
   }

   @Transactional
   public   void addCategory(String categoryName, int categoryId){
       Category category=new Category();
       category.setCategoryName(categoryName);
       category.setCategoryID(categoryId);
       categoryRepository.save(category);
       LOGGER.info("added category to db");
   }

   public   void initProduct(String productName, int productId, int categoryId, String description, String brand,double weight) {
       Product product = new Product();
       product.setBrand(brand);
       product.setProductID(productId);
       Category category = categoryRepository.findById(categoryId).get();
       product.setCatagoryID(category);
       product.setDescription(description);
       product.setProductName(productName);
       product.setWeight(weight);
       productRepository.save(product);
       LOGGER.info("added product to db");
   }

   public   void initStore(String username, String description,int id) {
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
           //store.setInventory(inventoryRepository.save(inv));
           store.setRating(0);
           store.setStoreID(id);
           storeRepository.save(store);
           LOGGER.info("added store to db");
       }
       LOGGER.severe("couldnt find user");
   }

   @Transactional
   public   void addProduct(int storeId, int productId, double price, int quantity) {
       Product product = productRepository.getReferenceById(productId);
       //Store tempStore = storeRepository.getReferenceById(storeId);
       //tempStore.getInventory().addProduct(product, price, quantity);
       Store store=storeRepository.findById(storeId).get();
       Inventory inventory = inventoryRepository.findById(store).get();
       inventory.addProduct(product, price, quantity);
       inventoryRepository.save(inventory);
       LOGGER.info("added product to the store in db");
   }

   public   void removeProduct(int storeId, int productId) {
       Product product = productRepository.getReferenceById(productId);
       //Store tempStore = storeRepository.getReferenceById(storeId);
       Store store=storeRepository.findById(storeId).get();
       Inventory inventory = inventoryRepository.findById(store).get();
       inventory.removeProduct(product);
       inventoryRepository.save(inventory);
       LOGGER.info("removed product from store in db");
   }

   public   void EditProductPrice(int productId, int storeId, Double newPrice) {
       Product product = productRepository.getReferenceById(productId);
       //Store tempStore = storeRepository.getReferenceById(storeId);
       Store store=storeRepository.findById(storeId).get();
       Inventory inventory = inventoryRepository.findById(store).get();
       inventory.editPrice(product,newPrice);
       inventoryRepository.save(inventory);
       LOGGER.info("replaced price in db");
   }

   public   void EditProductQuantity(int productId, int storeId, int newQuantity) {
       Product product = productRepository.getReferenceById(productId);
       //Store tempStore = storeRepository.getReferenceById(storeId);
       Store store=storeRepository.findById(storeId).get();
       Inventory inventory = inventoryRepository.findById(store).get();
       inventory.editQuantity(product,newQuantity);
       inventoryRepository.save(inventory);
       LOGGER.info("replaced quantity in db");
   }

   public   void openStore(int storeId) {
       Store store = storeRepository.getReferenceById(storeId);
       store.setActive(true);
   }

   public   void closeStore(int storeId) {
       Store store = storeRepository.getReferenceById(storeId);
       store.setActive(false);
   }

   public   Store getStore(int storeId) {
       return storeRepository.getReferenceById(storeId);
   }

   public   void removePurchaseHistory(int purchaseId) {
       purchaseHistoryRepository.deleteById(purchaseId);
       LOGGER.info("deleted " + purchaseId + " history from store");
   }

   public   List<PurchaseHistory> getPutchaseHistory() {
       return purchaseHistoryRepository.findAll();
   }

   public   List<Store> getAllStores(){
       //get all stores
       return new ArrayList<>();
   }

   public BasketRepository getBasketRepository() {
    return basketRepository;
}

public CategoryRepository getCategoryRepository() {
    return categoryRepository;
}

public InventoryRepository getInventoryRepository() {
    return inventoryRepository;
}

public NotificationRepository getNotificationRepository() {
    return notificationRepository;
}

public PermissionsRepository getPermissionsRepository() {
    return permissionsRepository;
}

public ProductRepository getProductRepository() {
    return productRepository;
}

public PurchaseHistoryRepository getPurchaseHistoryRepository() {
    return purchaseHistoryRepository;
}

public StoreRepository getStoreRepository() {
    return storeRepository;
}

public TransactionRepository getTransactionRepository() {
    return transactionRepository;
}

public MarketRepository getMarketRepository() {
    return marketRepository;
}

public UserRepository getUserRepository() {
    return userRepository;
}

public void addDiscountPolicy(Boolean standard, double conditionalPrice, double conditionalQuantity,
        double discountPercentage, int categoryId, int productId, int storeId, String username, int parentid, String type) {
    DiscountPolicy discountPolicy=new DiscountPolicy();
    discountPolicy.setStandard(standard);
    discountPolicy.setConditionalPrice(conditionalPrice);
    discountPolicy.setConditionalQuantity(conditionalQuantity);
    discountPolicy.setDiscountPercentage(discountPercentage);
    discountPolicy.setCategoryId(categoryId);
    discountPolicy.setProductId(productId);
    Store store=storeRepository.findById(storeId).get();
    discountPolicy.setStoreId(store);
    discountPolicy.setUsername(username);
    DiscountPolicy dp=discountRepository.findById(parentid).get();
    discountPolicy.setParentDiscount(dp);
    discountPolicy.setType(type);
    discountRepository.save(discountPolicy);
    List<DiscountPolicy> subDiscountPolicies=dp.getSubDiscounts();
    subDiscountPolicies.add(discountPolicy);
    dp.setSubDiscounts(subDiscountPolicies);
    discountRepository.save(dp);
}

public void addPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,
        double userAge, int categoryId, int productId, String username, int storeId, Boolean immediate, int parentid, String type) {
    PurchasePolicy purchasePolicy=new PurchasePolicy();
    purchasePolicy.setQuantity(quantity);
    purchasePolicy.setPrice(price);
    purchasePolicy.setDate(date.toString());
    purchasePolicy.setAtLeast(atLeast);
    purchasePolicy.setWeight(weight);
    purchasePolicy.setAge(userAge);
    purchasePolicy.setUserAge(userAge);
    purchasePolicy.setCategoryId(categoryId);
    purchasePolicy.setProductId(productId);
    purchasePolicy.setUsername(username);
    Store store=storeRepository.findById(storeId).get();
    purchasePolicy.setStoreId(store);
    purchasePolicy.setImmediate(immediate);
    PurchasePolicy pp=purchaseRepository.findById(parentid).get();
    purchasePolicy.setParentPurchase(pp);
    purchasePolicy.setType(type);
    purchaseRepository.save(purchasePolicy);
    List<PurchasePolicy> subPurchasePolicies=pp.getSubPurchases();
    subPurchasePolicies.add(purchasePolicy);
    pp.setSubPurchases(subPurchasePolicies);
    purchaseRepository.save(pp);
}


}
