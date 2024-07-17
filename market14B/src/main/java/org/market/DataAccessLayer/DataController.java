package org.market.DataAccessLayer;

import org.market.DataAccessLayer.Entity.*;
import org.market.DataAccessLayer.Repository.*;
import org.market.DomainLayer.backend.Purchase;
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
    private BasketRepository basketRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private MarketRepository marketRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private ProductEntityRepository productEntityRepository;
   @Autowired
   private EmployerPermissionRepository employerPermissionRepository;

    public EmployerPermissionRepository getEmployerPermissionRepository() {
    return employerPermissionRepository;
}

    private FileHandler fileHandler;

    public DataController() {
        try {
            fileHandler = new FileHandler("DataController.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set up logger handler.", e);
        }
    }

    public void clearAll() {
        discountRepository.deleteAll();
        inventoryRepository.deleteAll();
        // basketRepository.deleteAll();
        purchaseHistoryRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        notificationRepository.deleteAll();
        employerPermissionRepository.deleteAll();
        storeRepository.deleteAll();
        transactionRepository.deleteAll();
        marketRepository.deleteAll();
        userRepository.deleteAll();
        basketRepository.deleteAll();
        productEntityRepository.deleteAll();
        purchaseRepository.deleteAll();

        // basketRepository.deleteAll();
        // categoryRepository.deleteAll();
        // inventoryRepository.deleteAll();
        // notificationRepository.deleteAll();
        // employerPermissionRepository.deleteAll();
        // productRepository.deleteAll();
        // purchaseHistoryRepository.deleteAll();
        // storeRepository.deleteAll();
        // transactionRepository.deleteAll();
        // marketRepository.deleteAll();
        // userRepository.deleteAll();
        // productEntityRepository.deleteAll();
    }

    public void setMarketOnline() {
        Market market = marketRepository.findById(0).get();
        market.setOnline(true);
        marketRepository.save(market);
        LOGGER.info("market updated to online at DataBase");
    }

    public void setMarketOFFLINE() {
        Market market = marketRepository.findById(0).get();
        market.setOnline(false);
        marketRepository.save(market);
        LOGGER.info("market updated to OFFLINE at DataBase");
    }

    public List<String> getSystemManagers(int id) {
        Market market = marketRepository.findById(id).get();
        List<User> systemManagersEntity = market.getSystemManagers();
        List<String> systemManagers = Collections.synchronizedList(new ArrayList<>());
        for (User user : systemManagersEntity) {
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
            List<Basket> baskets=user.getBaskets();
            for(Basket basket : baskets){
                List<ProductBasket> productEntities=basket.getProducts();
                for(ProductBasket productEntity : productEntities){
                    org.market.DomainLayer.backend.Market.getUC().getUser(user.getUsername()).addToCart(productEntity.getProductID(),basket.getStoreID(),productEntity.getQuantity());
                }
            }
        }

        // store controller
        List<Store> stores=new ArrayList<>();
        stores=storeRepository.findAll();
        Integer maxStoreId=-1;
        for(Store store : stores){
            if(maxStoreId<store.getStoreID())
                maxStoreId=store.getStoreID();
            org.market.DomainLayer.backend.Market.getSC().loudStore(store.getStoreID(),store.getFirstOwner().getUsername(),store.getName(),store.getDesciption());
        }
        org.market.DomainLayer.backend.Market.getSC().setCounterID(maxStoreId);

        // category controller
        List<Category> categories=categoryRepository.findAll();
        int maxCategoryId=-1;
        for(Category category : categories){
            if(maxCategoryId<category.getCategoryID())
                maxCategoryId=category.getCategoryID();
            org.market.DomainLayer.backend.Market.getCC().addCategory(category.getCategoryName());
            for(Category subCategory : category.getSubCategories()){
                if(maxCategoryId<subCategory.getCategoryID())
                    maxCategoryId=subCategory.getCategoryID();
                org.market.DomainLayer.backend.Market.getCC().addCategory(subCategory.getCategoryName(), category.getCategoryID());
            }
        }
        org.market.DomainLayer.backend.Market.getCC().setCounterID(maxCategoryId);

        // store inventories
        List<Inventory> inventories=inventoryRepository.findAll();
        for(Inventory inventory : inventories){
            // Integer storeId=inventory.getStoreID();
            List<ProductEntity> productEntities=inventory.getProducts();
            for(ProductEntity productEntity : productEntities){
                // int productId, int storeId, double price, int quantity,double weight
                int productId=productEntity.getProductID();
                int storeID=inventory.getStoreID();
                double price=productEntity.getPrice();
                int quantity=productEntity.getQuantity();
                double weight=productRepository.findById(productEntity.getProductID()).get().getWeight();
                org.market.DomainLayer.backend.Market.getSC().addProduct(productId, storeID, price, quantity, weight);
            }
        }

        // product controller
        List<Product> products=productRepository.findAll();
        int maxProductId=-1;
        for(Product product : products){
            if(maxProductId<product.getProductID())
                maxProductId=product.getProductID();
            org.market.DomainLayer.backend.ProductPackage.Category category=org.market.DomainLayer.backend.Market.getCC().getCategory(product.getCatagoryID().getCategoryID());
            org.market.DomainLayer.backend.Market.getPC().addProduct(product.getProductName(), category, product.getDescription(), product.getBrand(), product.getWeight());
        }
        org.market.DomainLayer.backend.Market.getPC().setIdCounter(maxProductId);

        // purchase history
        List<PurchaseHistory> purchases=purchaseHistoryRepository.findAll();
        int maxPurcahseHistoryId=-1;
        for(PurchaseHistory purchaseEntity : purchases){
            if(maxPurcahseHistoryId<purchaseEntity.getPurchaseID())
                maxPurcahseHistoryId=purchaseEntity.getPurchaseID();
            Map<Integer, double[]> productsMap=new ConcurrentHashMap<>();
            for(ProductScreenShot productEntity : purchaseEntity.getProducts()){
                double[] QP={productEntity.getQuantity(),productEntity.getPrice()};
                productsMap.put(productEntity.getProductID(),QP);
            }
            Purchase purchase=new Purchase(purchaseEntity.getUsername(),purchaseEntity.getStoreID(), purchaseEntity.getOvlprice(), productsMap); 
            org.market.DomainLayer.backend.Market.getPH().addPurchase(purchaseEntity.getStoreID(), purchaseEntity.getUsername(), purchase);
        }
        org.market.DomainLayer.backend.Market.getPH().setCounterID(maxPurcahseHistoryId);

        // permissions
        List<EmployerPermission> permissions=employerPermissionRepository.findAll();
        for(EmployerPermission permission : permissions){
            int storeId=permission.getPermissionId().getStoreID();
            List<EmployerPermission> employees=permission.getEmployees();
            if(!org.market.DomainLayer.backend.Market.getP().storeExist(storeId)){
                org.market.DomainLayer.backend.Market.getP().initStore(storeId, permission.getPermissionId().getUsername());
            }
            else{
                Boolean[] pType={permission.getEditProducts(),permission.getAddOrEditPurchaseHistory(),permission.getAddOrEditDiscountHistory()};
                org.market.DomainLayer.backend.Market.getP().addPermission(storeId, permission.getParentusername(), permission.getPermissionId().getUsername(), permission.getStoreOwner(), permission.getStoreManager(), pType);
            }
            for(EmployerPermission employee : employees){
                Boolean[] pType={employee.getEditProducts(),employee.getAddOrEditPurchaseHistory(),employee.getAddOrEditDiscountHistory()};
                org.market.DomainLayer.backend.Market.getP().addPermission(storeId, employee.getParentusername(), employee.getPermissionId().getUsername(), employee.getStoreOwner(), employee.getStoreManager(), pType);
            }
        }

        // discount policy
        List<DiscountPolicy> discountPolicies=discountRepository.findAll();
        for(DiscountPolicy discountPolicy : discountPolicies){
            switch (discountPolicy.getType()) {
                case "category":
                    org.market.DomainLayer.backend.Market.loudCategoryDiscountPolicy(discountPolicy.getStandard(),discountPolicy.getConditionalPrice(),discountPolicy.getConditionalQuantity(),discountPolicy.getDiscountPercentage(),discountPolicy.getCategoryId(),discountPolicy.getStoreId(),discountPolicy.getUsername(),discountPolicy.getParentDiscount().getDiscountId());
                    break;
                case "product":
                    org.market.DomainLayer.backend.Market.loudProductDiscountPolicy(discountPolicy.getStandard(),discountPolicy.getConditionalPrice(),discountPolicy.getConditionalQuantity(),discountPolicy.getDiscountPercentage(),discountPolicy.getCategoryId(),discountPolicy.getStoreId(),discountPolicy.getUsername(),discountPolicy.getParentDiscount().getDiscountId());
                    break;
                case "store":
                    org.market.DomainLayer.backend.Market.loudStoreDiscountPolicy(discountPolicy.getStandard(),discountPolicy.getConditionalPrice(),discountPolicy.getConditionalQuantity(),discountPolicy.getDiscountPercentage(),discountPolicy.getCategoryId(),discountPolicy.getStoreId(),discountPolicy.getUsername(),discountPolicy.getParentDiscount().getDiscountId());
                    break;
                case "add":
                    org.market.DomainLayer.backend.Market.loudNmericalDiscount(discountPolicy.getUsername(),discountPolicy.getStoreId(),true,discountPolicy.getParentDiscount().getDiscountId());
                    break;
                
                default:
                    org.market.DomainLayer.backend.Market.loudLogicalDiscount(discountPolicy.getUsername(), discountPolicy.getStoreId(), discountPolicy.getType(), discountPolicy.getParentDiscount().getDiscountId());
                    break;
            }
        }

        // purchase policy
        List<PurchasePolicy> purchasePolicies=purchaseRepository.findAll();
        for(PurchasePolicy purchasePolicy : purchasePolicies){
            switch (purchasePolicy.getType()) {
                case "category":
                    org.market.DomainLayer.backend.Market.loudCategoryPurchasePolicy(purchasePolicy.getQuantity(), purchasePolicy.getPrice(), LocalDate.now(), purchasePolicy.getAtLeast(), purchasePolicy.getWeight(), purchasePolicy.getAge(), purchasePolicy.getCategoryId(), purchasePolicy.getUsername(), purchasePolicy.getStoreId(),purchasePolicy.getImmediate(), purchasePolicy.getParentPurchase().getPurchaseId());
                    break;
                case "product":
                    org.market.DomainLayer.backend.Market.loudProductPurchasePolicy(purchasePolicy.getQuantity(), purchasePolicy.getPrice(), LocalDate.now(), purchasePolicy.getAtLeast(), purchasePolicy.getWeight(), purchasePolicy.getAge(), purchasePolicy.getCategoryId(), purchasePolicy.getUsername(), purchasePolicy.getStoreId(),purchasePolicy.getImmediate(), purchasePolicy.getParentPurchase().getPurchaseId());
                    break;
                case "shoppingcart":
                    org.market.DomainLayer.backend.Market.loudShoppingCartPurchasePolicy(purchasePolicy.getQuantity(), purchasePolicy.getPrice(), LocalDate.now(), purchasePolicy.getAtLeast(), purchasePolicy.getWeight(), purchasePolicy.getAge(), purchasePolicy.getCategoryId(), purchasePolicy.getUsername(), purchasePolicy.getStoreId(),purchasePolicy.getImmediate(), purchasePolicy.getParentPurchase().getPurchaseId());
                    break;
                case "user":
                    org.market.DomainLayer.backend.Market.loudUserPurchasePolicy(purchasePolicy.getQuantity(), purchasePolicy.getPrice(), LocalDate.now(), purchasePolicy.getAtLeast(), purchasePolicy.getWeight(), purchasePolicy.getAge(), purchasePolicy.getCategoryId(), purchasePolicy.getUsername(), purchasePolicy.getStoreId(),purchasePolicy.getImmediate(), purchasePolicy.getParentPurchase().getPurchaseId());
                    break;
            
                default:
                    org.market.DomainLayer.backend.Market.loudLogicalPurchase(purchasePolicy.getUsername(),purchasePolicy.getStoreId(),purchasePolicy.getType(),purchasePolicy.getParentPurchase().getProductId());
                    break;
            }
        }
    }

    public Boolean getOnline() {
        Market market = marketRepository.findById(0).get();
        return market.getOnline();
    }

    public void Login(String username) {
        User user = userRepository.findById(username).get();
        user.setLoggedIn(true);
        userRepository.save(user);
        LOGGER.info("user is online at the DataBase");
    }

    public void Logout(String username) {
        User user = userRepository.findById(username).get();
        user.setLoggedIn(false);
        userRepository.save(user);
        LOGGER.info("user is offline at the DataBase");
    }

    public void Register(String username, String password, double age) {
        User user = new User();
        user.setUsername(username);
        user.setAge(age);
        user.setPassword(password);
        List<Basket> baskets = Collections.synchronizedList(new ArrayList<>());
        user.setBaskets(baskets);
        user.setLoggedIn(false);
        userRepository.save(user);
        LOGGER.info("user entity added to DataBase");
    }

    public void cleanShoppingCart(String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Basket> toDel=basketRepository.findstoreIds(username);
            for (Basket basket : toDel) {
                basketRepository.delete(basket);
            }
            List<Basket> baskets = Collections.synchronizedList(new ArrayList<>());
            user.setBaskets(baskets);
            userRepository.save(user);
            LOGGER.info("user's shoppingCart is clean at the DataBase");
        }
    }

    public void addToCart(String username, Integer storeID, Integer productID, Integer quantity) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {

            // get the user if registered
            User user = optionalUser.get();

            // init product entity to add it to the relevant basket
            double price = inventoryRepository.findById(storeID).get().getProdPrice(productID);
            ProductBasket productEntity = new ProductBasket();
            Product product = productRepository.findById(productID).get();
            productEntity.setProductID(product);
            productEntity.setQuantity(quantity);
            productEntity.setUsername(username);
            productEntity.setStoreID(storeID);
            productEntity.setPrice(price);
            // init basket with the same id to find it in the list of baskets
            Basket newBasket = new Basket();
            BasketId bID=new BasketId();
            newBasket.setBasketId(bID);
            newBasket.setUsername(userRepository.findById(username).get());
            newBasket.setStoreID(storeRepository.findById(storeID).get());

            // get the baskets from the user
            List<Basket> baskets = user.getBaskets();

            // get the basket with the same id (if existed)
            Optional<Basket> basket = baskets.stream().filter(b -> b.equals(newBasket)).findFirst();

            if (basket.isPresent()) {
                // if existed add the product to the basket
                Basket existedBasket = basket.get();
                List<ProductBasket> products = existedBasket.getProducts();
                products.add(productEntity);
                existedBasket.setProducts(products);
                basketRepository.save(existedBasket);
            } else {
                // if not; init new basket and add it to the baskets
                List<ProductBasket> products = Collections.synchronizedList(new ArrayList<>());
                products.add(productEntity);
                newBasket.setProducts(products);
                basketRepository.save(newBasket);
            }
            LOGGER.info("product added to " + username + "'s cart at the DataBase");
        }
    }

    public List<Basket> inspectCart(String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {

            // get the user if registered
            User user = optionalUser.get();

            // get the baskets from the user
            return user.getBaskets();
        }
        LOGGER.severe("user does not exist");
        return null;
    }

    public void removeCartItem(String username, Integer storeID, Integer productID) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {

            // get the user if registered
            User user = optionalUser.get();

            // init basket with the same id to find it in the list of baskets
            Basket newBasket = new Basket();
            newBasket.setUsername(userRepository.findById(username).get());
            newBasket.setStoreID(storeRepository.findById(storeID).get());

            // get the baskets from the user
            List<Basket> baskets = user.getBaskets();

            // get the basket with the same id (if existed)
            Basket basket = baskets.stream().filter(b -> b.equals(newBasket)).findFirst().get();

            // get the products from the basket
            List<ProductBasket> products = basket.getProducts();

            // get the relevant product entity and remove it
            ProductBasket productEntity = products.stream().filter(p -> p.getProductID() == productID).findFirst()
                    .get();
            products.remove(productEntity);

            // update the basket
            basket.setProducts(products);
            basketRepository.save(basket);

            LOGGER.info(productID + " removed from " + username + "'s shoppingCart at the DataBase");
        }
    }

    public void EditPermissions(Integer storeID, String username, Boolean storeOwner, Boolean storeManager,
            Boolean editProducts, Boolean addOrEditPurchaseHistory, Boolean addOrEditDiscountHistory) {
        PermissionId permissionId=new PermissionId();
        permissionId.setStoreID(storeID);
        permissionId.setUsername(username);
        EmployerPermission employerPermission = employerPermissionRepository.findById(permissionId).get();

        employerPermission.setStoreOwner(storeOwner);
        employerPermission.setStoreManager(storeManager);
        employerPermission.setEditProducts(editProducts);
        employerPermission.setAddOrEditPurchaseHistory(addOrEditPurchaseHistory);
        employerPermission.setAddOrEditDiscountHistory(addOrEditDiscountHistory);

        employerPermissionRepository.save(employerPermission);

        LOGGER.info("permission updated at the DataBase");

    }

    public void AssignStoreManager(Integer storeID, String username,String owner,Boolean[] pType) {
        initPermission(storeID, username);
        PermissionId permissionId=new PermissionId();
        permissionId.setStoreID(storeID);
        permissionId.setUsername(owner);
        EmployerPermission employerPermission = employerPermissionRepository.findById(permissionId).get();
        List<EmployerPermission> employerPermissions=employerPermission.getEmployees();
        if (employerPermissions==null) {
            employerPermissions=new ArrayList<>();
        }
        PermissionId permissionId2=new PermissionId();
        permissionId2.setStoreID(storeID);
        permissionId2.setUsername(username);
        EmployerPermission malek= employerPermissionRepository.findById(permissionId2).get();
        employerPermissions.add(malek);
        employerPermission.setEmployees(employerPermissions);

        malek.setStoreOwner(false);
        malek.setStoreManager(true);
        malek.setEditProducts(pType[0]);
        malek.setAddOrEditDiscountHistory(pType[2]);
        malek.setAddOrEditPurchaseHistory(pType[1]);
        malek.setParentusername(owner);
        employerPermissionRepository.save(employerPermission);
        employerPermissionRepository.save(malek);

        LOGGER.info("user assigned as store manager at the DataBase");

    }

    public void AssignStoreOwner(Integer storeID, String username,String ownerUserName,Boolean[] pType) {
        initPermission(storeID, username);
        PermissionId permissionId=new PermissionId();
        permissionId.setStoreID(storeID);
        permissionId.setUsername(ownerUserName);
        EmployerPermission employerPermission = employerPermissionRepository.findById(permissionId).get();
        List<EmployerPermission> employerPermissions=employerPermission.getEmployees();
        if (employerPermissions==null) {
            employerPermissions=new ArrayList<>();
        }
        PermissionId permissionId2=new PermissionId();
        permissionId2.setStoreID(storeID);
        permissionId2.setUsername(username);
        EmployerPermission malek= employerPermissionRepository.findById(permissionId2).get();
        employerPermissions.add(malek);
        employerPermission.setEmployees(employerPermissions);

        malek.setStoreOwner(true);
        malek.setStoreManager(false);
        malek.setEditProducts(pType[0]);
        malek.setAddOrEditDiscountHistory(pType[2]);
        malek.setAddOrEditPurchaseHistory(pType[1]);
        malek.setParentusername(ownerUserName);
        employerPermissionRepository.save(employerPermission);
        employerPermissionRepository.save(malek);

        LOGGER.info("user assigned as store owner at the DataBase");

    }

    public void unassignUser(Integer storeID, String username,String owner) {
        PermissionId user=new PermissionId();
        user.setStoreID(storeID);
        user.setUsername(username);
        EmployerPermission employerUser = employerPermissionRepository.findById(user).get();

        PermissionId ownerPermission=new PermissionId();
        ownerPermission.setStoreID(storeID);
        ownerPermission.setUsername(owner);
        EmployerPermission employerOwner = employerPermissionRepository.findById(ownerPermission).get();
        List<EmployerPermission> employees=employerOwner.getEmployees();
        for(EmployerPermission e : employees){
            if(e.getPermissionId().getStoreID()==storeID && e.getPermissionId().getUsername().equals(username)){
                e.deleteEmployees();
                employees.remove(e);
                break;
            }
        }
        employerOwner.setEmployees(employees);
        employerPermissionRepository.save(employerOwner);
        PermissionId deluser=new PermissionId();
        deluser.setStoreID(employerUser.getPermissionId().getStoreID());
        deluser.setUsername(employerUser.getPermissionId().getUsername());
        EmployerPermission delemployerUser = employerPermissionRepository.findById(deluser).get();
        employerPermissionRepository.delete(delemployerUser);

        LOGGER.info("user unassigned from the DataBase");

    }

    public void resign(int storeID, String username) {
        PermissionId user=new PermissionId();
        user.setStoreID(storeID);
        user.setUsername(username);
        EmployerPermission employerUser = employerPermissionRepository.findById(user).get();
        if(employerUser.getParentusername()==null){
            List<EmployerPermission> employees=employerUser.getEmployees();
            for(EmployerPermission e : employees){
                if(e.getPermissionId().getStoreID()==storeID){
                    e.deleteEmployees();
                    employees.remove(e);
                    break;
                }
            }
            employerPermissionRepository.delete(employerUser);
            return;
        }
        PermissionId ownerPermission=new PermissionId();
        ownerPermission.setStoreID(storeID);
        ownerPermission.setUsername(employerUser.getParentusername());
        EmployerPermission parent = employerPermissionRepository.findById(ownerPermission).get();
        List<EmployerPermission> employees=parent.getEmployees();
        for(EmployerPermission e : employees){
            if(e.getPermissionId().getStoreID()==storeID && e.getPermissionId().getUsername().equals(username)){
                e.deleteEmployees();
                employees.remove(e);
                break;
            }
        }
        parent.setEmployees(employees);
        employerPermissionRepository.save(parent);
        PermissionId deluser=new PermissionId();
        deluser.setStoreID(employerUser.getPermissionId().getStoreID());
        deluser.setUsername(employerUser.getPermissionId().getUsername());
        EmployerPermission delemployerUser = employerPermissionRepository.findById(deluser).get();
        employerPermissionRepository.delete(delemployerUser);
    }

    public void suspendUser(String username) {
        User user = userRepository.findById(username).get();
        user.setSuspended(true);
        userRepository.save(user);
        LOGGER.info("user is suspended at the DataBase");
    }

    public void resumeUser(String username) {
        User user = userRepository.findById(username).get();
        user.setSuspended(false);
        userRepository.save(user);
        LOGGER.info("user is not suspended at the DataBase");
    }

    public List<String> viewSuspended() {
        List<User> users = userRepository.findAll();
        List<User> suspendedUsers = users.stream().filter(user -> user.getSuspended()).toList();
        List<String> usernames = suspendedUsers.stream().map(user -> user.getUsername()).toList();
        return usernames;
    }

    // @Transactional
    public void addCategory(String categoryName, int categoryId) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setCategoryID(categoryId);
        categoryRepository.save(category);
        LOGGER.info("added category to db");
    }

    public void initProduct(String productName, int productId, int categoryId, String description, String brand,
            double weight) {
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

    public void initStore(String username, String description, int id) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {

            // get the user if registered
            User user = optionalUser.get();
            Store store = new Store();
            store.setActive(false);
            store.setDesciption(description);
            store.setFirstOwner(user);
            store.setRating(0);
            store.setStoreID(id);
            store.setName("store name");
            Inventory inv = new Inventory();
            inv.setInventoryID(store.getStoreID());
            inv.setStoreID(store);
            inv.setProducts(new ArrayList<>());
            storeRepository.save(store);
            inventoryRepository.save(inv);
            LOGGER.info("added store to db");
        } else {
            LOGGER.severe("couldnt find user");
        }
    }

    @Transactional
    public void addProduct(int storeId, int productId, double price, int quantity) {
        Product product = productRepository.findById(productId).get();
        // Store tempStore = storeRepository.getReferenceById(storeId);
        // tempStore.getInventory().addProduct(product, price, quantity);
        Store store = storeRepository.findById(storeId).get();
        Inventory inventory = inventoryRepository.findById(store.getStoreID()).get();
        inventory.addProduct(product, price, quantity);
        inventoryRepository.save(inventory);
        LOGGER.info("added product to the store in db");
    }

    public void removeProduct(int storeId, int productId) {
        Product product = productRepository.getReferenceById(productId);
        // Store tempStore = storeRepository.getReferenceById(storeId);
        Store store = storeRepository.findById(storeId).get();
        Inventory inventory = inventoryRepository.findById(store.getStoreID()).get();
        inventory.removeProduct(product);
        inventoryRepository.save(inventory);
        LOGGER.info("removed product from store in db");
    }

    public void EditProductPrice(int productId, int storeId, Double newPrice) {
        Product product = productRepository.getReferenceById(productId);
        // Store tempStore = storeRepository.getReferenceById(storeId);
        Store store = storeRepository.findById(storeId).get();
        Inventory inventory = inventoryRepository.findById(store.getStoreID()).get();
        inventory.editPrice(product, newPrice);
        inventoryRepository.save(inventory);
        LOGGER.info("replaced price in db");
    }

    public void EditProductQuantity(int productId, int storeId, int newQuantity) {
        Product product = productRepository.getReferenceById(productId);
        // Store tempStore = storeRepository.getReferenceById(storeId);
        Store store = storeRepository.findById(storeId).get();
        Inventory inventory = inventoryRepository.findById(store.getStoreID()).get();
        inventory.editQuantity(product, newQuantity);
        inventoryRepository.save(inventory);
        LOGGER.info("replaced quantity in db");
    }

    public void openStore(int storeId) {
        Store store = storeRepository.getReferenceById(storeId);
        store.setActive(true);
    }

    public void closeStore(int storeId) {
        Store store = storeRepository.getReferenceById(storeId);
        store.setActive(false);
    }

    public Store getStore(int storeId) {
        return storeRepository.findById(storeId).get();
    }

    public void removePurchaseHistory(int purchaseId) {
        purchaseHistoryRepository.deleteById(purchaseId);
        LOGGER.info("deleted " + purchaseId + " history from store");
    }

    public List<PurchaseHistory> getPutchaseHistory() {
        return purchaseHistoryRepository.findAll();
    }

    public List<Store> getAllStores() {
        // get all stores
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
            double discountPercentage, int categoryId, int productId, int storeId, String username, int parentid,
            String type,int selfid) {
        DiscountPolicy discountPolicy = new DiscountPolicy();
        discountPolicy.setStandard(standard);
        discountPolicy.setConditionalPrice(conditionalPrice);
        discountPolicy.setConditionalQuantity(conditionalQuantity);
        discountPolicy.setDiscountPercentage(discountPercentage);
        discountPolicy.setCategoryId(categoryId);
        discountPolicy.setProductId(productId);
        Store store = storeRepository.findById(storeId).get();
        discountPolicy.setStoreId(store);
        discountPolicy.setUsername(username);
        Optional<DiscountPolicy> dp = discountRepository.findById(parentid);
        if (!dp.isPresent()) {
            // false, -1, -1, -1, -1, -1, storeId, username, id, "xor
            DiscountPolicy newdp = new DiscountPolicy(0, "And", true, -1.0, -1.0, 0.0, storeId, username, categoryId, -1,
                    null, new ArrayList<>());
            discountRepository.save(newdp);

        }
            DiscountPolicy malek=discountRepository.findById(parentid).get();
            discountPolicy.setParentDiscount(malek);
            discountPolicy.setType(type);
            discountPolicy.setDiscountId(selfid);
            discountRepository.save(discountPolicy);
            List<DiscountPolicy> subDiscountPolicies = malek.getSubDiscounts();
            if (subDiscountPolicies==null) {
                subDiscountPolicies=new ArrayList<>();
            }
            subDiscountPolicies.add(discountPolicy);
            malek.setSubDiscounts(subDiscountPolicies);
            discountRepository.save(malek);
        
    }

    public void addPurchasePolicy(int quantity, double price, LocalDate date, int atLeast, double weight, double age,
            double userAge, int categoryId, int productId, String username, int storeId, Boolean immediate,
            int parentid, String type,int selfid) {
        PurchasePolicy purchasePolicy = new PurchasePolicy();
        purchasePolicy.setQuantity(quantity);
        purchasePolicy.setPrice(price);
        if(date==null){
            purchasePolicy.setDate("");
        }else{
            purchasePolicy.setDate(date.toString());
        }
        purchasePolicy.setAtLeast(atLeast);
        purchasePolicy.setWeight(weight);
        purchasePolicy.setAge(userAge);
        purchasePolicy.setUserAge(userAge);
        purchasePolicy.setCategoryId(categoryId);
        purchasePolicy.setProductId(productId);
        purchasePolicy.setUsername(username);
        Store store = storeRepository.findById(storeId).get();
        purchasePolicy.setStoreId(store);
        purchasePolicy.setImmediate(immediate);
        Optional<PurchasePolicy> pp = purchaseRepository.findById(parentid);
        if (!pp.isPresent()) {
            PurchasePolicy newpp=new PurchasePolicy(0, type, store.getStoreID(), quantity, price, "", atLeast, weight, age, userAge, categoryId, productId, username, immediate, null, new ArrayList<>());
            purchaseRepository.save(newpp);
        }
        PurchasePolicy malek=purchaseRepository.findById(parentid).get();
        purchasePolicy.setParentPurchase(malek);
        purchasePolicy.setType(type);
        purchasePolicy.setPurchaseId(selfid);
        purchaseRepository.save(purchasePolicy);
        List<PurchasePolicy> subPurchasePolicies = malek.getSubPurchases();
        subPurchasePolicies.add(purchasePolicy);
        malek.setSubPurchases(subPurchasePolicies);
        purchaseRepository.save(malek);
    }

    public void updateQuantity(org.market.DomainLayer.backend.StorePackage.Store store){
        Inventory inv=inventoryRepository.findById(store.getId()).get();
        org.market.DomainLayer.backend.ProductPackage.Inventory inventory=store.getInventory();
        for(Map.Entry<Integer, double[]> entry:inventory.getProducts().entrySet()){
            Product proc=productRepository.findById(entry.getKey()).get();
            inv.editQuantity(proc, (int)entry.getValue()[0]);
        }
        inventoryRepository.save(inv);
    }

    public void addSystemManager(String username){
        User user=userRepository.findById(username).get();
        Market market = marketRepository.findById(0).get();
        List<User> users=market.getSystemManagers();
        users.add(user);
        market.setSystemManagers(users);
        marketRepository.save(market);
    }

    public void addToPurchaseHistory(Integer purchaseID,Map<Integer,double[]> products,Integer storeId,String username,Integer ovlprice){
        PurchaseHistory purchaseHistory=new PurchaseHistory();
        purchaseHistory.setPurchaseID(purchaseID);
        purchaseHistory.setStoreID(storeId);
        purchaseHistory.setUsername(username);
        purchaseHistory.setOvlprice(ovlprice);
        List<ProductScreenShot> prods=new ArrayList<>();
        for(Map.Entry<Integer,double[]> prod : products.entrySet()){
            ProductScreenShot pe=new ProductScreenShot();
            pe.setProductID(prod.getKey());
            pe.setPurchaseID(purchaseID);
            pe.setPrice(prod.getValue()[1]);
            pe.setQuantity((int)prod.getValue()[0]);
            prods.add(pe);
        }
        purchaseHistory.setProducts(prods);
        purchaseHistoryRepository.save(purchaseHistory);
    }

    public void initPermission(Integer storeId,String username){
        PermissionId permissionId=new PermissionId();
        permissionId.setStoreID(storeId);
        permissionId.setUsername(username);
        EmployerPermission employerPermission = new EmployerPermission();
        employerPermission.setPermissionId(permissionId);
        employerPermission.setEmployees(new ArrayList<>());
        employerPermission.setStoreOwner(true);
        employerPermission.setStoreManager(true);
        employerPermission.setEditProducts(true);
        employerPermission.setAddOrEditDiscountHistory(true);
        employerPermission.setAddOrEditPurchaseHistory(true);
        employerPermissionRepository.save(employerPermission);
    }
}
